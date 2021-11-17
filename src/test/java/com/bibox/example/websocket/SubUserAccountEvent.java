package com.bibox.example.websocket;

import com.bibox.spots.BiboxSpotsClient;

public class SubUserAccountEvent {

    public static void main(String[] args) {
        String apiKey = "your apiKey";
        String secretKey = "your secretKey";
        BiboxSpotsClient client = new BiboxSpotsClient(apiKey, secretKey);
        client.subscribeAccount(x -> {
            x.forEach(System.out::println);
            // ...
        });
        // client.unSubscribeUserAll();
    }

}
