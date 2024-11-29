package pers.yuews.bcu.core.common.annotaion;

import java.lang.annotation.*;

/**
 * 被标记此注解的字段和类 都会被注入到EnvironmentContext中
 * @author yuews
 * @create 2024/6/3 11:19
 * @describe
 */
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Container {

    String name() default "";

}
