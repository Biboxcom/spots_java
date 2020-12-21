package com.bibox.example.trade;

import com.bibox.spots.BiboxSpotsClient;

import java.math.BigDecimal;

public class Withdraw {

    public static void main(String[] args) throws Throwable {
        String apiKey = "2aa4a99148c65c4dbaed3a9718c87f83fc5d333e";
        String secretKey = "a05f00a59a1ffbf7e3a88b10f0e658e6a77ba474";
        BiboxSpotsClient client = new BiboxSpotsClient(apiKey, secretKey);
        System.out.println(client.withdraw("BTC", BigDecimal.valueOf(1),"Your addr"));
        // 一些币种提现必须指定memo标签，与地址组合标识唯一性，比如EOS
        System.out.println(client.withdraw("EOS", BigDecimal.valueOf(1),
                "Your addr","Your memo"));
    }

}
