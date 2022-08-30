package com.bibox.example.trade;

import com.bibox.spots.BiboxSpotsClient;
import com.bibox.spots.OrderIdSet;

public class CancelOrderBatch {

    public static void main(String[] args) throws Throwable {
        String apiKey = "";
        String secretKey = "";
        BiboxSpotsClient client = new BiboxSpotsClient(apiKey, secretKey);
        OrderIdSet orderIdSet = new OrderIdSet();
        orderIdSet.add("14481667651527753");
        orderIdSet.add("14484966187427632");
        client.cancelBatchOrder(orderIdSet);
    }

}
