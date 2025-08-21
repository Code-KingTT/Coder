package com.coder.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermission {
    /**
     * 所需权限
     */
    String[] value();
    
    /**
     * 权限验证逻辑：AND(所有权限都需要) 或 OR(任一权限即可)
     */
    Logical logical() default Logical.AND;
    
    enum Logical {
        AND, OR
    }
}