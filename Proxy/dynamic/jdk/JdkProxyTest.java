package com.xlg.Proxy.dynamic.jdk;

/**
 * @program: designpattern
 * @description:
 * @author: Mr.Wang
 * @create: 2020-09-21 20:42
 **/
public class JdkProxyTest {

    public static void main(String[] args) {

        JdkProxyExample example = new JdkProxyExample();
        HelloWord proxy = (HelloWord) example.bind(new HelloWordImpl());
        proxy.sayHelloWorld();
    }
}
