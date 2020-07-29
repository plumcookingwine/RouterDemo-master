package com.plumcookingwine.irouter.api.manager;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 跳转时 Intent ，用于参数的传递
 */
public class BundleManager {

    // 携带的值，保存到这里  Intent 传输
    private Bundle bundle = new Bundle();

    public Bundle getBundle() {
        return this.bundle;
    }

    // 对外界提供，可以携带参数的方法
    public BundleManager withString(@NonNull String key, String value) {
        bundle.putString(key, value);
        return this;
    }

    public BundleManager withBoolean(@NonNull String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public BundleManager withInt(@NonNull String key, int value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundleManager withBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    /**
     * 一半提供给fragment使用，返回fragment的实例
     * 如果activity进行跳转，那么返回null，并使用NEW_TASK的模式进行跳转
     *
     * @return fragment | null
     */
    public Object navigation() {
        return navigation(null);
    }

    /**
     * fragment获取则用不到context
     *
     * @param context context
     * @return null
     */
    public Object navigation(Context context) {
        //根据源码可知requestCode默认为-1
        return navigation(context, -1);
    }

    /**
     * 直接完成跳转
     *
     * @param context     Cotnext
     * @param requestCode 请求吗
     * @return null or fragment
     */
    //
    public Object navigation(Context context, int requestCode) {
        // 单一原则
        // 把自己所有行为 都交给了  路由管理器
        return RouterManager.getInstance().navigation(context, requestCode, this);
    }
}
