package com.gap.learning.foodapp.service;

import com.gap.learning.foodapp.dto.Vendor;
import com.gap.learning.foodapp.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VendorServiceTest {

    private VendorRepository repo;
    private VendorService service;

    private Vendor vendor;

    @BeforeEach
    void setUp() {
        repo = mock(VendorRepository.class);
        service = new VendorService(repo);
        vendor = new Vendor("v1", "Naresh", "naresh@example.com", "1234567890", "Hyderabad");
    }

    @Test
    void testGetAllVendors() {
        when(repo.findAll()).thenReturn(Arrays.asList(vendor));

        List<Vendor> result = service.getAllVendors();

        assertEquals(1, result.size());
        assertEquals("Naresh", result.get(0).getName());
        verify(repo, times(1)).findAll();
    }

    @Test
    void testGetVendorById() {
        when(repo.findByVendorId("v1")).thenReturn(vendor);

        Vendor result = service.getVendorById("v1");

        assertNotNull(result);
        assertEquals("v1", result.getVendorId());
        verify(repo).findByVendorId("v1");
    }

    @Test
    void testAddVendor() {
        when(repo.save(vendor)).thenReturn(vendor);

        Vendor result = service.addVendor(vendor);

        assertEquals("Naresh", result.getName());
        verify(repo).save(vendor);
    }

    @Test
    void testUpdateVendor_WhenExists() {
        Vendor updated = new Vendor("v1", "Updated", "updated@example.com", "9876543210", "Bangalore");

        when(repo.findByVendorId("v1")).thenReturn(vendor);
        when(repo.save(any(Vendor.class))).thenReturn(updated);

        Vendor result = service.updateVendor("v1", updated);

        assertNotNull(result);
        assertEquals("Updated", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        verify(repo).findByVendorId("v1");
        verify(repo).save(any(Vendor.class));
    }

    @Test
    void testUpdateVendor_WhenNotExists() {
        when(repo.findByVendorId("v2")).thenReturn(null);

        Vendor result = service.updateVendor("v2", vendor);

        assertNull(result);
        verify(repo).findByVendorId("v2");
        verify(repo, never()).save(any());
    }

    @Test
    void testDeleteVendor() {
        doNothing().when(repo).deleteById("v1");

        service.deleteVendor("v1");

        verify(repo).deleteById("v1");
    }
}
