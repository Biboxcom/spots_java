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

import com.bibox.spots.model.enums.OrderStatus;
import com.bibox.spots.model.enums.OrderType;
import com.bibox.spots.model.enums.TradeSide;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public abstract class Order {

    // 委托id
    private String orderId;

    // client order id
    private String clientOid;

    // 成交价格
    private BigDecimal dealPrice;

    // 委托数量
    @Setter
    private BigDecimal quantity;

    // 交易方向
    @Setter
    private TradeSide side;

    // 交易对
    @Setter
    private String symbol;

    // 成交数量
    private BigDecimal executedQty;

    // 委托状态
    private OrderStatus status;

    // 委托时间
    private long createTime;

    // 获取委托类型
    public OrderType getOrderType() {
        return null;
    }

}
