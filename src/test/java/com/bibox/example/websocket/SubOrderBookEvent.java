package com.bibox.example.websocket;

import com.bibox.spots.BiboxSpotsClient;

public class SubOrderBookEvent {

    public static void main(String[] args) {
        BiboxSpotsClient client = new BiboxSpotsClient();
        String symbol = "BIX_USDT";
        client.subscribeOrderBook(symbol, x -> {
            System.out.println(x.getAskBook().getPriceLevel(0));
            System.out.println(x.getBidBook().getPriceLevel(0));
            // ...
        });
    }

}
