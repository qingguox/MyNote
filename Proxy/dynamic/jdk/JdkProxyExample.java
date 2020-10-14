package com.xlg.Proxy.dynamic.jdk;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @program: designpattern
 * @description: 代理类
 * @author: Mr.Wang
 * @create: 2020-09-21 20:37
 **/
public class JdkProxyExample implements InvocationHandler {
    // 真实 对象
    private Object target = null;

    public Object bind(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), (InvocationHandler) this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(" 进入代理逻辑方法");
        System.out.println("调用 真实对象之前的服务 ，，");
        Object obj = method.invoke(target, args);
        System.out.println("调用 真实对象之后的服务。。");
        return obj;
    }
}
