package com.xlg.Proxy.Static;

/**
 * @program: designpattern
 * @description: 代理工厂
 * @author: Mr.Wang
 * @create: 2020-09-21 20:24
 **/
public class ProxyFactory {
    public static UserServiceProxy getProxy() {
        return new UserServiceProxy(new UserServiceImpl());
    }
}
