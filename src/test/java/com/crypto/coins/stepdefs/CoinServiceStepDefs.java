package com.crypto.coins.stepdefs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import com.crypto.coins.CoinsApplication;
import com.crypto.coins.dto.CoinBaseResponse;
import com.crypto.coins.dto.PriceHistory;
import com.crypto.coins.exception.UnsupportedDigitalCoin;
import com.crypto.coins.exception.ValidationException;
import com.crypto.coins.service.CoinService;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
@ContextConfiguration(classes = CoinsApplication.class)
public class CoinServiceStepDefs {

    @Autowired
    private CoinService coinService;

    @MockitoBean
    private RestTemplate restTemplate;

    private String code;
    private String from;
    private String to;
    private PriceHistory history;
    private Exception thrownException;

    @Given("the user wants the history for coin {string}")
    public void theUserWantsTheHistoryForCoin(String code) {
        this.code = code;
    }

    @Given("the date range is from {string} to {string}")
    public void theDateRangeIsFromTo(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @When("the request is made to the CoinService")
    public void theRequestIsMadeToTheCoinService() {
        try {
            // Mocking the API Response
            Map<String, String> mockBpi = new HashMap<>();
            mockBpi.put("11-02-2025", "96722.190");
            mockBpi.put("12-02-2025", "96043.711");
            CoinBaseResponse mockResponse = new CoinBaseResponse();
            mockResponse.setBpi(mockBpi);
            
            when(restTemplate.getForObject(anyString(), CoinBaseResponse.class, anyString(), anyString()))
                    .thenReturn(mockResponse);

            history = coinService.getHistory(code, from, to);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the service should return price history with the highest price {string} and lowest price {string}")
    public void theServiceShouldReturnPriceHistoryWithTheHighestPriceAndLowestPrice(String high, String low) {
        assertEquals(high, history.getHigh());
        assertEquals(low, history.getLow());
    }

    @Then("an UnsupportedDigitalCoin exception should be thrown")
    public void anUnsupportedDigitalCoinExceptionShouldBeThrown() {
        assertEquals(UnsupportedDigitalCoin.class, thrownException.getClass());
    }

    @Then("a ValidationException should be thrown with message {string}")
    public void aValidationExceptionShouldBeThrownWithMessage(String message) {
        assertEquals(ValidationException.class, thrownException.getClass());
        assertEquals(message, thrownException.getMessage());
    }
}
