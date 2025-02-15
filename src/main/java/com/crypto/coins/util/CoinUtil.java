package com.crypto.coins.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crypto.coins.dto.PriceHistory;
import com.crypto.coins.dto.CoinBaseResponse;

public class CoinUtil {
	
	private Logger log = LoggerFactory.getLogger(CoinUtil.class);
	
	private static volatile CoinUtil _instance;
	private DecimalFormat decimalFormat = new DecimalFormat("###0.000");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private CoinUtil() {
		if (_instance != null) {
            throw new IllegalStateException("Instance already exists");
        }
	}
	
	public static CoinUtil getInstacnce() {
		if(_instance == null) {
			synchronized (CoinUtil.class) {
                if (_instance == null) {
                	_instance = new CoinUtil();
                }
            }
		}	
		return _instance;
	}
	
	public CoinBaseResponse getRandomPriceHistory(Date from, Date to) {
		//List<PriceHistory> historyList = new ArrayList<PriceHistory>();
		Calendar fromDate = Calendar.getInstance();
		fromDate.setTimeZone(TimeZone.getTimeZone("IST"));
		fromDate.setTime(from);
		Map<String, String> bpiList= new HashMap<String, String>();
		while (!fromDate.getTime().after(to)) {
			double price = ThreadLocalRandom.current().nextDouble(96000.00, 97000.99);
			bpiList.put(dateFormat.format(fromDate.getTime()), decimalFormat.format(price));
			fromDate.add(Calendar.DAY_OF_MONTH, 1);
			/*
			 * PriceHistory history = PriceHistory.builder() .code(code)
			 * .date(dateFormat.format(fromDate.getTime()))
			 * .price(decimalFormat.format(price)) .currency("USD") .build();
			 * fromDate.add(Calendar.DAY_OF_MONTH, 1);
			 * System.out.println("History: "+history); historyList.add(history);
			 */
		}
		
		CoinBaseResponse response = CoinBaseResponse.builder()
				.bpi(bpiList)
				.time(new Date().toString())
				.build();
		System.out.println(response);
		return response;
	}
}
