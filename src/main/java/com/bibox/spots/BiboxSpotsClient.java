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

import com.alibaba.fastjson.JSONObject;
import com.bibox.spots.model.*;
import com.bibox.spots.model.enums.DepositStatus;
import com.bibox.spots.model.enums.TimeInterval;
import com.bibox.util.HttpUtils;
import com.bibox.util.Listener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
public class BiboxSpotsClient extends BiboxSpotsClientBase {

    public BiboxSpotsClient() {
    }

    public BiboxSpotsClient(String apiKey, String secretKey) {
        super(apiKey, secretKey);
    }

    /**
     * 提交委托
     */
    public String placeOrder(Order order) throws Throwable {
        if (order instanceof LimitOrder) {
            return placeOrder((LimitOrder) order);
        } else if (order instanceof MarketOrder) {
            return placeOrder((MarketOrder) order);
        } else {
            throw new RuntimeException("couldn't support this order");
        }
    }

    /**
     * 撤销委托
     */
    public void cancelOrder(String orderId) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("orders_id", orderId);

        JSONUtils.parseError(doPost(URL_CANCEL_ORDER, json.toJSONString()));
    }

    /**
     * 从钱包转进现货
     *
     * @param symbol 划转币种
     * @param amount 数量
     */
    public void transferIn(String symbol, BigDecimal amount) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("symbol", symbol);
        json.put("amount", amount.toString());
        json.put("type", 0);

        JSONUtils.parseError(doPost(URL_TRANSFER_SPOT, json.toJSONString()));
    }

    /**
     * 从现货转进钱包
     *
     * @param symbol 划转币种
     * @param amount 数量
     */
    public void transferOut(String symbol, BigDecimal amount) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("symbol", symbol);
        json.put("amount", amount.toString());
        json.put("type", 1);

        JSONUtils.parseError(doPost(URL_TRANSFER_SPOT, json.toJSONString()));
    }

    /**
     * 查询钱包账户资产
     */
    public List<Account> getMainAccounts() throws Throwable {
        JSONObject json = new JSONObject();
        json.put("select",1);

        return JSONUtils.parseAccount(doPost(URL_MAIN_ACCOUNT, json.toJSONString()));
    }

    /**
     * 查询钱包账户资产
     *
     * @param symbol 交易对
     */
    public Account getMainAccount(String symbol) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("select",1);

        return JSONUtils.parseAccount(doPost(URL_MAIN_ACCOUNT, json.toJSONString()),symbol);
    }

    /**
     * 查询现货账户资产
     */
    public List<Account> getSpotAccounts() throws Throwable {
        JSONObject json = new JSONObject();
        json.put("select", 1);

        return JSONUtils.parseAccount(doPost(URL_SPOT_ACCOUNT, json.toJSONString()));
    }

    /**
     * 查询现货账户资产
     *
     * @param symbol 交易对
     */
    public Account getSpotAccount(String symbol) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("select", 1);

        return JSONUtils.parseAccount(doPost(URL_SPOT_ACCOUNT, json.toJSONString()),symbol);
    }

    /**
     * 获取充值地址
     *
     * @param symbol 充值币种
     */
    public String getDepositAddress(String symbol) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("coin_symbol", symbol);

        return JSONUtils.parseDepositAddress(doPost(URL_GET_DEPOSIT_ADDR, json.toJSONString()));
    }

    /**
     * 提现
     *
     * @param symbol    提现币种
     * @param amount    提现数量
     * @param address   提现地址
     */
    public long withdraw(String symbol, BigDecimal amount, String address) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("coin_symbol", symbol);
        json.put("amount", amount.doubleValue());
        json.put("addr", address);

        return JSONUtils.parseWithdrawId(doPost(URL_WITHDRAW, json.toJSONString()));
    }

    /**
     * 提现
     *
     * @param symbol    提现币种
     * @param amount    提现数量
     * @param address   提现地址
     * @param memo       memo
     */
    public long withdraw(String symbol, BigDecimal amount, String address, String memo ) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("coin_symbol", symbol);
        json.put("amount", amount.doubleValue());
        json.put("addr", address);
        json.put("memo", memo);

        return JSONUtils.parseWithdrawId(doPost(URL_WITHDRAW, json.toJSONString()));
    }

    /**
     * 获取充值记录
     *
     * @param status  充值状态
     * @param symbol  币种
     * @param page    页数
     * @param size    每页数量
     */
    public Pager<DepositEntry> getDepositEntries(
            DepositStatus status, String symbol, int page, int size) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("filter_type", status.getValue());
        if (!StringUtils.isBlank(symbol)) {
            json.put("coin_symbol", symbol);
        }
        json.put("page", page);
        json.put("size", size);

        return JSONUtils.parseDepositEntries(doPost(URL_DEPOSIT_ENTRIES, json.toJSONString()));
    }

    /**
     * 获取提现记录
     *
     * @param symbol  币种
     * @param page    页数
     * @param size    每页数量
     */
    public Pager<WithdrawEntry> getWithDrawEntries(String symbol, int page, int size) throws Throwable {
        JSONObject json = new JSONObject();
        if (!StringUtils.isBlank(symbol)) {
            json.put("coin_symbol", symbol);
        }
        json.put("page", page);
        json.put("size", size);

        return JSONUtils.parseWithDrawEntryEntries(doPost(URL_WITHDRAW_ENTRIES, json.toJSONString()));
    }
    /**
     * 获取指定 id 的提现记录
     *
     * @param id  提现id
     */
    public WithdrawEntry getTransferOutEntry(long id) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("id", id);

        return JSONUtils.parseTransferOutEntry(doPost(URL_WITHDRAW_ENTRY, json.toJSONString()));
    }

    /**
     * 获取全部币种的配置
     */
    public List<AssetInfo> getAssetInfos() throws Throwable {
        JSONObject json = new JSONObject();

        return JSONUtils.parseAssetInfos(doPost(URL_ASSET_INFO, json.toJSONString()));
    }

    /**
     * 获取指定币种的配置
     * @param symbol 币种
     */
    public AssetInfo getAssetInfo(String symbol) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("coin_symbol", symbol);

        return JSONUtils.parseAssetInfos(doPost(URL_ASSET_INFO, json.toJSONString())).get(0);
    }

    /**
     * 获取指定 query 查询的现货账单记录
     */
    public Pager<Bill> getBills(BillQuery query) throws Throwable {
        JSONObject json = new JSONObject();
        Integer page = Optional.ofNullable(query.getPage()).orElse(1);
        Integer size = Optional.ofNullable(query.getSize()).orElse(10);
        json.put("page", page);
        json.put("size", size);
        Optional.ofNullable(query.getType())
                .ifPresent(item -> json.put("type",item.getValue()));
        if (query.getStart()>0) {
            json.put("begin_time", query.getStart());
        }
        if (query.getEnd()>0) {
            json.put("end_time", query.getStart());
        }

        return JSONUtils.parseBills(doPost(URL_BILLS, json.toJSONString()));
    }

    /**
     * 获取指定订单的用户成交记录
     *
     * @param orderId 委托id
     */
    public List<Fill> getFills(String orderId) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("account_type", 0);
        json.put("id", orderId);

        return JSONUtils.parseFills(doPost(URL_TRADES, json.toJSONString()));
    }

    /**
     * 获取指定 query 的用户成交记录
     */
    public Pager<Fill> getFills(FillQuery query) throws Throwable {
        JSONObject json = new JSONObject();
        Integer page = Optional.ofNullable(query.getPage()).orElse(1);
        Integer size = Optional.ofNullable(query.getSize()).orElse(10);
        json.put("page", page);
        json.put("size", size);
        json.put("account_type", 0);

        Optional.ofNullable(query.getBase())
                .ifPresent(item -> json.put("coin_symbol", item));
        Optional.ofNullable(query.getQuote())
                .ifPresent(item -> json.put("currency_symbol",item));
        Optional.ofNullable(query.getSide())
                .ifPresent(item -> json.put("order_side",item.getValue()));

        return JSONUtils.parseFillsByPage(doPost(URL_TRADES_HISTORY, json.toJSONString()));
    }

    /**
     * 获取指定 orderId 的委托
     */
    public Order getOrder(String orderId) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("account_type", 0);
        json.put("id", orderId);
        return JSONUtils.parseOrder(doPost(URL_ORDER_DETAIL, json.toJSONString()));
    }

    /**
     * 获取符合指定 OrderQuery 条件的当前委托
     */
    public Pager<Order> getOpenOrders(OrderQuery query) throws Throwable {
        JSONObject json = new JSONObject();
        Integer page = Optional.ofNullable(query.getPage()).orElse(1);
        Integer size = Optional.ofNullable(query.getSize()).orElse(10);
        json.put("page", page);
        json.put("size", size);
        json.put("account_type", 0);

        Optional.ofNullable(query.getBase())
                .ifPresent(item -> json.put("coin_symbol", item));
        Optional.ofNullable(query.getQuote())
                .ifPresent(item -> json.put("currency_symbol",item));
        Optional.ofNullable(query.getSide())
                .ifPresent(item -> json.put("order_side",item.getValue()));

        return JSONUtils.parseOrdersByPage(doPost(URL_ORDER_PENDING_LIST, json.toJSONString()));
    }

    /**
     * 获取符合指定 OrderQuery 条件的历史委托
     */
    public Pager<Order> getClosedOrders(OrderQuery query) throws Throwable {
        JSONObject json = new JSONObject();
        Integer page = Optional.ofNullable(query.getPage()).orElse(1);
        Integer size = Optional.ofNullable(query.getSize()).orElse(10);
        json.put("page", page);
        json.put("size", size);
        json.put("account_type", 0);

        Optional.ofNullable(query.getBase())
                .ifPresent(item -> json.put("coin_symbol", item));
        Optional.ofNullable(query.getQuote())
                .ifPresent(item -> json.put("currency_symbol",item));
        Optional.ofNullable(query.getSide())
                .ifPresent(item -> json.put("order_side",item.getValue()));

        return JSONUtils.parseOrdersByPage(doPost(URL_ORDER_HISTORY_LIST, json.toJSONString()));
    }

    /**
     * 获取交易对
     */
    public List<CurrencyPair> getCurrencyPairs() throws Throwable {
        List<CurrencyPair> currencyPairs = JSONUtils.parseSpots(HttpUtils.doGet(rest(URL_SPOT_LIST), newQuery()));
        JSONObject limit = JSONUtils.parseTradeLimit(HttpUtils.doGet(rest(URL_TRADE_LIMIT),
                newQuery()));
        CurrencyPair.wrapper(currencyPairs, limit);
        return currencyPairs;

    }

    /**
     * 获取指定的 K 线
     *
     * @param symbol       交易对
     * @param timeInterval 周期
     * @param limit        数量限制
     */
    public List<Candlestick> getCandlestick(
            String symbol, TimeInterval timeInterval, int limit) throws Throwable {
        return JSONUtils.parseCandlesticks(HttpUtils.doGet(rest(URL_CANDLESTICK), newQuery()
                .put("period", timeInterval.toString())
                .put("pair", symbol)
                .put("size", String.valueOf(limit))));
    }

    /**
     * 获取指定 K 线, 最多 100 周期数据
     *
     * @param symbol       交易对名称
     * @param timeInterval 周期
     */
    public List<Candlestick> getCandlestick(
            String symbol, TimeInterval timeInterval) throws Throwable {
        return getCandlestick(symbol, timeInterval, 100);
    }

    /**
     * 获取深度
     *
     * @param symbol 交易对
     * @param limit  数量限制
     */
    public OrderBook getOrderBook(String symbol, int limit) throws Throwable {
        return JSONUtils.parseOrderBook(HttpUtils.doGet(rest(URL_ORDER_BOOK), newQuery()
                .put("pair", symbol)
                .put("size", String.valueOf(limit))));
    }

    /**
     * 获取深度
     *
     * @param symbol 交易对
     */
    public OrderBook getOrderBook(String symbol) throws Throwable {
        return JSONUtils.parseOrderBook(HttpUtils.doGet(rest(URL_ORDER_BOOK), newQuery()
                .put("pair", symbol)));
    }

    /**
     * 获取ticker
     *
     * @param symbol 交易对
     */
    public Ticker getTicker(String symbol) throws Throwable {
        return JSONUtils.parseTicker(HttpUtils.doGet(rest(URL_TICKER), newQuery()
                .put("pair", symbol)));
    }

    /**
     * 获取公开成交记录
     *
     * @param symbol 交易对
     * @param limit  数量限制
     */
    public List<Trade> getTrades(String symbol,int limit) throws Throwable {
        return JSONUtils.parseTrades(HttpUtils.doGet(rest(URL_DEALS), newQuery()
        .put("pair",symbol)
        .put("size",String.valueOf(limit))
        ));
    }

    /**
     * 获取公开成交记录
     *
     * @param symbol 交易对
     */
    public List<Trade> getTrades(String symbol) throws Throwable {
        return JSONUtils.parseTrades(HttpUtils.doGet(rest(URL_DEALS), newQuery()
                .put("pair", symbol)));
    }

    /**
     * 订阅 K 线数据
     *
     * @param symbol       交易对
     * @param timeInterval 周期
     */
    public void subscribeCandlestick(
            String symbol, TimeInterval timeInterval, Listener<List<Candlestick>> listener) {
        subscribe(new CandlestickSubscription(this, symbol, timeInterval, listener));
    }

    /**
     * 取消 K 线数据的订阅
     */
    public void unsubscribeCandlestick(String symbol, TimeInterval timeInterval) {
        unsubscribe(CandlestickSubscription.buildChannelName(symbol, timeInterval));
    }

    /**
     * 订阅指定的深度
     */
    public void subscribeOrderBook(String symbol, Listener<OrderBook> listener) {
        subscribe(new OrderBookSubscription(this, symbol, listener));
    }

    /**
     * 取消对指定的深度订阅
     */
    public void unsubscribeOrderBook(String symbol) {
        unsubscribe(OrderBookSubscription.buildChannelName(symbol));
    }

    /**
     * 订阅市场成交记录
     */
    public void subscribeTrade(String symbol, Listener<List<Trade>> listener) {
        subscribe(new TradeSubscription(this, symbol, listener));
    }

    /**
     * 取消订阅最新成交价
     */
    public void unsubscribeTrade(String symbol) {
        unsubscribe(TradeSubscription.buildChannelName(symbol));
    }

    /**
     * 订阅指定 Ticker 数据
     */
    public void subscribeTicker(String symbol, Listener<Ticker> listener) {
        subscribe(new TickerSubscription(this, symbol, listener));
    }

    /**
     * 取消订阅指定 Ticker 数据
     */
    public void unsubscribeTicker(String symbol) {
        unsubscribe(TickerSubscription.buildChannelName(symbol));
    }

    /**
     * 订阅资产账户变化信息
     */
    public void subscribeAccount(Listener<List<Account>> listener) {
        subscribe(new AccountSubscription(this, listener));
    }

    /**
     * 订阅与委托相关的信息
     */
    public void subscribeOrder(Listener<List<Order>> listener) {
        subscribe(new OrderSubscription(this, listener));
    }

    /**
     * 订阅用户数据解析成交明细
     */
    public void subscribeFill(Listener<List<Fill>> listener) {
        subscribe(new FillSubscription(this, listener));
    }

    /**
     * 取消全部对用户数据的订阅
     */
    public void unsubscribePrivateChannel() {
        unsubscribeAllPrivateSubscriptions();
    }

}
