# RouterDemo-master

# 使用

1. 在通用模块添加依赖：
    api 'com.plumcookingwine:irouter_api:1.0.0'

2. 在每个组件模块中添加依赖和注解处理器：
    annotationProcessor 'com.plumcookingwine:irouter_compiler:1.0.0'

3. 在每个组件模块中添加模块名（必须在）
    defaultConfig {
            // 。。。

            javaCompileOptions {
                annotationProcessorOptions {
                    // this.project.getName() == order
                    // this.getProject().getName() == order
                    arguments = [moduleName: project.getName()]
                }
            }
        }

