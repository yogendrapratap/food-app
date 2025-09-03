package com.gap.learning.foodapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gap.learning.foodapp.dto.Vendor;
import com.gap.learning.foodapp.service.VendorService;

@RestController
@RequestMapping("/vendor")
public class VendorController {
	
	@Autowired
    private VendorService service;
	 
 
    @GetMapping
    public List<Vendor> getAllVendors() {
        return service.getAllVendors();
    }
 
    @GetMapping("/{vendorId}")
    public Vendor getVendor(@PathVariable String vendorId) {
        return service.getVendorById(vendorId);
    }
 
    @PostMapping("/add")
    public Vendor addVendor(@RequestBody Vendor vendor) {
        return service.addVendor(vendor);
    }
 
    @PutMapping("/{vendorId}")
    public Vendor updateVendor(@PathVariable String vendorId, @RequestBody Vendor vendor) {
        return service.updateVendor(vendorId, vendor);
    }
 
    @DeleteMapping("/{vendorId}")
    public String deleteVendor(@PathVariable String vendorId) {
        service.deleteVendor(vendorId);
        return "Vendor deleted with id " + vendorId;
    }

}
