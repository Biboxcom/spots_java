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
import com.bibox.spots.model.LimitOrder;
import com.bibox.spots.model.MarketOrder;
import com.bibox.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

@Slf4j
abstract class BiboxSpotsClientBase {

    public String restHost = "https://api.bibox.com";
    public String urlWss = "wss://npush.bibox360.com";

    public void setRestHost(String restHost) {
        this.restHost = restHost;
    }

    public void setUrlWss(String urlWss) {
        this.urlWss = urlWss;
    }

    String rest(String uri) {
        return restHost + uri;
    }

    // URL for Private Data
    public static final String URL_MAIN_ACCOUNT = "/v3.1/transfer/mainAssets";
    public static final String URL_SPOT_ACCOUNT = "/v3.1/spot/account/assets";
    public static final String URL_GET_DEPOSIT_ADDR = "/v3.1/transfer/transferIn";
    public static final String URL_WITHDRAW = "/v3.1/transfer/transferOut";
    public static final String URL_DEPOSIT_ENTRIES = "/v3.1/transfer/transferInList";
    public static final String URL_WITHDRAW_ENTRIES = "/v3.1/transfer/transferOutList";
    public static final String URL_WITHDRAW_ENTRY = "/v3.1/transfer/withdrawInfo";
    public static final String URL_ASSET_INFO = "/v3.1/transfer/coinConfig";
    public static final String URL_TRANSFER_SPOT = "/v3/assets/transfer/spot";
    public static final String URL_TRANSFER_CREDIT = "/v3.1/credit/transferAssets/base2credit";
    public static final String URL_ORDER_PENDING_LIST = "/v3.1/orderpending/orderPendingList";
    public static final String URL_ORDER_HISTORY_LIST = "/v3.1/orderpending/pendingHistoryList";
    public static final String URL_ORDER_DETAIL = "/v3.1/orderpending/order";
    public static final String URL_PLACE_ORDER = "/v3.1/orderpending/trade";
    public static final String URL_CANCEL_ORDER = "/v3.1/orderpending/cancelTrade";
    public static final String URL_TRADES = "/v3.1/orderpending/orderDetail";
    public static final String URL_TRADES_HISTORY = "/v3.1/orderpending/orderHistoryList";
    public static final String URL_BILLS = "/v3.1/spot/account/bills";

    // URL for Market Data
    public static final String URL_SPOT_LIST = "/v3/mdata/pairList";
    public static final String URL_CANDLESTICK = "/v3/mdata/kline";
    public static final String URL_ORDER_BOOK = "/v3/mdata/depth";
    public static final String URL_DEALS = "/v3/mdata/deals";
    public static final String URL_TICKER = "/v3/mdata/ticker";
    public static final String URL_TRADE_LIMIT = "/v3.1/orderpending/tradeLimit";

    // Headers
    public static final String HEADER_SIGN = "bibox-api-sign";
    public static final String HEADER_API_KEY = "bibox-api-key";
    public static final String HEADER_TIMESTAMP = "bibox-timestamp";

    private final String apiKey;
    private final String secretKey;
    private final Map<String, Subscription> subscriptions = new HashMap<>();
    private final Map<String, PrivateSubscription> privateSubscriptions = new HashMap<>();

    private Handler messageHandler;
    private Handler subscriptionHandler;
    private HttpUtils.WebSocket webSocket;
    private Runnable mScheduledReconnect;
    private Runnable mScheduledPing;
    private boolean mInitialized;

    /**
     * Initialize an instance
     */
    public BiboxSpotsClientBase() {
        apiKey = "";
        secretKey = "";
    }

