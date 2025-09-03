package com.gap.learning.foodapp.service;

import com.gap.learning.foodapp.dto.Vendor;
import com.gap.learning.foodapp.repository.VendorRepository;

import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class VendorService {
    private final VendorRepository repo;
 
    public VendorService(VendorRepository repo) {
        this.repo = repo;
    }
 
    public List<Vendor> getAllVendors() {
        return repo.findAll();
    }
 
    public Vendor getVendorById(String id) {
        return repo.findByVendorId(id);
    }
 
    public Vendor addVendor(Vendor vendor) {
        return repo.save(vendor);
    }
 
    public Vendor updateVendor(String id, Vendor vendor) {
        Vendor existing =  repo.findByVendorId(id);
        	if(existing != null) {
        		 existing.setName(vendor.getName());
                 existing.setEmail(vendor.getEmail());
                 existing.setPhone(vendor.getPhone());
                 existing.setAddress(vendor.getAddress());
                 return repo.save(existing);
        	}else {
        		return null;
        	}
           
    }
 
    public void deleteVendor(String id) {
        repo.deleteById(id);
    }
}