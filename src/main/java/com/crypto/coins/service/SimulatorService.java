package com.crypto.coins.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.crypto.coins.dto.CoinBaseResponse;
import com.crypto.coins.exception.UnsupportedDigitalCoin;
import com.crypto.coins.exception.ValidationException;
import com.crypto.coins.util.CoinUtil;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SimulatorService {
	
	private CoinUtil coinUtil;
	
	@PostConstruct
	public void init() {
		coinUtil = CoinUtil.getInstacnce();
	}
	
	public CoinBaseResponse getHistory(String code, String from, String to) throws UnsupportedDigitalCoin, ValidationException {
		if(StringUtils.isEmpty(code)) {
			code = "BTC";
		}
		
		boolean isFromDateEmpty = StringUtils.isEmpty(from);
		boolean isToDateEmpty =  StringUtils.isEmpty(to);
		if((isFromDateEmpty||isToDateEmpty) && ((!isFromDateEmpty || !isToDateEmpty))) {
			throw new ValidationException("Both from and to date are required");
		}
		
		log.info("from: {}, to:{}",from,to);
		Date fromDate, toDate;
		//Default rages set last 7 days
		if(isFromDateEmpty || isToDateEmpty) {
			Calendar cal = Calendar.getInstance();
			toDate = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			fromDate = cal.getTime();
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			try {
				fromDate = sdf.parse(from);
				toDate = sdf.parse(to);
			} catch (ParseException e) {
				log.error("Invlid Date Format: ",e);
				throw new ValidationException("Invalid Date Format");
			}			
		}
		
		if(fromDate.after(toDate)) {
			throw new ValidationException("From Date Should Be Earlier Than To Date");
		}
		
		
		
		if(!code.equalsIgnoreCase("BTC")) {
			throw new UnsupportedDigitalCoin("Unsupported Digital Coin");
		}
		
		return coinUtil.getRandomPriceHistory( fromDate, toDate);
	}

}
