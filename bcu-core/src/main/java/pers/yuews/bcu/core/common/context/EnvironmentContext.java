package pers.yuews.bcu.core.common.context;

import pers.yuews.bcu.core.common.annotaion.Autowired;
import pers.yuews.bcu.core.common.annotaion.ComponentScan;
import pers.yuews.bcu.core.common.annotaion.Container;
import pers.yuews.bcu.core.common.annotaion.UIComponent;
import pers.yuews.bcu.core.utils.ArraysUtils;
import pers.yuews.bcu.core.utils.ReflectionUtils;
import pers.yuews.bcu.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * todo 需要解决的问题
 * 1、目前实例化类似通过Class#newInstance()去实例化对象，但是无法处理有参数的构造函数，所以需要通过反射去实例化对象。但是这样操作又会破坏单例模式
 * 如果忽略单例模式，那如何传参？
 *
 *
 * @author yuews
 * @create 2024/5/29 16:31
 * @describe
 */
public class EnvironmentContext {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentContext.class);

    private static ConcurrentHashMap<String, Object> envMap;

    private static volatile EnvironmentContext environmentContext;

    public static EnvironmentContext getInstance() {
        if (environmentContext == null) {
            synchronized (EnvironmentContext.class) {
                if (environmentContext == null) {
                    envMap = new ConcurrentHashMap<>(10);
                    environmentContext = new EnvironmentContext();
                }
            }
        }
        return environmentContext;
    }

    /**
     * 注册容器及填充字段属性
     * @param componentScan
     */
    public void registerComponents(ComponentScan componentScan) {
        if (componentScan == null) {
            return;
        }
        final String[] registerPackagePaths = componentScan.registerPackagePaths();

        if (!ArraysUtils.isEmpty(registerPackagePaths)) {
            for (String packageName : registerPackagePaths) {
                final Set<Class<?>> classByPackageName;
	            classByPackageName = ReflectionUtils.getAllClassByPacking(packageName);
	            //1、注册被标记@Container注解的类与字段
	            classByPackageName.forEach(this::register);
            }
        }
        //2、填充被标记@Autowired注解的字段
        final String[] fieldValuePackagePaths = componentScan.fieldValuePackagePaths();
        if (!ArraysUtils.isEmpty(fieldValuePackagePaths)) {
            for (String fieldValuePackagePath : fieldValuePackagePaths) {
                setFieldValueByAnnotation(fieldValuePackagePath);
            }
        }

    }

    /**
     * 填充包路径下的字段
     * todo 因反射问题，原生的反射会在项目打包后无法扫描到类路径下面的类，无法获取到被标记的字段类，导致无法注入。
     * @param fieldValuePackagePath 包路径
     */
    private void setFieldValueByAnnotation(String fieldValuePackagePath) {
        try {
//            logger.info("Autowired填充包路径字段{}", ReflectionUtils.getClassByPackageName(fieldValuePackagePath));
            final Set<Class<?>> classList = ReflectionUtils.getAllClassByPacking(fieldValuePackagePath);
//            logger.info("Autowired填充包路径字段{}", ReflectionUtils.getAnnotationByPackageName(fieldValuePackagePath, Autowired.class));
            for (Class<?> aClass : classList) {
                final Object o = get(aClass.getName());
                for (Field declaredField : aClass.getDeclaredFields()) {
                    final Autowired annotation = declaredField.getAnnotation(Autowired.class);
                    if (annotation != null) {
                        setFieldValue(annotation, declaredField, o);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("填充字段失败", e);
            //throw new RuntimeException(e);
        }
    }

    private <T> void setFieldValue(Autowired annotation, Field declaredField, Object o) {
        T t;
        // 1、判断注入的方式
        // 优先级
        // 1、注解的name 2、注解的type 3、字段名fieldName
        // 2、3 应该是并行的 只要没有被指定name、那么2、3哪个有就注入哪个

        if (StringUtils.isNotEmpty(annotation.name())) {
            //根据name注入
            t = get(annotation.name());
        } else if (java.lang.Object.class == annotation.type() ) {
            // 如果没有指定类型 那么就优先根据字段名注入
            t = get(declaredField.getName());
        } else {
            //最后才按照字段类型来注入
            t = get(annotation.type().getName());
        }
        //根据字段类型注入应该是一种约定，按照使用Spring的习惯，这种类型注入才是最多的 其次才是根据name注入

        //todo 这里的注入看起来有点奇怪 暂时先不处理
        /**
         * Spring的Autowired注解是根据类型注入的，如果类型相同，则注入成功，如果类型不同，则不注入
         */
        //1、根据类型注入
        //get()


        /**
        if (t == null) {
            throw new RuntimeException("没有找到对应的容器");
        }
         */
        try {
            declaredField.setAccessible(true);
            declaredField.set(o, t);
        } catch (Exception e) {
            //throw new RuntimeException(e);
            //此异常忽略 不做处理 防止影响业务
            logger.error(declaredField.getName()+"注入字段失败", e);

        }
    }

    /**
     * 注册被标记Container的类与字段
     * @param aClass 被扫描的类
     */
    private void register(Class<?> aClass) {
        //注册类
        for (Annotation aClassAnnotation : aClass.getAnnotations()) {
            if (aClassAnnotation instanceof Container) {
                registerClass(aClass, ((Container) aClassAnnotation).name());
            }
            if (aClassAnnotation instanceof UIComponent) {
                registerClass(aClass, ((UIComponent) aClassAnnotation).name());
            }
        }

        //注册方法
        registerMethod(aClass);

        //注册字段
        registerField(aClass);
    }

    /**
     * 注册被方法上被标记Container的注解
     *
     * @param aClass
     */
    private void registerMethod(Class<?> aClass) {
        for (Method declaredMethod : aClass.getDeclaredMethods()) {
            declaredMethod.setAccessible(true);
            final Container annotationMethod = declaredMethod.getAnnotation(Container.class);
            if (annotationMethod == null) {
                continue;
            }
            //容器名称
            String containerName = StringUtils.isNotEmpty(annotationMethod.name()) ? annotationMethod.name() : declaredMethod.getReturnType().getName();
            Object o = get(aClass.getName());
            if (o == null) {
                try {
                    final Constructor<?> declaredConstructor = aClass.getConstructor();
                    declaredConstructor.setAccessible(true);
                    o = declaredConstructor.newInstance();
                    putByClass(aClass.getName(), o);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    logger.error(declaredMethod.getName()+"创建方法对象失败", e);
                }
            }
            final Object invoke;
            try {
                invoke = declaredMethod.invoke(o);
                put(containerName, invoke);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error(declaredMethod.getName()+"创建方法对象失败", e);
            }


        }

    }

    /**
     * 注册被标记的字段
     *
     * @param aClass 被标记字段类
     */
    private void registerField(Class<?> aClass) {
        for (Field declaredField : aClass.getDeclaredFields()) {
            final Container annotationField = declaredField.getAnnotation(Container.class);
            if (annotationField == null) {
                continue;
            }
            // 获取到了被标记的注解的字段值
            final Object o = get(aClass.getName());
            if (o==null){
                //被标记的字段的类没有被注册进容器
                logger.error(aClass.getName()+"没有被注册进容器");
                throw new RuntimeException(aClass.getName()+"没有被注册进容器");
            }

            declaredField.setAccessible(true);
            Object o1 = null;
            try {
                o1 = declaredField.get(o);
            } catch (IllegalAccessException e) {
                logger.error(declaredField.getName()+"获取字段失败", e);
            }
            String containerName = StringUtils.isNotEmpty(annotationField.name()) ? annotationField.name() : declaredField.getName();
            putByClass(containerName, o1);
        }
    }


    /**
     * 注册被标记的类
     *
     * @param aClass         被标记的类
     * @param annotationName 注解容器名称
     */
    private void registerClass(Class<?> aClass, String annotationName) {
        String containerName = StringUtils.isNotEmpty(annotationName) ? annotationName : aClass.getName();
        if (get(containerName) == null) {
            try {
                final Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
                declaredConstructor.setAccessible(true);
                put(containerName, declaredConstructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                logger.error(aClass.getName()+"创建对象失败", e);
            }
        }
    }


    /**
     * 根据字段名设置值
     *
     * @param fieldName 字段名
     * @param value     字段所在的类
     */
    public void putByField(String fieldName, Object value) {
        final Field declaredField;
        try {
            declaredField = value.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            final Container annotation = declaredField.getAnnotation(Container.class);
            final Object o = declaredField.get(value);
            if (annotation != null && !"".equals(annotation.name())) {
                put(annotation.name(), o);
            } else {
                put(declaredField.getType().getName(), o);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void put(String key, Object value) {
        check(key, value);
        envMap.put(key, value);
    }

    public void check(String key, Object value) {
        final Object o = envMap.get(key);
        if (o != null && o != value) {
            throw new RuntimeException("容器中出现重复key，且value不一致。key:" + key + "");
        }
    }

    /**
     * 获取使用 类名.getClass().getName()
     * 需要保证添加进来的key不能重复
     *
     * @param key
     * @param value
     */
    public void putByClass(String key, Object value) {
        put(key, value);
    }

    /**
     * 优先根据 class获取值 如果没有则根据字段名获取值
     *
     * @param clazz
     * @param name
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> clazz, String name) {
        final Object o = get(clazz.getName());
        if (o == null) {
            return get(name);
        }
        return (T) o;
    }

    public <T> T get(Class<T> clazz) {
        return (T) envMap.get(clazz.getName());
    }

    public <T> T get(String name) {
        return (T) envMap.get(name);
    }

    private EnvironmentContext() {
    }

}

