package com.bibox.example.trade;

import com.bibox.spots.BiboxSpotsClient;

public class GetAssetInfo {

    public static void main(String[] args) throws Throwable {
        String apiKey = "2aa4a99148c65c4dbaed3a9718c87f83fc5d333e";
        String secretKey = "a05f00a59a1ffbf7e3a88b10f0e658e6a77ba474";
        BiboxSpotsClient client = new BiboxSpotsClient(apiKey, secretKey);
        System.out.println(client.getAssetInfo("BTC"));
        System.out.println(client.getAssetInfos());
    }

}
