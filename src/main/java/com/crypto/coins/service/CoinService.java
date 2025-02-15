package com.crypto.coins.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.crypto.coins.dto.CoinBaseResponse;
import com.crypto.coins.dto.PriceHistory;
import com.crypto.coins.exception.UnsupportedDigitalCoin;
import com.crypto.coins.exception.ValidationException;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CoinService {

	@Value("${coinbase.url}") String coinBaseUrl;

	@Autowired
	private RestTemplate restTemplate;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	@PreAuthorize("hasAuthority('ROLE_USER')")
	public PriceHistory getHistory(String code, String from, String to)
			throws UnsupportedDigitalCoin, ValidationException {
		if (StringUtils.isEmpty(code)) {
			code = "BTC";
		}

		boolean isFromDateEmpty = StringUtils.isEmpty(from);
		boolean isToDateEmpty = StringUtils.isEmpty(to);
		if ((isFromDateEmpty || isToDateEmpty) && ((!isFromDateEmpty || !isToDateEmpty))) {
			throw new ValidationException("Both from and to date are required");
		}

		Date fromDate, toDate;
		// Default rages set last 7 days
		if (isFromDateEmpty || isToDateEmpty) {
			Calendar cal = Calendar.getInstance();
			toDate = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			fromDate = cal.getTime();
		} else {
			try {
				fromDate = sdf.parse(from);
				toDate = sdf.parse(to);
			} catch (ParseException e) {
				log.error("Invlid Date Format: ", e.getMessage());
				throw new ValidationException("Invalid Date Format");
			}
		}

		if (fromDate.after(toDate)) {
			throw new ValidationException("From Date Should Be Earlier Than To Date");
		}

		if (!code.equalsIgnoreCase("BTC")) {
			throw new UnsupportedDigitalCoin("Unsupported Digital Coin");
		}

		log.info("Coin base URL: {}", coinBaseUrl);
		CoinBaseResponse response = restTemplate.getForObject(coinBaseUrl, CoinBaseResponse.class, from, to);

		PriceHistory history = PriceHistory.builder().code(code).currency("USD").fromDate(sdf.format(fromDate)).toDate(sdf.format(toDate))
				.priceIndices(response==null?new HashMap():response.getBpi()).build();

		if (response != null && response.getBpi() != null) {
			Optional<Map.Entry<String, String>> maxPrice = response.getBpi().entrySet().stream()
					.max(Map.Entry.comparingByValue());

			// Find Minimum Price
			Optional<Map.Entry<String, String>> minPrice = response.getBpi().entrySet().stream()
					.min(Map.Entry.comparingByValue());

			maxPrice.ifPresent(entry -> history.setHigh(entry.getValue()));
			minPrice.ifPresent(entry -> history.setLow(entry.getValue()));
		}
		return history;
	}

}
