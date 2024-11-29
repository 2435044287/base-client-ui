package pers.yuews.bcu.core.common.annotaion;

import java.lang.annotation.*;

/**
 * 被标记此注解的属性，会从容器中取值 自动注入进去
 * @author yuews
 * @create 2024/6/3 16:06
 * @describe
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    String name() default "";

    Class<?> type() default java.lang.Object.class;;



}
