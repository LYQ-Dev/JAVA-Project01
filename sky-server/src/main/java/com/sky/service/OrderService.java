package com.sky.service;

import com.sky.dto.*;
import com.sky.vo.OrderReportVO;
import com.sky.vo.OrderSubmitVO;

import java.time.LocalDate;

public interface OrderService {

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);


    /**
     * 用户催单
     * @param id
     */
    void reminder(Long id);
}