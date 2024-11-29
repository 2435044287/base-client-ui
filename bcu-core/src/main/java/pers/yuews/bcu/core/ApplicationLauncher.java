package pers.yuews.bcu.core;

import pers.yuews.bcu.core.common.annotaion.ComponentScan;
import pers.yuews.bcu.core.common.context.EnvironmentContext;
import pers.yuews.bcu.core.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * 应用程序启动器
 *
 * @author yuews
 * @create 2024/6/5 15:35
 * @describe
 */
public class ApplicationLauncher {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationLauncher.class);

    final static EnvironmentContext INSTANCE = EnvironmentContext.getInstance();

    /**
     * 启动
     *
     * @param primarySources 被实例化的类
     * @param args           构造方法的参数
     * @return
     */
    public static <T> T run(Class<?> primarySources, Object... args) {

        final ComponentScan componentScan = primarySources.getAnnotation(ComponentScan.class);
        if (componentScan == null) {
            throw new NullPointerException("未找到ComponentScan注解");
        }
        //1、实例化主类 并将 实例化的值放入容器中
        long time = System.currentTimeMillis();
        T o = build(primarySources, args);
        if (logger.isDebugEnabled()) {
            logger.debug("主类实例化完成，耗时：{}ms", System.currentTimeMillis() - time);
        }

        //2、将容器中所有注解为Container的类实例化 并将实例化的值放入容器中
        time = System.currentTimeMillis();
        INSTANCE.registerComponents(componentScan);
        if (logger.isDebugEnabled()) {
            logger.debug("容器中注册完成，耗时：{}ms", System.currentTimeMillis() - time);
        }

        //3、执行扩展器扩展
        time = System.currentTimeMillis();
        postProcessor(primarySources);
        if (logger.isDebugEnabled()) {
            logger.debug("扩展器扩展完成，耗时：{}ms", System.currentTimeMillis() - time);
        }

        return o;
    }

    private static <T> T build(Class<?> primarySources, Object... args) {
        T o;
        try {
            Class<?>[] parameterTypes = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
            final Constructor<?> constructor = primarySources.getConstructor(parameterTypes);
            o = (T) constructor.newInstance(args);
            INSTANCE.putByClass(primarySources.getName(), o);
            INSTANCE.putByClass("ApplicationEnvironmentContext", o);
        } catch (Exception e) {
            logger.error("实例化失败", e);
            throw new RuntimeException("实例化失败");
        }
        return o;
    }

    private static void postProcessor(Class<?> primarySources, Object... args) {
        try {
//            List<Class<?>> classByPackageName = ReflectionUtils.getClassByPackageName(primarySources.getPackage().getName());
            if (logger.isDebugEnabled()){
                logger.debug("扫描包：{}", primarySources.getPackage().getName());
//                logger.debug("扫描到的类：{}", classByPackageName);
            }

            for (Class<?> aClass :ReflectionUtils.getClassWithByType(primarySources.getPackage().getName()+".*",ApplicationRunner.class)) {
                try {
                    ApplicationRunner applicationRunner = INSTANCE.get(aClass.getName());
                    if (applicationRunner == null) {
                        applicationRunner = (ApplicationRunner) aClass.newInstance();
                        INSTANCE.putByClass(aClass.getName(), applicationRunner);
                    }
                    final long l = System.currentTimeMillis();
                    applicationRunner.run(primarySources, args);
                    if (logger.isDebugEnabled()){
                        logger.debug(aClass.getName()+"扩展成功，耗时：{}ms", System.currentTimeMillis() - l);
                    }
                } catch (Exception e) {
                    logger.error(aClass.getName()+"扩展器扩展失败", e);
                    //throw new RuntimeException("扩展器扩展失败");
                }
            }
        } catch (Exception e) {
            logger.error("扩展器扩展失败",e);
        }

    }


}
