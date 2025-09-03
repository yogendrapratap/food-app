package com.gap.learning.foodapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gap.learning.foodapp.message.FoodItemMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KafkaProducerServiceTest {

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Set private kafkaTopic field via reflection
        Field topicField = KafkaProducerService.class.getDeclaredField("kafkaTopic");
        topicField.setAccessible(true);
        topicField.set(kafkaProducerService, "test-topic");
    }

    @Test
    void testSendMessageAsync_Success() throws Exception {
        FoodItemMessage message = new FoodItemMessage();
        message.setId("1");
        message.setItemName("Pizza");

        String json = "{\"id\":\"1\",\"itemName\":\"Pizza\"}";
        when(objectMapper.writeValueAsString(message)).thenReturn(json);

        // Mock a successful Kafka send
        SendResult<String, String> sendResultMock = mock(SendResult.class);
        ProducerRecord<String, String> producerRecordMock = mock(ProducerRecord.class);
        when(sendResultMock.getProducerRecord()).thenReturn(producerRecordMock);
        when(producerRecordMock.key()).thenReturn("1");
        when(producerRecordMock.partition()).thenReturn(0);

        CompletableFuture<SendResult<String, String>> futureMock = new CompletableFuture<>();
        futureMock.complete(sendResultMock);
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(futureMock);

        CompletableFuture<SendResult<String, String>> resultFuture = kafkaProducerService.sendMessageAsync(message);

        assertNotNull(resultFuture);
        assertTrue(resultFuture.isDone());

        verify(objectMapper).writeValueAsString(message);
        verify(kafkaTemplate).send(any(ProducerRecord.class));
    }

    @Test
    void testSendMessageAsync_Failure() throws Exception {
        FoodItemMessage message = new FoodItemMessage();
        message.setId("2");
        message.setItemName("Burger");

        String json = "{\"id\":\"2\",\"itemName\":\"Burger\"}";
        when(objectMapper.writeValueAsString(message)).thenReturn(json);

        // Mock a failed Kafka send
        CompletableFuture<SendResult<String, String>> futureMock = new CompletableFuture<>();
        futureMock.completeExceptionally(new RuntimeException("Kafka failed"));
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(futureMock);

        CompletableFuture<SendResult<String, String>> resultFuture = kafkaProducerService.sendMessageAsync(message);

        assertNotNull(resultFuture);
        assertTrue(resultFuture.isCompletedExceptionally());

        verify(objectMapper).writeValueAsString(message);
        verify(kafkaTemplate).send(any(ProducerRecord.class));
    }

    @Test
    void testSendMessageAsync_JsonProcessingException() throws Exception {
        FoodItemMessage message = new FoodItemMessage();
        message.setId("3");
        message.setItemName("Sandwich");

        when(objectMapper.writeValueAsString(message)).thenThrow(new JsonProcessingException("fail") {});

        assertThrows(JsonProcessingException.class, () -> kafkaProducerService.sendMessageAsync(message));

        verify(objectMapper).writeValueAsString(message);
        verifyNoInteractions(kafkaTemplate);
    }
}