    public BiboxSpotsClientBase(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    String getApiKey() {
        return apiKey;
    }

    Handler getMessageHandler() {
        return messageHandler;
    }

    Handler getSubscriptionHandler() {
        return subscriptionHandler;
    }

    protected void subscribe(Subscription sub) {
        ensureInitialized();
        messageHandler.post(() -> {
            String channel = sub.getChannel();
            if (sub instanceof PrivateSubscription) {
                if (!privateSubscriptions.containsKey(channel)) {
                    privateSubscriptions.put(channel, (PrivateSubscription) sub);
                    sub.start();
                }
            } else {
                if (!subscriptions.containsKey(channel)) {
                    subscriptions.put(channel, sub);
                    sub.start();
                }
            }
        });
    }

    public void unsubscribe(String channel) {
        messageHandler.post(() -> {
            Subscription sub = subscriptions.remove(channel);
            if (sub != null) {
                sub.stop();
            }
        });
    }

    public void unsubscribeAllPrivateSubscriptions() {
        messageHandler.post(() -> {
            sendUnsubscribeMessage(PrivateSubscription.CHANNEL_PREFIX);
            privateSubscriptions.clear();
        });
    }

    private void sendPing() {
        JSONObject json = new JSONObject();
        json.put("ping", System.currentTimeMillis()/1000);
        sendMessage(json.toJSONString());
    }

    private void sendPong(long id) {
        JSONObject json = new JSONObject();
        json.put("pong", id);
        sendMessage(json.toJSONString());
    }

    void sendSubscribeMessage(Subscription sub) {
        sendMessage(sub.toString());
    }

    void sendUnsubscribeMessage(Subscription sub) {
        sendUnsubscribeMessage(sub.getChannel());
    }

    void sendUnsubscribeMessage(String channel) {
        JSONObject json = new JSONObject();
        json.put("unsub", channel);
        sendMessage(json.toJSONString());
    }

    void sendMessage(String text) {
        connectWebSocket();
        webSocket.send(text);
    }

    protected void ensureInitialized() {
        if (!mInitialized) {
            init();
        }
    }

    private synchronized void init() {
        if (mInitialized) {
            return;
        }

        if (Looper.myQueue() == null) {
            Future<RuntimeException> future = new Future<>();
            Executors.newSingleThreadExecutor().execute(() -> {
                Thread.currentThread().setName("bibox-websocket-executor");
                Looper.prepare();
                messageHandler = new Handler();
                messageHandler.post(() -> future.set(null));
                Looper.loop();
            });
            future.get();
        } else {
            messageHandler = new Handler();
        }

        Future<RuntimeException> future = new Future<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            Thread.currentThread().setName("bibox-subscription-executor");
            Looper.prepare();
            subscriptionHandler = new Handler();
            subscriptionHandler.post(() -> future.set(null));
            Looper.loop();
        });

