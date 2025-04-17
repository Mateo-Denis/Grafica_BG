package utils;

import lombok.Getter;


@Getter
public class Client {
	private final String name;
	private final String address;
	private final String city;
	private final String phone;
	private final boolean isClient;

	public Client(String name, String address, String city, String phone, boolean isClient) {
		this.name = name;
		this.address = address;
		this.city = city;
		this.phone = phone;
		this.isClient = isClient;
	}


}
