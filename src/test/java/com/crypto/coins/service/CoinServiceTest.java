package com.crypto.coins.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.crypto.coins.dto.CoinBaseResponse;
import com.crypto.coins.dto.PriceHistory;
import com.crypto.coins.exception.UnsupportedDigitalCoin;
import com.crypto.coins.exception.ValidationException;

@ExtendWith(SpringExtension.class)
public class CoinServiceTest {

    @InjectMocks
    private CoinService coinService;

    @Mock
    private RestTemplate restTemplate;
    
    @Value("${coinbase.url}")
    private String coinBaseUrl;
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @BeforeEach
    public void setUp() {
        // Mocking the URL value
        coinService.coinBaseUrl = "https://api.coindesk.com/v1/bpi/historical/close.json";
    }

    @Test
    public void testGetHistory_Success() {
        // Mock Response
        Map<String, String> mockBpi = new HashMap<>();
        mockBpi.put("11-02-2025", "96722.190");
        mockBpi.put("12-02-2025", "96043.711");
        CoinBaseResponse mockResponse = new CoinBaseResponse();
        mockResponse.setBpi(mockBpi);
        
        when(restTemplate.getForObject(anyString(),any() , anyString(), anyString()))
                .thenReturn(mockResponse);

        PriceHistory history;
		try {
			history = coinService.getHistory("BTC", "11-02-2025", "12-02-2025");
			assertEquals("BTC", history.getCode());
	        assertEquals("USD", history.getCurrency());
	        assertEquals("11-02-2025", history.getFromDate());
	        assertEquals("12-02-2025", history.getToDate());
	        assertEquals("96722.190", history.getHigh());
	        assertEquals("96043.711", history.getLow());
		} catch (UnsupportedDigitalCoin | ValidationException e) {
			e.printStackTrace();
		}

        
    }

    @Test
    public void testGetHistory_DefaultDateRange() {
        PriceHistory history;
		try {
			history = coinService.getHistory("BTC", "", "");
			 assertEquals("BTC", history.getCode());
		     assertEquals("USD", history.getCurrency());
		} catch (UnsupportedDigitalCoin | ValidationException e) {
			e.printStackTrace();
		}
       
    }

    @Test
    public void testGetHistory_EmptyCodeDefaultsToBTC() {
        PriceHistory history;
		try {
			history = coinService.getHistory("", "11-02-2025", "12-02-2025");
			 assertEquals("BTC", history.getCode());
		} catch (UnsupportedDigitalCoin | ValidationException e) {
			e.printStackTrace();
		}
       
    }

    @Test
    public void testGetHistory_UnsupportedDigitalCoin() {
        assertThrows(UnsupportedDigitalCoin.class, () -> {
            coinService.getHistory("ETH", "11-02-2025", "12-02-2025");
        });
    }

    @Test
    public void testGetHistory_InvalidDateFormat() {
        assertThrows(ValidationException.class, () -> {
            coinService.getHistory("BTC", "11/02/2025", "12-02-2025");
        });
    }

    @Test
    public void testGetHistory_FromDateAfterToDate() {
        assertThrows(ValidationException.class, () -> {
            coinService.getHistory("BTC", "12-02-2025", "11-02-2025");
        });
    }

    @Test
    public void testGetHistory_OneDateEmpty_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> {
            coinService.getHistory("BTC", "11-02-2025", "");
        });
    }

    @Test
    public void testGetHistory_NullDates_DefaultsToLast7Days() {
        PriceHistory history;
		try {
			history = coinService.getHistory("BTC", null, null);
			assertEquals("BTC", history.getCode());
	        assertEquals("USD", history.getCurrency());
		} catch (UnsupportedDigitalCoin | ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
    }
}
