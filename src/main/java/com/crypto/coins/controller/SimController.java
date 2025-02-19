package com.crypto.coins.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crypto.coins.aspects.LogExecutionTime;
import com.crypto.coins.dto.CoinBaseResponse;
import com.crypto.coins.exception.UnsupportedDigitalCoin;
import com.crypto.coins.exception.ValidationException;
import com.crypto.coins.service.SimulatorService;

@RestController
@RequestMapping("/sim/api/v1/bpi/historical")
public class SimController {
	
	@Autowired
	private SimulatorService service;
	
	@GetMapping("")
	@LogExecutionTime
	public CoinBaseResponse getPriceHistory(@RequestParam(defaultValue = "BTC") String code,@RequestParam(required = false) String from,@RequestParam(required = false) String to) throws UnsupportedDigitalCoin, ValidationException {
		return service.getHistory(code, from, to);
	}

}
