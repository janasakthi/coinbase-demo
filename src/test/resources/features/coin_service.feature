Feature: Historical Bitcoin Price Service

  Scenario: Successful retrieval of historical Bitcoin prices
    Given the user wants the history for coin "BTC"
    And the date range is from "11-02-2025" to "12-02-2025"
    When the request is made to the CoinService
    Then the service should return price history with the highest price "96722.190" and lowest price "96043.711"

  Scenario: Unsupported digital coin is requested
    Given the user wants the history for coin "ETH"
    And the date range is from "11-02-2025" to "12-02-2025"
    When the request is made to the CoinService
    Then an UnsupportedDigitalCoin exception should be thrown

  Scenario: Invalid date format is provided
    Given the user wants the history for coin "BTC"
    And the date range is from "11/02/2025" to "12-02-2025"
    When the request is made to the CoinService
    Then a ValidationException should be thrown with message "Invalid Date Format"
