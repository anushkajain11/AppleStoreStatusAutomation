package com.api.status;

public class CountryStatus {
	String name;
	String status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "CountryStatus [name=" + name + ", status=" + status + "]";
	}
}
