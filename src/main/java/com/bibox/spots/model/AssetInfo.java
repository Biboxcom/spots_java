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

@Getter
@Setter(value = AccessLevel.PROTECTED)
@ToString
public class AssetInfo {

    // 币种
    private String symbol;

    // 币种原值精度
    private int precision;

    // 是否可以充值
    private boolean depositEnabled;

    // 是否可以提现
    private boolean withdrawEnabled;

    // 提现手续费
    private BigDecimal withdrawalFee;

    // 最小提现数量
    private BigDecimal minWithdraw;

    public static AssetInfo parseResult(JSONObject obj) {
        AssetInfo a = new AssetInfo();
        a.setSymbol(obj.getString("coin_symbol"));
        a.setPrecision(obj.getInteger("original_decimals"));
        a.setDepositEnabled(obj.getBoolean("enable_deposit"));
        a.setWithdrawEnabled(obj.getBoolean("enable_withdraw"));
        a.setWithdrawalFee(obj.getBigDecimal("withdraw_fee"));
        a.setMinWithdraw(obj.getBigDecimal("withdraw_min"));
        return a;
    }

}
