/*
 * Copyright (C) 2020, Bibox.com. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.bibox.spots;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bibox.spots.model.*;

import java.util.ArrayList;
import java.util.List;

class JSONUtils {

    public static long parsePing(JSONObject json) {
        if (json.containsKey("ping")) {
            return json.getLong("ping");
        }
        return -1;
    }

    public static JSONObject parseError(String text) throws Throwable {
        if (!text.startsWith("{")) {
            throw new RuntimeException();
        }

        JSONObject json = JSON.parseObject(text);
        if (json.containsKey("state")) {
            int state = json.getInteger("state");
            String message = json.getString("msg");
            if (state != 0) {
                throw new RuntimeException(
                        String.format("Error (state:%s, message:%s)", state, message));
            }
        }
        return json;
    }

    public static boolean parseError(JSONObject json) {
        return "error".equals(json.getString("status"));
    }

    public static JSONObject parseObject(String text) throws Throwable {
        return parseError(text);
    }

    public static JSONObject parseObject(String text, String child) throws Throwable {
        return parseError(text).getJSONObject(child);
    }

    public static JSONArray parseArray(String text) throws Throwable {
        if (!text.startsWith("[")) {
            parseError(text);
            throw new RuntimeException();
        }
        return JSON.parseArray(text);
    }

    public static JSONArray parseArray(String text, String child) throws Throwable {
        text = parseObject(text).getString(child);
        if (!text.startsWith("[")) {
            parseError(text);
            throw new RuntimeException();
        }
        return JSON.parseArray(text);
    }

    public static List<CurrencyPair> parseSpots(String text) throws Throwable {
        JSONArray arr = parseArray(text, "result");
        List<CurrencyPair> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(CurrencyPair.parseResult(arr.getJSONObject(i)));
        }
        return list;
    }

    public static List<Trade> parseTrades(String text) throws Throwable {
        JSONArray arr = parseArray(text, "result");
        List<Trade> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(Trade.parseResult(arr.getJSONObject(i)));
        }
        return list;
    }

    public static List<Candlestick> parseCandlesticks(String text) throws Throwable {
        JSONArray arr = parseArray(text, "result");
        return parseCandlesticks(arr);
    }

    public static Ticker parseTicker(String text) throws Throwable {
        JSONObject obj = parseObject(text, "result");
        return Ticker.parseResult(obj);
    }

    public static List<Candlestick> parseCandlesticks(JSONArray arr) {
        List<Candlestick> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(Candlestick.parseResult(arr.getJSONObject(i)));
        }
        return list;
    }

    public static OrderBook parseOrderBook(String text) throws Throwable {
        JSONObject obj = parseObject(text, "result");
        return OrderBook.parseResult(obj);
    }

    public static JSONObject parseTradeLimit(String text) throws Throwable {
        return parseObject(text, "result");
    }

    public static Long parseTimestamp(String text) throws Throwable {
        return parseObject(text).getLong("time");
    }

    public static String parseOrderId(String text) throws Throwable {
        return parseObject(text).getString("result");
    }

    public static List<Account> parseAccount(String text) throws Throwable {
        JSONObject obj = parseObject(text, "result");
        return new ArrayList<>(Account.parseResult(obj).values());
    }

    public static Account parseAccount(String text,String symbol) throws Throwable {
        JSONObject obj = parseObject(text, "result");
        return Account.parseResult(obj).get(symbol);
    }

    public static List<Account> parseAccountEvent(JSONObject obj) {
        return Account.parseEvent(obj);
    }

    public static String parseDepositAddress(String text) throws Throwable {
        JSONObject obj = parseObject(text);
        return obj.getString("result");
    }

    public static long parseWithdrawId(String text) throws Throwable {
        JSONObject obj = parseObject(text);
        return obj.getInteger("result");
    }

    public static Pager<DepositEntry> parseDepositEntries(String text) throws Throwable {
        Pager<DepositEntry> pager = new Pager<>();

        JSONObject json = parseObject(text, "result");
        pager.setCount(json.getInteger("count"));
        pager.setPage(json.getInteger("page"));

        JSONArray arr = parseArray(json.getString("items"));
        ArrayList<DepositEntry> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(DepositEntry.parseResult(arr.getJSONObject(i)));
        }
        pager.setItems(list);
        return pager;
    }

    public static Pager<WithdrawEntry> parseWithDrawEntryEntries(String text) throws Throwable {
        Pager<WithdrawEntry> pager = new Pager<>();

        JSONObject json = parseObject(text, "result");
        pager.setCount(json.getInteger("count"));
        pager.setPage(json.getInteger("page"));

        JSONArray arr = parseArray(json.getString("items"));
        ArrayList<WithdrawEntry> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(WithdrawEntry.parseResult(arr.getJSONObject(i)));
        }
        pager.setItems(list);
        return pager;
    }

    public static WithdrawEntry parseTransferOutEntry(String text) throws Throwable {
        JSONObject obj = parseObject(text, "result");
        return WithdrawEntry.parseResult(obj);
    }

    public static List<AssetInfo> parseAssetInfos(String text) throws Throwable {
        JSONArray arr = parseArray(text, "result");
        List<AssetInfo> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(AssetInfo.parseResult(arr.getJSONObject(i)));
        }
        return list;
    }

    public static Pager<Bill> parseBills(String text) throws Throwable {
        Pager<Bill> pager = new Pager<>();

        JSONObject json = parseObject(text, "result");
        pager.setCount(json.getInteger("count"));
        pager.setPage(json.getInteger("page"));

        JSONArray arr = parseArray(json.getString("items"));
        ArrayList<Bill> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(Bill.parseResult(arr.getJSONObject(i)));
        }
        pager.setItems(list);
        return pager;
    }

    public static Pager<Fill> parseFillsByPage(String text) throws Throwable {
        Pager<Fill> pager = new Pager<>();

        JSONObject json = parseObject(text, "result");
        pager.setCount(json.getInteger("count"));
        pager.setPage(json.getInteger("page"));

        JSONArray arr = parseArray(json.getString("items"));
        ArrayList<Fill> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(Fill.parseResult(arr.getJSONObject(i)));
        }
        pager.setItems(list);
        return pager;
    }

    public static List<Fill> parseFills(String text) throws Throwable {
        JSONObject obj = parseObject(text, "result");
        JSONArray arr = parseArray(obj.getString("orderList"));
        ArrayList<Fill> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(Fill.parseResult(arr.getJSONObject(i)));
        }
        return list;
    }

    public static Order parseOrder(String text) throws Throwable {
        JSONObject obj = parseObject(text, "result");
        return LimitOrder.parseResult(obj);
    }

    public static Pager<Order> parseOrdersByPage(String text) throws Throwable {
        Pager<Order> pager = new Pager<>();

        JSONObject json = parseObject(text, "result");
        pager.setCount(json.getInteger("count"));
        pager.setPage(json.getInteger("page"));

        JSONArray arr = parseArray(json.getString("items"));
        ArrayList<Order> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(LimitOrder.parseResult(arr.getJSONObject(i)));
        }
        pager.setItems(list);
        return pager;
    }

    public static Ticker parseTickerEvent(JSONObject obj) {
        return Ticker.parseEvent(obj);
    }

    public static List<Trade> parseTradeEvent(JSONArray arr) {
        List<Trade> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(Trade.parseEvent(arr.getJSONObject(i)));
        }
        return list;
    }

    public static Order parseOrderEvent(JSONObject obj) {
        return LimitOrder.parseEvent(obj);
    }

    public static Fill parseFillEvent(JSONObject obj) {
        return Fill.parseEvent(obj);
    }

    public static List<Candlestick> parseCandlesticksNew(JSONArray arr) {
        List<Candlestick> list = new ArrayList<>();
        if (arr.size()>1) {
            list.add(Candlestick.parseResult(arr.getJSONArray(1)));
        }
        return list;
    }

    public static Ticker parseTickerEventNew(JSONArray obj) {
        return Ticker.parseEvent(obj);
    }

    public static List<Trade> parseTradeEventNew(JSONArray arr) {
        List<Trade> list = new ArrayList<>();
        list.add(Trade.parseEvent(arr));
        return list;
    }
    public static List<Trade> parseTradeEventNew(JSONArray arr, String pair) {
        List<Trade> list = new ArrayList<>();
        list.add(Trade.parseEvent(arr,pair));
        return list;
    }

}
