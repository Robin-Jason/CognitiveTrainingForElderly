package org.example.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.server.dataobject.User;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    void insertUser(@Param("user") User user);
}