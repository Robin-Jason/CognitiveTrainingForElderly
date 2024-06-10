package org.example.server.controller;

import org.example.server.dataobject.CommonResult;
import org.example.server.dataobject.Login.TokenResponse;
import org.example.server.dataobject.User;
import org.example.server.service.UserService;
import org.example.server.utils.JwtTokenUtil;
import org.example.server.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // 用户注册
    @PostMapping("/register")
    public CommonResult<?> register(@RequestBody User user) {
        // 确保用户填写了邮箱
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return CommonResult.error(400, "邮箱不能为空");
        }

        userService.registerUser(user);
        return CommonResult.success("注册成功");
    }

    // 用户登录验证
    @PostMapping("/login")
public CommonResult<?> login(@RequestBody Map<String, String> loginData) {
    String username = loginData.get("username");
    String password = loginData.get("password");

    if (username == null || password == null) {
        return CommonResult.error(400, "用户名或密码不能为空");
    }

    // 获取用户信息
    User user = userService.findUserByName(username);
    if (user == null) {
        return CommonResult.error(401, "用户名或密码错误");
    }

    // 验证密码
    String encryptedPassword = MD5Util.encrypt(password);
    if (user.getPassword() == null || !user.getPassword().equals(encryptedPassword)) {
        return CommonResult.error(401, "用户名或密码错误");
    }

    // 生成访问令牌和刷新令牌
    String accessToken = jwtTokenUtil.generateAccessToken(username);
    String refreshToken = jwtTokenUtil.generateRefreshToken(username);
    TokenResponse token_resp = new TokenResponse(accessToken, refreshToken);

    return CommonResult.success(token_resp);
}

    // 获取用户信息
    @GetMapping("/getInfo")
    public CommonResult<?> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return CommonResult.success(username);
    }
}