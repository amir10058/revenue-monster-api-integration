package com.neurogine.rmai.service;

public class TopupRequestGenerator {

    public static String generateRequest() {
        // Construct the JSON payload for the request
        return "{ \"amount\": 1000, \"currencyType\": \"MYR\" }";
    }
}
