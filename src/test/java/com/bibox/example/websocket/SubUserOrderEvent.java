package com.bibox.example.websocket;

import com.bibox.spots.BiboxSpotsClient;

public class SubUserOrderEvent {

    public static void main(String[] args) {
        String apiKey = "your apiKey";
        String secretKey = "your secretKey";
        BiboxSpotsClient client = new BiboxSpotsClient(apiKey, secretKey);
        client.subscribeOrder(x -> {
            x.forEach(System.out::println);
            // ...
        });
    }

}
