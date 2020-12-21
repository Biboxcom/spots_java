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

package com.bibox.spots.model;

import com.alibaba.fastjson.JSONObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@ToString
public class CurrencyPair {

    // 交易对 id
    private long id;

    // 交易对名称
    private String symbol;

    // 最小下单金额
    private BigDecimal minOrderValue;

    // 最小下单数量
    private BigDecimal minOrderSize;

    public static CurrencyPair parseResult(JSONObject obj) {
        CurrencyPair a = new CurrencyPair();
        a.setId(obj.getLong("id"));
        a.setSymbol(obj.getString("pair"));
        return a;
    }

    public static void wrapper(List<CurrencyPair> currencyPairs, JSONObject limit) {
        JSONObject amountLimit = limit.getJSONObject("min_trade_amount");
        JSONObject moneyLimit = limit.getJSONObject("min_trade_money");
        BigDecimal defaultAmountLimit = amountLimit.getBigDecimal("default");
        currencyPairs.forEach(item -> {
            String[] sp = item.getSymbol().split("_");
            String base = sp[1];
            item.setMinOrderSize(defaultAmountLimit);
            if (moneyLimit.containsKey(base)) {
                item.setMinOrderValue(moneyLimit.getBigDecimal(base));
            }
        });
    }

}
