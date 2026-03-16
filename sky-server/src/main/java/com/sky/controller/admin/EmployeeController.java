package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate; // 必须引入
import javax.servlet.http.HttpServletRequest; // 必须引入

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId()); //存入员工ID作为员工识别的信号
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {

        //这里来实现一下redis的token黑名单机制
        // 1. 从请求头获取当前的 Token
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        if (token != null) {
            // 2. 将 Token 存入 Redis，Key 可以加个前缀方便管理
            // 这里的过期时间建议设置为 JWT 的剩余有效时间，简单处理可以直接设为 jwtProperties 的 TTL
            String key = "BLACK_LIST:" + token;
            stringRedisTemplate.opsForValue().set(key, "1", jwtProperties.getAdminTtl(), TimeUnit.MILLISECONDS);
            log.info("用户退出，Token 已加入黑名单：{}", token);
        }

        return Result.success();
    }

    /**
     * 新增员工
     */
    @PostMapping
    public Result addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工，员工数据：{}", employeeDTO);
        employeeService.add(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询员工
     */
    @GetMapping("/page")
    public Result page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("分页查询员工信息：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.page(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用员工账号
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,@RequestParam Long id) {
        log.info("启用禁用员工账号：{}", id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据id查询员工信息
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        log.info("根据id查询员工信息：{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     */
    @PutMapping
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息：{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

}
