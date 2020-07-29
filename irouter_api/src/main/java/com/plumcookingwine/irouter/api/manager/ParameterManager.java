package com.plumcookingwine.irouter.api.manager;

import android.app.Activity;
import android.util.LruCache;

import androidx.fragment.app.Fragment;

import com.plumcookingwine.irouter.api.ParameterGet;

/**
 * 参数的 加载管理器
 * 这是用于接收参数的
 * <p>
 * 1.查找  Order_MainActivity$$Parameter
 * 2.使用 Order_MainActivity$$Parameter
 */
public class ParameterManager {

    private static ParameterManager instance;

    public static ParameterManager getInstance() {
        if (instance == null) {
            synchronized (ParameterManager.class) {
                if (instance == null) {
                    instance = new ParameterManager();
                }
            }
        }
        return instance;
    }

    // 懒加载  用到了 就加载，    阿里的路由 全局加载（不管你用没有用到，反正全部加载）
    // LRU缓存 key=类名      value=参数加载接口
    private LruCache<String, ParameterGet> cache;

    private ParameterManager() {
        cache = new LruCache<>(100);
    }

    private static final String FILE_SUFFIX_NAME = "$$Parameter"; // 最终拼接：OrderActivity$$Parameter

    // activity 反向注入
    public void loadParameter(Activity activity) {
        load(activity);
    }

    // fragment反向注入
    public void loadParameter(Fragment fragment) {
        load(fragment);
    }

    private void load(Object object) {
        String className = object.getClass().getName(); // className == Personal_MainActivity

        ParameterGet parameterLoad = cache.get(className); // key className
        if (null == parameterLoad) { // 缓存里面没东东
            // 拼接 如：Order_MainActivity + $$Parameter
            // 类加载Order_MainActivity + $$Parameter
            try {
                Class<?> aClass = Class.forName(className + FILE_SUFFIX_NAME);
                // 用接口parameterLoad = 接口的实现类Personal_MainActivity
                parameterLoad = (ParameterGet) aClass.newInstance();
                cache.put(className, parameterLoad); // 保存到缓存
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(parameterLoad!= null) {
            parameterLoad.getParameter(object); // 最终的执行
        }

    }
}
