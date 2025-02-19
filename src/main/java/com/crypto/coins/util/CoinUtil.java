package com.crypto.coins.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crypto.coins.dto.PriceHistory;
import com.crypto.coins.dto.CoinBaseResponse;

public class CoinUtil {
	
	private Logger log = LoggerFactory.getLogger(CoinUtil.class);
	
	private static volatile CoinUtil _instance;
	private DecimalFormat decimalFormat = new DecimalFormat("###0.000");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private Map<String,Map<String,String>> cache = new ConcurrentHashMap<String, Map<String,String>>();
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
	
	/***
	 * GEnerates random rate for the given date ranges. For internal testing. 
	 * @param from
	 * @param to
	 * @return
	 */
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
		}
		
		CoinBaseResponse response = CoinBaseResponse.builder()
				.bpi(bpiList)
				.time(new Date().toString())
				.build();
		return response;
	}
	
	/***
	 * Keeps the response records in cache for serving offline
	 * @param cuurencyCode
	 * @param coinBaseResponse
	 */
	public void updateCache(String cuurencyCode, CoinBaseResponse coinBaseResponse) {
		if (coinBaseResponse != null && coinBaseResponse.getBpi() != null) {
			if (cache.get(cuurencyCode) == null) {
				cache.put(cuurencyCode, new ConcurrentHashMap<String, String>());
			}
			Map<String, String> coinPriceCache = cache.get(cuurencyCode);
			coinBaseResponse.getBpi().forEach((key, value) -> coinPriceCache.put(key, value));
		}
	}

	/***
	 * provides cache data in case the public API fails, for the given date range if its availbe
	 * @param code
	 * @param from
	 * @param to
	 * @return
	 */
	public Map<String, String> loadFromCache(String code, String from, String to) {
		Map<String, String> cachedData = cache.get(code);
		if (cachedData == null || cachedData.size() == 0) {
			return new HashMap<String, String>();
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate startDate = LocalDate.parse(from, formatter);
		LocalDate endDate = LocalDate.parse(to, formatter);

		Map<String, String> filteredBpi = cachedData.entrySet().stream().filter(entry -> {
			LocalDate date = LocalDate.parse(entry.getKey(), formatter);
			return !date.isBefore(startDate) && !date.isAfter(endDate);
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return filteredBpi;
	}
}
