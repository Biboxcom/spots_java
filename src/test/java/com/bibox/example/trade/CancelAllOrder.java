package com.bibox.example.trade;

import com.bibox.spots.BiboxSpotsClient;

public class CancelAllOrder {

    public static void main(String[] args) throws Throwable {
        String apiKey = "";
        String secretKey = "";
        BiboxSpotsClient client = new BiboxSpotsClient(apiKey, secretKey);
        client.cancelAllOrder("BTC_USDT");
    }

}