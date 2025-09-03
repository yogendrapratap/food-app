package com.gap.learning.foodapp.dto;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vendors")
public class Vendor {
    public Vendor(String vendorId, String name, String email, String phone, String address) {
		this.vendorId = vendorId;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}
	@Id
	//@Field("vendorId")
    private String vendorId;
    private String name;
    private String email;
    private String phone;
    private String address;
    public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
