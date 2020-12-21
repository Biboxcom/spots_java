package com.bibox.example.market;

import com.bibox.spots.BiboxSpotsClient;

public class GetTicker {
    public static void main(String[] args) throws Throwable {
        BiboxSpotsClient client = new BiboxSpotsClient();
        System.out.println(client.getTicker("BTC_USDT"));
    }
}
