package pers.yuews.bcu.demo.common.handler;

import pers.yuews.bcu.core.ApplicationRunner;
import pers.yuews.bcu.core.common.annotaion.Autowired;
import pers.yuews.bcu.core.common.annotaion.ComponentScan;
import pers.yuews.bcu.core.common.annotaion.Container;
import pers.yuews.bcu.core.common.annotaion.UIComponent;
import pers.yuews.bcu.core.common.context.EnvironmentContext;
import pers.yuews.bcu.core.common.handler.BuilderHandler;
import pers.yuews.bcu.core.ui.BaseWindows;
import pers.yuews.bcu.core.utils.ReflectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yuews
 * @create 2024/6/5 16:47
 * @describe
 */
@Container
public class HandlerRunner implements ApplicationRunner {

    EnvironmentContext instance =EnvironmentContext.getInstance();

    @Override
    public void run(Class<?> primarySources,Object... args) {
        final ComponentScan componentScan =primarySources.getAnnotation(ComponentScan.class);

        //1、创建导航栏的组件
        //注册容器 和 本类
        registerComponents(componentScan);
        //System.out.println("注册成功");
    }

    /**
     * 注册被标记{@link UIComponent @UIComponent}的组件、被标记{@link Container @Container}的字段
     */
    private void registerComponents(ComponentScan componentScan ) {

        if (componentScan == null) {
            return;
        }
        final String[] path = componentScan.registerPackagePaths();
        if (path == null || path.length == 0) {
            return;
        }

        //1、创建导航栏的组件
        for (String packageName : path) {
            filtrationAnComponentMetadata(packageName).forEach(builderHandler -> {
                try {
                    builderHandler.handle(builderHandler);
                } catch (InstantiationException | IllegalAccessException e) {
                    //e.printStackTrace();
                    throw new RuntimeException("创建导航栏失败");
                }
            });
        }
    }

    /**
     * 获取所有被标记{@link UIComponent @UIComponent}  <br/>
     * 且继承自 {@link BuilderHandler BuilderHandler}<br/>
     * condition(isFirst() && isEnable())
     * @param packageName 扫描的包路径
     * @return 过滤后的构造器集合
     */
    private List<BuilderHandler> filtrationAnComponentMetadata(String packageName) {
        final List<BuilderHandler> handleList = new ArrayList<>(48);
        try {
            final Set<Class<?>> classes = ReflectionUtils.getAnnotationByPackageName(packageName, UIComponent.class);
            for (Class<?> aClass : classes) {
                final UIComponent annotation = aClass.getAnnotation(UIComponent.class);
                //获取第一个执行的类
                if (annotation.isEnable() && annotation.isFirst()) {
                    final Object o = instance.get(aClass.getName());
                    if (o instanceof BuilderHandler) {
                        final BuilderHandler handler = (BuilderHandler) o;
                        handler.setGroup(annotation.group());
                        handleList.add(handler);
                    }
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("获取类失败"+e);
        }
        //按照分组的值来进行排序
        handleList.sort((o1, o2) -> {
            if (o1.getGroup().equals(o2.getGroup())) {
                return 0;
            }
            return o1.getGroup().compareTo(o2.getGroup());
        });
        return handleList;
    }
}
