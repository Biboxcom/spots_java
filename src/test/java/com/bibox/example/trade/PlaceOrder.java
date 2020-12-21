package com.bibox.example.trade;

import com.bibox.spots.BiboxSpotsClient;
import com.bibox.spots.model.LimitOrder;
import com.bibox.spots.model.MarketOrder;
import com.bibox.spots.model.enums.TradeSide;

import java.math.BigDecimal;

public class PlaceOrder {

    public static void main(String[] args) throws Throwable {
        String apiKey = "2aa4a99148c65c4dbaed3a9718c87f83fc5d333e";
        String secretKey = "a05f00a59a1ffbf7e3a88b10f0e658e6a77ba474";
        BiboxSpotsClient client = new BiboxSpotsClient(apiKey, secretKey);
        placeLimitOrder(client);
        // placeMarketOrder(client); // 现不支持市价单
    }

    private static void placeMarketOrder(BiboxSpotsClient client) throws Throwable {
        // market order
        MarketOrder marketOrder = new MarketOrder();
        marketOrder.setSymbol("ETH_USDT");
        marketOrder.setQuantity(BigDecimal.valueOf(0.001));
        marketOrder.setSide(TradeSide.BID);
        String orderId = client.placeOrder(marketOrder);
        System.out.println("the market order_id: " + orderId);
    }

    private static void placeLimitOrder(BiboxSpotsClient client) throws Throwable {
        // limit order
        LimitOrder limitOrder = new LimitOrder();
        limitOrder.setSymbol("ETH_USDT");
        limitOrder.setQuantity(BigDecimal.valueOf(0.01));
        limitOrder.setSide(TradeSide.BID);
        limitOrder.setPrice(BigDecimal.valueOf(540));
        String orderId = client.placeOrder(limitOrder);
        System.out.println("the limit order_id: " + orderId);
        // 13079790338691530
    }

}
