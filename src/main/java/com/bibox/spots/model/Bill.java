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
import com.bibox.spots.model.enums.BillType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@ToString
public class Bill {

    // 币种
    private String symbol;

    // 账单类型
    private BillType type;

    // 资产改变
    private BigDecimal change;

    // 资产改变后的余额
    private BigDecimal result;

    // 生成账单时间
    private long time;

    public static Bill parseResult(JSONObject obj) {
        Bill a = new Bill();
        a.setSymbol(obj.getString("symbol"));
        a.setTime(obj.getTimestamp("createdAt").getTime());
        a.setChange(obj.getBigDecimal("change_amount"));
        a.setResult(obj.getBigDecimal("result_amount"));
        a.setType(BillType.fromInteger(obj.getInteger("bill_type")));
        return a;
    }

}
