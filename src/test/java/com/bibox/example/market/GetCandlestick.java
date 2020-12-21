package com.bibox.example.market;

import com.bibox.spots.BiboxSpotsClient;
import com.bibox.spots.model.Candlestick;
import com.bibox.spots.model.enums.TimeInterval;

import java.util.List;

public class GetCandlestick {
    public static void main(String[] args) throws Throwable {
        BiboxSpotsClient client = new BiboxSpotsClient();
        List<Candlestick> res = client.getCandlestick("BTC_USDT", TimeInterval.DAILY);
        System.out.println(res);
    }

}
