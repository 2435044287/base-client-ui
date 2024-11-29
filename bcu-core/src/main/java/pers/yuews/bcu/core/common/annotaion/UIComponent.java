package pers.yuews.bcu.core.common.annotaion;

import pers.yuews.bcu.core.common.event.listener.BaseListener;
import pers.yuews.bcu.core.common.event.listener.DefaultListener;

import java.lang.annotation.*;

/**
 * UI组件注解<br/>
 * 被此注解的类扫描，然后根据注解信息进行组件的创建 需要继承BuilderHandler<br/>
 * text：组件名称 <br/>
 * listenerEvent:事件监听器 不配置则使用默认的事件监听器 <br/>
 * @author yuews
 * @create 2024/5/28 14:10
 * @describe
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UIComponent {

    String text() default "undefined";

    boolean isEnable() default true;

    boolean isFirst() default false;

    Class<? extends BaseListener> listenerEvent() default DefaultListener.class;

    String name() default "";

    String group() default "0-default";

}
