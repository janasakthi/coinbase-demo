package com.crypto.coins.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceHistory {
	
	private String code;
	private String fromDate;
	private String toDate;	
	private String currency;
	private String low;
	private String high;
	private Map<String,String> priceIndices;
	
	
}
