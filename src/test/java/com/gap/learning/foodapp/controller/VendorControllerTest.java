package com.gap.learning.foodapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gap.learning.foodapp.dto.Vendor;
import com.gap.learning.foodapp.service.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendorController.class)
public class VendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendorService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Vendor vendor;

    @BeforeEach
    void setUp() {
        vendor = new Vendor("v1", "Naresh", "naresh@example.com", "1234567890", "Hyderabad");
    }

    @Test
    void testGetAllVendors() throws Exception {
        List<Vendor> vendors = Arrays.asList(vendor);
        Mockito.when(service.getAllVendors()).thenReturn(vendors);

        mockMvc.perform(get("/vendor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vendorId").value("v1"))
                .andExpect(jsonPath("$[0].name").value("Naresh"));
    }

    @Test
    void testGetVendorById() throws Exception {
        Mockito.when(service.getVendorById("v1")).thenReturn(vendor);

        mockMvc.perform(get("/vendor/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("naresh@example.com"));
    }

    @Test
    void testAddVendor() throws Exception {
        Mockito.when(service.addVendor(any(Vendor.class))).thenReturn(vendor);

        mockMvc.perform(post("/vendor/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorId").value("v1"));
    }

    @Test
    void testUpdateVendor() throws Exception {
        Mockito.when(service.updateVendor(Mockito.eq("v1"), any(Vendor.class))).thenReturn(vendor);

        mockMvc.perform(put("/vendor/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Naresh"));
    }

    @Test
    void testDeleteVendor() throws Exception {
        doNothing().when(service).deleteVendor("v1");

        mockMvc.perform(delete("/vendor/v1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Vendor deleted with id v1"));
    }
}
