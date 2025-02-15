package com.crypto.coins.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Coin {

	private String name;
	private String code;
	private Double price;
	private Date lastUpatedTime;
	private Double rateInUSD;
}
