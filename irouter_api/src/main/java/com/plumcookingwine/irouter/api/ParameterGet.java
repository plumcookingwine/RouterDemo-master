package com.plumcookingwine.irouter.api;

// 传递参数 的 标准  【组件 和 组件 直接通讯的时候 参数的高层】
public interface ParameterGet {

    /**
     * 目标对象.属性名 = getIntent().属性类型... 完成赋值操作
     * @param targetParameter 目标对象：例如：MainActivity 中的那些属性
     */
    void getParameter(Object targetParameter);

}
