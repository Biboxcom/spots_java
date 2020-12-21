package com.bibox.example.market;

import com.bibox.spots.BiboxSpotsClient;

public class GetTrades {
    public static void main(String[] args) throws Throwable {
        BiboxSpotsClient client = new BiboxSpotsClient();
        System.out.println(client.getTrades("BTC_USDT",2));
    }
}
