package org.example.server.service;

import org.example.server.dataobject.User;
import org.example.server.mapper.UserMapper;
import org.example.server.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserByName(String name) {
        return userMapper.findByUsername(name);
    }

    public void registerUser(User user) {
        // 密码加密
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        // 调用mapper保存用户
        userMapper.insertUser(user);
    }
}