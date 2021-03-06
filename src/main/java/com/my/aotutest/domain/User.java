package com.my.aotutest.domain;

import java.io.Serializable;

/**
 * @author lijx
 * @date 2018/11/6 - 20:24
 */
public class User implements Serializable{
    private Integer id;
    private String username;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
