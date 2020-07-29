package com.plumcookingwine.irouter.api;

import com.plumcookingwine.irouter.annotation.bean.RouterBean;

import java.util.Map;

/**
 *  其实就是 路由组 Group 对应的 ---- 详细Path加载数据接口 ARouterPath
 *  例如：order分组 对应 ---- 有那些类需要加载（Order_MainActivity  Order_MainActivity2 ...）
 */
public interface IRouterPath {

    /**
     * 例如：order分组下有这些信息，personal分组下有这些信息
     *
     * @return key:"/order/Order_MainActivity"   或  "/personal/Personal_MainActivity"
     *         value: RouterBean==Order_MainActivity.class 或 RouterBean=Personal_MainActivity.class
     */
    Map<String, RouterBean> getPathMap();

}
