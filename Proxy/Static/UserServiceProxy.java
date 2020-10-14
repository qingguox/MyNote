package com.xlg.Proxy.Static;

import javax.sound.midi.Soundbank;

/**
 * @program: designpattern
 * @description: 代理类
 * @author: Mr.Wang
 * @create: 2020-09-21 20:24
 **/
public class UserServiceProxy  implements UserService{
    private UserService userService;
    public UserServiceProxy(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void addUser() {
        System.out.println("代理类进入，addUser");
        userService.addUser();
        System.out.println("代理类出去 addUser");
    }
    @Override
    public void editUser() {
        System.out.println("代理类进入，editUser");
        userService.editUser();
        System.out.println("代理类出去 editUser");
    }
}
