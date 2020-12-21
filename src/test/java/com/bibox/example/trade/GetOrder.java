package com.bibox.example.trade;

import com.bibox.spots.BiboxSpotsClient;
import com.bibox.spots.OrderQuery;
import com.bibox.spots.Pager;
import com.bibox.spots.model.Order;

public class GetOrder {

    public static void main(String[] args) throws Throwable {
        String apiKey = "2aa4a99148c65c4dbaed3a9718c87f83fc5d333e";
        String secretKey = "a05f00a59a1ffbf7e3a88b10f0e658e6a77ba474";
        BiboxSpotsClient client = new BiboxSpotsClient(apiKey, secretKey);
        getOrder(client);
        getOpenOrders(client);
        getClosedOrders(client);
    }

    private static void getOrder(BiboxSpotsClient client) throws Throwable {
        System.out.println(client.getOrder("13079790325371098"));
    }

    private static void getOpenOrders(BiboxSpotsClient client) throws Throwable {
        OrderQuery query = new OrderQuery();
        query.setPage(1);
        query.setSize(10);

        Pager<Order> ordersByPage = client.getOpenOrders(query);
        System.out.println(ordersByPage);
        System.out.println(ordersByPage.getItems().size());
    }

    private static void getClosedOrders(BiboxSpotsClient client) throws Throwable {
        OrderQuery query = new OrderQuery();
        query.setPage(1);
        query.setSize(10);
        query.setBase("BTC");
        Pager<Order> ordersByPage = client.getClosedOrders(query);
        System.out.println(ordersByPage);
        System.out.println(ordersByPage.getItems().size());
    }

}
