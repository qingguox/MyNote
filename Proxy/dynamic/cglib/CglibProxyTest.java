package com.xlg.Proxy.dynamic.cglib;

/**
 * @program: designpattern
 * @description: kk
 * @author: Mr.Wang
 * @create: 2020-09-21 21:04
 **/
public class CglibProxyTest {

    public static void main(String[] args) {
        CglibProxyExample example = new CglibProxyExample();
        CglibBase proxy = (CglibBase) example.getProxy(CglibBase.class);
        proxy.sayHello("张三 ");
    }
}
