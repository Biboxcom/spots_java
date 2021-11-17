package com.bibox.example.websocket;

import com.bibox.spots.BiboxSpotsClient;

public class SubTradeEvent {

    public static void main(String[] args) {
        BiboxSpotsClient client = new BiboxSpotsClient();
        String symbol = "BTC_USDT";
        client.subscribeTrade(symbol, (x) -> {
            x.forEach(System.out::println);
            // ...
        });
        // client.unSubscribeTradeEvent(symbol);
    }

}
