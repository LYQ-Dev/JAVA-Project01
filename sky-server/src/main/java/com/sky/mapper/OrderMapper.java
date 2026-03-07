package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据状态和下单时间查询订单
     * @param status
     * @param orderTime
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrdertimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 更新订单信息（支持状态、取消原因、取消时间等字段更新）
     * @param order
     */
    @Update("UPDATE orders " +
            "SET status = #{status}, " +
            "cancel_reason = #{cancelReason}, " +
            "cancel_time = #{cancelTime}, " +
            "update_time = #{updateTime} " +
            "WHERE id = #{id}")
    void update(Orders order);

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

}