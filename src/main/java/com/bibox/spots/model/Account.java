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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@ToString
public class Account {

    // 资产名称
    private String asset;

    // 可用资产
    private BigDecimal available;

    // 冻结资产
    private BigDecimal freeze;

    public static Map<String, Account> parseResult(JSONObject obj) {
        Map<String, Account> m = new HashMap<>();
        if (obj.containsKey("assets_list")) {
            JSONArray subs = obj.getJSONArray("assets_list");
            for (int i = 0; i < subs.size(); i++) {
                JSONObject s = subs.getJSONObject(i);
                Account a = new Account();
                a.setAsset(s.getString("coin_symbol"));
                a.setAvailable(s.getBigDecimal("balance"));
                a.setFreeze(s.getBigDecimal("freeze"));
                m.put(a.asset, a);
            }
        }
        return m;
    }

    public static List<Account> parseEvent(JSONObject obj) {
        ArrayList<Account> l = new ArrayList<>();
        if (obj.containsKey("normal")) {
            JSONObject spots = obj.getJSONObject("normal");
            spots.keySet().forEach(item -> {
                JSONObject spot = spots.getJSONObject(item);
                Account a = new Account();
                a.setAsset(item);
                a.setAvailable(spot.getBigDecimal("balance"));
                a.setFreeze(spot.getBigDecimal("freeze"));
                l.add(a);
            });
        }
        return l;
    }

}
