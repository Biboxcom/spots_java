package com.bibox.example.websocket;

import com.bibox.spots.BiboxSpotsClient;
import com.bibox.spots.model.enums.TimeInterval;

public class SubCandlestickEvent {

    public static void main(String[] args) {
        BiboxSpotsClient client = new BiboxSpotsClient();
        String symbol = "BTC_USDT";
        client.subscribeCandlestick(symbol, TimeInterval.ONE_MINUTE, (x) -> {
            x.forEach(System.out::println);
            // ...
        });
        // client.unSubscribeCandlestickEvent(symbol,CandlestickInterval.ONE_MINUTE);
    }

}
