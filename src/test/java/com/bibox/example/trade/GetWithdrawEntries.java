package com.bibox.example.trade;

import com.bibox.spots.BiboxSpotsClient;
import com.bibox.spots.Pager;
import com.bibox.spots.model.WithdrawEntry;

public class GetWithdrawEntries {

    public static void main(String[] args) throws Throwable {
        String apiKey = "2aa4a99148c65c4dbaed3a9718c87f83fc5d333e";
        String secretKey = "a05f00a59a1ffbf7e3a88b10f0e658e6a77ba474";
        BiboxSpotsClient client = new BiboxSpotsClient(apiKey, secretKey);
        Pager<WithdrawEntry> entries = client.getWithDrawEntries("", 1, 50);
        entries.getItems().forEach(System.out::println);
        WithdrawEntry entry = client.getTransferOutEntry(778067);
        System.out.println(entry);
    }

}
