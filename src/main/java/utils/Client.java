package utils;

public class Client {
	private String name;
	private String address;
	private String city;
	private String phone;
	private boolean isClient;

	public Client(String name, String address, String city, String phone, boolean isClient) {
		this.name = name;
		this.address = address;
		this.city = city;
		this.phone = phone;
		this.isClient = isClient;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getPhone() {
		return phone;
	}

	public boolean isClient() {
		return isClient;
	}
}
