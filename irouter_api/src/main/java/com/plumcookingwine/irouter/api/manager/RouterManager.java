package com.plumcookingwine.irouter.api.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.LruCache;

import androidx.fragment.app.Fragment;

import com.plumcookingwine.irouter.annotation.bean.RouterBean;
import com.plumcookingwine.irouter.api.IRouterGroup;
import com.plumcookingwine.irouter.api.IRouterPath;

/**
 * 路由管理器，辅助完成交互通信
 * 详细流程：
 * 1.拼接 找 ARouter$$Group$$personal
 * 2.进入 ARouter$$Group$$personal 调用函数返回groupMap
 * 3.执行 groupMap.get(group)  group == personal
 * 4.查找  ARouter$$Path$$personal.class
 * 5.进入  ARouter$$Path$$personal.class 执行函数
 * 6.执行 pathMap.get(path) path = "/personal/Personal_Main2Activity"
 * 7.拿到 RouterBean（Personal_MainActivity.class）
 * 8.startActivity（new Intent(this, Personal_MainActivity.class)）
 */
public class RouterManager {

    //1.拿到ARouter$$Group$$personal  根据组名 拿到 ARouter$$Path$$personal
    //2.操作路径，通过路径 拿到  Personal_MainActivity.class，就可以实现跳转了
    private String group; // 路由的组名 app，order，personal ...
    private String path;  // 路由的路径  例如：/order/Order_MainActivity

    private static Context mContext;

    // 单例模式
    private volatile static RouterManager instance;

    public static RouterManager getInstance() {
        if (instance == null) {
            synchronized (RouterManager.class) {
                if (instance == null) {
                    instance = new RouterManager();
                }
            }
        }
        return instance;
    }

    public static void init(Application application) {
        mContext = application.getApplicationContext();
    }

    // 性能  LRU缓存
    private LruCache<String, IRouterGroup> groupLruCache;
    private LruCache<String, IRouterPath> pathLruCache;

    // 为了拼接，例如:ARouter$$Group$$personal
    private final static String FILE_GROUP_NAME = "IRouter$$Group$$";

    private RouterManager() {
        groupLruCache = new LruCache<>(100);
        pathLruCache = new LruCache<>(100);
    }

    /**
     * @param path 例如：/order/Order_MainActivity
     * @return this
     */
    public BundleManager build(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new IllegalArgumentException("找不到路径：：" + path);
        }

        if (path.lastIndexOf("/") == 0) { // 只写了一个 /
            throw new IllegalArgumentException("找不到路径：：" + path);
        }

        String finalGroup = path.substring(1, path.indexOf("/", 1)); // finalGroup = order
        if (TextUtils.isEmpty(finalGroup)) {
            throw new IllegalArgumentException("找不到路径：：" + path);
        }

        this.path = path;
        this.group = finalGroup;

        return new BundleManager();
    }

    // 真正完成跳转
    Object navigation(Context context, int requestCode, BundleManager bundleManager) {
        String groupClassName = "com.plumcookingwine.generated." + FILE_GROUP_NAME + group;

        try {
            // 读取路由组Group类文件
            IRouterGroup loadGroup = groupLruCache.get(group);
            // 读取路由组Group类文件
            if (null == loadGroup) { // 缓存里面没有东东
                // 加载APT路由组Group类文件 例如：ARouter$$Group$$order
                Class<?> aClass = Class.forName(groupClassName);
                // 初始化类文件
                loadGroup = (IRouterGroup) aClass.newInstance();
                // 保存到缓存
                groupLruCache.put(group, loadGroup);
            }

            if (loadGroup.getGroupMap().isEmpty()) {
                throw new RuntimeException("路由表Group报废了...");
            }

            // 读取路由Path类文件
            IRouterPath loadPath = pathLruCache.get(path);
            if (null == loadPath) {
                Class<? extends IRouterPath> clazz = loadGroup.getGroupMap().get(group);
                if (clazz != null) {
                    loadPath = clazz.newInstance();
                }
                pathLruCache.put(path, loadPath);
            }

            if (loadPath != null) {
                if (loadPath.getPathMap().isEmpty()) {
                    throw new RuntimeException("路由表Path报废了...");
                }

                RouterBean routerBean = loadPath.getPathMap().get(path);
                if (routerBean != null) {

                    if (context == null) {
                        if (mContext == null) {
                            throw new NullPointerException("请在application中初始化");
                        }
                        context = mContext;
                    }

                    switch (routerBean.getTypeEnum()) {
                        case ACTIVITY:
                            Intent intent = new Intent(context, routerBean.getMyClass()); // 例如：getClazz == Order_MainActivity.class
                            intent.putExtras(bundleManager.getBundle());
                            if (!(context instanceof Activity)) {
                                // 独栈模式运行
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                return null;
                            }
                            if (requestCode != -1) {
                                // 携带返回值跳转
                                ((Activity) context).startActivityForResult(intent, requestCode);
                            } else {
                                context.startActivity(intent);
                            }
                            break;
                        case FRAGMENT:
                            Fragment fragment = (Fragment) routerBean.getMyClass().newInstance();
                            fragment.setArguments(bundleManager.getBundle());
                            return fragment;
                        default:
                            break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
