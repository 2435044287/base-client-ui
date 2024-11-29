package pers.yuews.bcu.core.utils;

import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * @author yuews
 * @create 2024/5/28 14:51
 * @describe
 */
public class ReflectionUtils {

    static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * 根据包名获取包下所有实现指定接口的类
     * @param packageName
     * @param type
     * @return
     * @param <T>
     */
    public static  <T> Set<Class<? extends T>>  getClassWithByType(String packageName, Class<T> type) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(type);
    }

    public static Set<Class<?>>  getAllClassByPacking(String packageName) {
        Set<Class<?>> set = new HashSet<>();
        Reflections reflections = new Reflections(packageName);
        String replace = packageName.replace(".*", "");
        Set<String> keys = reflections.getStore().keySet();
        Store store = reflections.getStore();
        for (String key : keys) {
            Map<String, Set<String>> stringSetMap = store.get(key);
            for (String parentClassName : stringSetMap.keySet()) {
                for (String childClassName : stringSetMap.get(parentClassName)) {
                    if (childClassName.startsWith(replace)){
	                    try {
		                    set.add(Class.forName(childClassName));
                            if (logger.isDebugEnabled()) {
                                logger.debug("子类："+childClassName);
                            }
	                    } catch (ClassNotFoundException e) {
		                    throw new RuntimeException(e);
	                    }

                    }
                }
            }
        }
        return set;
    }

    /**
     * todo 被jar包环境下 无法使用此方法查找包下的类 弃用
     * 根据包名获取包下所有类
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @Deprecated
    public static List<Class<?>> getClassByPackageName(String packageName) throws ClassNotFoundException, IOException {
        //Reflections reflections = new Reflections(packageName+".*");
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(packageName)
                .addScanners(Scanners.TypesAnnotated, Scanners.SubTypes));
        Set<Class<?>> subTypesOf = reflections.getSubTypesOf(Object.class);

        List<Class<?>> classes = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (logger.isDebugEnabled()) {
            logger.debug("获取包下的所有类:{}", classLoader);
        }
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("file")) {
                dirs.add(new File(resource.getFile()));
            } else if (resource.getProtocol().equals("jar")) {
                // 处理 JAR 文件
                String jarFilePath = resource.getPath().substring(5, resource.getPath().indexOf("!")); // 去掉 "file:" 和 "!/"
                JarFile jarFile = new JarFile(jarFilePath);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                        String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                        Class<?> clazz = Class.forName(className);
//                        dirs.add(new File(className));
                        classes.add(clazz);
                    }
                }
                jarFile.close();
            }
        }

        for (File directory : dirs) {
            if (directory.isFile()) {
                // 如果是文件，直接加载类
                String className = directory.toString().replace(File.separatorChar, '.');
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            } else {
                // 如果是目录，递归查找类
                classes.addAll(findClassesByPackageName(directory, packageName));
            }
        }
        return classes;
    }

    private static List<Class<?>> findClassesByPackageName(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClassesByPackageName(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }

    public static Set<Class<?>> getAnnotationByPackageName(String packageName, Class<? extends Annotation> annotationClass) throws ClassNotFoundException, IOException {
//        Reflections reflections = new Reflections(packageName);
//        Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(annotationClass);
//        return new ArrayList<>(classes);
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotationClass);
        return classes;
    }

    public static List<Class<?>> getAnnotationFieldByPackageName(String packageName, Class<? extends Annotation> annotationClass) throws ClassNotFoundException, IOException {
//        Reflections reflections = new Reflections(packageName);
//        Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(annotationClass);
//        return new ArrayList<>(fieldsAnnotatedWith);
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(packageName)
                .addScanners(new FieldAnnotationsScanner()));
        return null;
    }



    public static Object invokeGet(String fieldName,Class<?> aClass,Object value){
        try {
            return aClass.getMethod(conversionGetMethod(fieldName)).invoke(value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("反射获取属性值失败"+e);
        }
    }

    public static<T> void invokeSet(String fieldName,Class<?> aClass,Object value,T t){
        try {
            aClass.getMethod(conversionSetMethod(fieldName),t.getClass()).invoke(value,t);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("反射获取属性值失败"+e);
        }
    }

    public static String conversionGetMethod(String fieldName){
        if (fieldName==null||fieldName.length()==0){
            throw new RuntimeException("非法的字段名");
        }
        return "get"+Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    public static String conversionSetMethod(String fieldName){
        if (fieldName==null||fieldName.length()==0){
            throw new RuntimeException("非法的字段名");
        }
        return "set"+Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }


    private ReflectionUtils(){}
}
