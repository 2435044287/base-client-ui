package pers.yuews.bcu.core.common.annotaion;

import java.lang.annotation.*;

/**
 * @author yuews
 * @create 2024/5/29 16:17
 * @describe
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {

    String[] registerPackagePaths();

    String[] fieldValuePackagePaths();

}