        future.get();
        mInitialized = true;
    }

    private void connectWebSocket() {
        if (webSocket == null) {
            log.info("Connecting to '{}'", urlWss);

            webSocket = HttpUtils.createWebSocket(urlWss,
                    new HttpUtils.WebSocketListener() {
                        @Override
                        public void onOpen(HttpUtils.WebSocket socket) {
                            messageHandler.post(() -> onWebSocketOpen());

                        }

                        @Override
                        public void onMessage(HttpUtils.WebSocket socket, byte[] bytes) {
                            String text = UncompressUtils.decodeBytes(bytes);
                            if (text == null) {
                                return;
                            }
                            messageHandler.post(() -> onWebSocketMessage(text));
                        }
                        @Override
                        public void onFailure(HttpUtils.WebSocket socket, Throwable error) {
                            onWebSocketFailure(error);
                        }
                    });
        }
    }

    private void onWebSocketOpen() {
        log.info("Connected to '{}'", urlWss);
        for (Subscription e : subscriptions.values()) {
            e.start();
        }
        for (Subscription e : privateSubscriptions.values()) {
            e.start();
        }
        schedulePing();
    }

    private void onWebSocketMessage(String text) {
        if (StringUtils.isBlank(text)) {
            return;
        }
        scheduleReconnect();

        if (!isArrMsg(text)) {
            // Ping
            JSONObject json = JSON.parseObject(text);
            long pingId = JSONUtils.parsePing(json);
            if (pingId >= 0) {
                log.info("ping [{}]", pingId);
                sendPong(pingId);
                return;
            }
            // sub msg
            String channel = json.getString("topic");
            if (StringUtils.isEmpty(channel)) {
                return;
            }
            if (json.containsKey("error")) {
                log.error(text);
                return;
            }
            if (PrivateSubscription.CHANNEL_PREFIX.equals(channel)) {
                JSONObject data = json.getJSONObject("d");
                for (PrivateSubscription sub : privateSubscriptions.values()) {
                    if (sub.belong(data)) {
                        sub.onMessage(data);
                        return;
                    }
                }
                return;
            }

            Subscription sub = subscriptions.get(channel);
            if (sub != null) {
                sub.onMessage(json);
            }
        }
    }

    private void onWebSocketFailure(Throwable error) {
        log.warn("", error);

        if (error instanceof IOException) {
            messageHandler.postDelayed(() -> {
                log.error("Cannot connect to '{}'", urlWss, error);
                reconnectWebSocket();
            }, 2_000);
        }
    }

    private void schedulePing() {
        // 删除计划任务
        if (mScheduledPing != null) {
            messageHandler.removeCallbacks(mScheduledPing);
            mScheduledPing = null;
        }

        // 为稍后重连创建计划任务
        messageHandler.postDelayed(mScheduledPing = () -> {
            mScheduledPing = null;
            sendPing();
            schedulePing();
        }, 20_000);
    }

    private void scheduleReconnect() {
        // 删除计划任务
        if (mScheduledReconnect != null) {
            messageHandler.removeCallbacks(mScheduledReconnect);
            mScheduledReconnect = null;
        }

        // 为稍后重连创建计划任务
        messageHandler.postDelayed(mScheduledReconnect = () -> {
            mScheduledReconnect = null;
            try {
                sendPing();
            }catch (Exception e) {
                reconnectWebSocket();
            }
        }, 30_000);
    }

    private void reconnectWebSocket() {
        if (webSocket != null) {
            try {
                webSocket.close();
            }catch (Exception e){
                // 关闭失败
                log.warn(e.getMessage());
            }
            connectWebSocket();
        }
    }

    protected String buildSignature() {
        String oriStr = String.format("{\"apikey\":\"%s\",\"sub\":\"%s\"}",
                apiKey, PrivateSubscription.CHANNEL_PREFIX);
        return Hex.encodeHexString(MacUtils.buildMAC(
                oriStr, "HmacMD5", this.secretKey)).toLowerCase();
    }

    private boolean isArrMsg(String text) {
        return text.startsWith("[");
    }

    protected HttpHeader buildHeader(String text) {
        long timestamp = System.currentTimeMillis();
        String sign = buildSignature(timestamp + text);
        return new HttpHeader()
                .put(HEADER_TIMESTAMP, String.valueOf(timestamp))
                .put(HEADER_API_KEY, apiKey)
                .put(HEADER_SIGN, sign);
    }

    protected HttpQuery newQuery() {
        return new HttpQuery();
    }

    protected String buildSignature(String text) {
        return Hex.encodeHexString(
                MacUtils.buildMAC(text, "HmacMD5", secretKey)).toLowerCase();
    }

    /**
     * @param order 限价
     */
    protected String placeOrder(LimitOrder order) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("account_type", 0);
        json.put("pair", order.getSymbol());
        json.put("amount", order.getQuantity().doubleValue());
        json.put("price", order.getPrice().doubleValue());
        json.put("order_side", order.getSide().getValue());
        json.put("order_type", 2);

        return JSONUtils.parseOrderId(doPost(URL_PLACE_ORDER, json.toJSONString()));
    }

    /**
     * @param order 市价
     */
    protected String placeOrder(MarketOrder order) throws Throwable {
        JSONObject json = new JSONObject();
        json.put("account_type", 0);
        json.put("pair", order.getSymbol());
        json.put("amount", order.getQuantity().doubleValue());
        json.put("order_side", order.getSide().getValue());
        json.put("order_type", 1);

        return JSONUtils.parseOrderId(doPost(URL_PLACE_ORDER, json.toJSONString()));
    }

    protected String doPost(String url, String bodyString) throws Throwable {
        return HttpUtils.doPost(rest(url), buildHeader(bodyString), HttpUtils.MT_JSON, bodyString);
    }


}
