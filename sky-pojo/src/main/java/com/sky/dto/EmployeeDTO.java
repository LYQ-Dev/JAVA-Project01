package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
//用来接收前端员工数据
@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private String sex;

    private String idNumber;

}
