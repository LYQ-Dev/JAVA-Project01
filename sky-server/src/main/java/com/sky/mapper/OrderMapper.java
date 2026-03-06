package com.sky.mapper;

import com.sky.entity.Orders;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);
}