package com.plumcookingwine.irouter.compiler.utils;

public interface ProcessorConfig {

    // @IRouter注解 的 包名 + 类名
    String IROUTER_PACKAGE = "com.plumcookingwine.irouter.annotation.IRouter";

    // 接收参数的TAG标记
    String OPTIONS = "moduleName"; // 同学们：目的是接收 每个module名称

    // Activity全类名
    String ACTIVITY_PACKAGE = "android.app.Activity";

    String FRAGMENT_PACKAGE = "androidx.fragment.app.Fragment";

    // IRouter api 包名
    String IROUTER_API_PACKAGE = "com.plumcookingwine.irouter.api";

    // IRouter api 的 IRouterGroup 高层标准
    String IROUTER_API_GROUP = IROUTER_API_PACKAGE + ".IRouterGroup";

    // IRouter api 的 IRouterPath 高层标准
    String IROUTER_API_PATH = IROUTER_API_PACKAGE + ".IRouterPath";

    // IRouter api 的 ParameterGet 高层标准
    String IROUTER_API_PARAMETER_GET = IROUTER_API_PACKAGE + ".ParameterGet";

    // IRouter api 的 ParameterGet 方法参数的名字
    String PARAMETER_NAME = "targetParameter";

    // IRouter api 的 ParmeterGet 方法的名字
    String PARAMETER_METHOD_NAME = "getParameter";

    // String全类名
    String STRING = "java.lang.String";

    // 路由组，中的 Path 里面的 方法名
    String PATH_METHOD_NAME = "getPathMap";

    // 路由组，中的 Group 里面的 方法名
    String GROUP_METHOD_NAME = "getGroupMap";

    // 路由组，中的 Path 里面 的 变量名 1
    String PATH_VAR1 = "pathMap";

    // 路由组，中的 Group 里面 的 变量名 1
    String GROUP_VAR1 = "groupMap";

    // 路由组，PATH 最终要生成的 文件名
    String PATH_FILE_NAME = "IRouter$$Path$$";

    // 路由组，GROUP 最终要生成的 文件名
    String GROUP_FILE_NAME = "IRouter$$Group$$";

    // @Parameter注解 的 包名 + 类名
    String PARAMETER_PACKAGE = "com.plumcookingwine.irouter.annotation.Parameter";

    // IRouter aip 的 ParmeterGet 的 生成文件名称 $$Parameter
    String PARAMETER_FILE_NAME = "$$Parameter";
}
