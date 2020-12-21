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
import com.bibox.spots.model.enums.WithdrawStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@ToString
public class WithdrawEntry {

    // 币种
    private String symbol;

    // 提现地址
    private String address;

    // 提现数量
    private BigDecimal amount;

    // 手续费
    private BigDecimal fee;

    // 提现时间
    private long time;

    // 提现状态
    private WithdrawStatus status;

    // memo
    private String memo;

    public static WithdrawEntry parseResult(JSONObject obj) {
        WithdrawEntry a = new WithdrawEntry();
        a.setSymbol(obj.getString("coin_symbol"));
        a.setAddress(obj.getString("to_address"));
        a.setAmount(obj.getBigDecimal("amount"));
        a.setFee(obj.getBigDecimal("fee"));
        a.setTime(obj.getLong("createdAt"));
        a.setStatus(WithdrawStatus.fromInteger(obj.getInteger("status")));
        a.setMemo(obj.getString("memo"));
        return a;
    }

}
