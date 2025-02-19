package com.crypto.coins.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.coins.aspects.LogExecutionTime;
import com.crypto.coins.dto.PriceHistory;
import com.crypto.coins.exception.UnsupportedDigitalCoin;
import com.crypto.coins.exception.ValidationException;
import com.crypto.coins.service.CoinService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/coin")
public class CoinController {
	
	@Autowired
	private CoinService coinService;
	
	@GetMapping("/history")
	@LogExecutionTime
	public PriceHistory getPriceHistory(@RequestParam(required = false) String code,@RequestParam(required = false) String from,@RequestParam(required = false) String to) throws UnsupportedDigitalCoin, ValidationException {
		return coinService.getHistory(code, from, to);
	}

}
