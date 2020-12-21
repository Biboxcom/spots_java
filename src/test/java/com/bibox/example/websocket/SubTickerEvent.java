package com.bibox.example.websocket;

import com.bibox.spots.BiboxSpotsClient;

public class SubTickerEvent {

    public static void main(String[] args) {
        BiboxSpotsClient client = new BiboxSpotsClient();
        // 处理业务逻辑
        String symbol = "BIX_USDT";
        client.subscribeTicker(symbol, x -> {
            System.out.println(x);
            // ...
        });
        // client.unSubscribeTickerEvent(symbol);
    }

}
