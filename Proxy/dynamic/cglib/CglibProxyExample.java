package com.xlg.Proxy.dynamic.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @program: designpattern
 * @description:
 * @author: Mr.Wang
 * @create: 2020-09-21 20:49
 **/
public class CglibProxyExample implements MethodInterceptor {

    public Object getProxy(Class cls) {
        Enhancer enhancer = new Enhancer();
        // 设置 增强类型
        enhancer.setSuperclass(cls);
        //定义代理逻辑对象为当前对象，要求当前对象实现 MethodInterceptor 方法
        enhancer.setCallback(this);
        // 生成并返回代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("调用真实 对象前 ");
        // 真正调用
        Object result = methodProxy.invokeSuper(proxy, args);
        System.out.println("调用真实 对象之后");
        return result;
    }
}
