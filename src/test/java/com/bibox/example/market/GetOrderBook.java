package com.bibox.example.market;

import com.bibox.spots.BiboxSpotsClient;
import com.bibox.spots.model.OrderBook;

public class GetOrderBook {
    public static void main(String[] args) throws Throwable {
        BiboxSpotsClient client = new BiboxSpotsClient();
        OrderBook orderBook = client.getOrderBook("BTC_USDT");

        // ask1->askN
        orderBook.getAskBook().iterator().forEachRemaining(priceLevel ->
                System.out.println("the ask: " + priceLevel)
        );
        // bid1->bidN
        orderBook.getBidBook().iterator().forEachRemaining(priceLevel ->
                System.out.println("the bid: " + priceLevel)
        );

        // ...
    }
}
