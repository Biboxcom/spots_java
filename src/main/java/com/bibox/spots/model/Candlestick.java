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

@Getter
@Setter(value = AccessLevel.PROTECTED)
@ToString
public class Candlestick {

    // 时间戳
    private long time;

    // 开盘价
    private BigDecimal open;

    // 最高价
    private BigDecimal high;

    // 最低价
    private BigDecimal low;

    // 收盘价
    private BigDecimal close;

    // 成交量
    private BigDecimal volume;

    public static Candlestick parseResult(JSONObject obj) {
        Candlestick a = new Candlestick();
        a.setTime(obj.getLong("time"));
        a.setOpen(obj.getBigDecimal("open"));
        a.setClose(obj.getBigDecimal("close"));
        a.setHigh(obj.getBigDecimal("high"));
        a.setLow(obj.getBigDecimal("low"));
        a.setVolume(obj.getBigDecimal("vol"));
        return a;
    }

    public static Candlestick parseResult(JSONArray item) {
        Candlestick a = new Candlestick();
        a.setTime(item.getLong(0));
        a.setOpen(item.getBigDecimal(1));
        a.setHigh(item.getBigDecimal(2));
        a.setLow(item.getBigDecimal(3));
        a.setClose(item.getBigDecimal(4));
        a.setVolume(item.getBigDecimal(5));
        return a;
    }

}
