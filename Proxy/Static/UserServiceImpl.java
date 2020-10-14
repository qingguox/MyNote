package com.xlg.Proxy.Static;

/**
 * @program: designpattern
 * @description: k
 * @author: Mr.Wang
 * @create: 2020-09-21 20:24
 **/
public class UserServiceImpl  implements UserService{
    @Override
    public void addUser() {
        System.out.println("添加一个 新用户");
    }

    @Override
    public void editUser() {
        System.out.println("编辑用户");
    }
}
