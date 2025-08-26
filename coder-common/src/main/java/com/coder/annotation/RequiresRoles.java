package com.coder.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRoles {
    /**
     * 所需角色
     */
    String[] value();
    
    /**
     * 角色验证逻辑：AND(所有角色都需要) 或 OR(任一角色即可)
     */
    Logical logical() default Logical.AND;
    
    enum Logical {
        AND, OR
    }
}