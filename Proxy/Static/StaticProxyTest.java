package com.xlg.Proxy.Static;

import org.junit.Test;

/**
 * @program: designpattern
 * @description:       静态代理
 * @author: Mr.Wang
 * @create: 2020-09-21 20:32
 **/
public class StaticProxyTest {
    @Test
    public void test() {
        UserServiceProxy proxy = ProxyFactory.getProxy();
        proxy.addUser();
        proxy.editUser();
    }
}
