package com.gap.learning.foodapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gap.learning.foodapp.dto.Vendor;

@Repository
public interface VendorRepository extends MongoRepository<Vendor, String> {
	Vendor findByVendorId(String vendorId);
}
