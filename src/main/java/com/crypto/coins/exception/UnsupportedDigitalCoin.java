package com.crypto.coins.exception;

public class UnsupportedDigitalCoin extends RuntimeException {
	
	private static final long serialVersionUID = -6448909780108548405L;

	public UnsupportedDigitalCoin(String message) {
		super(message);
	}

}
