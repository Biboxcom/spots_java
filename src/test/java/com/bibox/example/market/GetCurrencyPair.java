package com.bibox.example.market;

import com.bibox.spots.BiboxSpotsClient;
import com.bibox.spots.model.CurrencyPair;

import java.util.List;

public class GetCurrencyPair {
    public static void main(String[] args) throws Throwable {
        BiboxSpotsClient client = new BiboxSpotsClient();
        List<CurrencyPair> sp = client.getCurrencyPairs();
        System.out.println(sp);
        System.out.println(sp.size());
    }
}
