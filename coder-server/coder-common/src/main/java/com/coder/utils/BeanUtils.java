package com.coder.utils;

import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean转换工具类
 *
 * @author Sunset
 * @date 2025/8/13
 */
@Slf4j
public final class BeanUtils {

    /**
     * 私有构造方法，防止实例化
     */
    private BeanUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 属性描述符缓存
     */
    private static final Map<Class<?>, PropertyDescriptor[]> PROPERTY_DESCRIPTOR_CACHE = new ConcurrentHashMap<>();

    // ================ 对象拷贝方法 ================

    /**
     * 拷贝对象属性
     * 将源对象的属性值拷贝到目标对象
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, (String[]) null);
    }

    /**
     * 拷贝对象属性（忽略指定属性）
     *
     * @param source           源对象
     * @param target           目标对象
     * @param ignoreProperties 忽略的属性名数组
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        if (source == null || target == null) {
            return;
        }

        try {
            // 使用Spring的BeanUtils进行属性拷贝
            org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
        } catch (Exception e) {
            log.error("属性拷贝失败: source={}, target={}, error={}", 
                source.getClass().getName(), target.getClass().getName(), e.getMessage(), e);
        }
    }

    /**
     * 创建目标对象并拷贝属性
     *
     * @param source      源对象
     * @param targetClass 目标类型
     * @param <T>         泛型类型
     * @return 目标对象实例
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        return copyProperties(source, targetClass, (String[]) null);
    }

    /**
     * 创建目标对象并拷贝属性（忽略指定属性）
     *
     * @param source           源对象
     * @param targetClass      目标类型
     * @param ignoreProperties 忽略的属性名数组
     * @param <T>              泛型类型
     * @return 目标对象实例
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass, String... ignoreProperties) {
        if (source == null || targetClass == null) {
            return null;
        }

        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            copyProperties(source, target, ignoreProperties);
            return target;
        } catch (Exception e) {
            log.error("创建对象并拷贝属性失败: source={}, targetClass={}, error={}", 
                source.getClass().getName(), targetClass.getName(), e.getMessage(), e);
            return null;
        }
    }

    // ================ 集合转换方法 ================

    /**
     * 转换对象列表
     *
     * @param sourceList  源对象列表
     * @param targetClass 目标类型
     * @param <S>         源类型
     * @param <T>         目标类型
     * @return 目标对象列表
     */
    public static <S, T> List<T> copyList(Collection<S> sourceList, Class<T> targetClass) {
        return copyList(sourceList, targetClass, (String[]) null);
    }

    /**
     * 转换对象列表（忽略指定属性）
     *
     * @param sourceList       源对象列表
     * @param targetClass      目标类型
     * @param ignoreProperties 忽略的属性名数组
     * @param <S>              源类型
     * @param <T>              目标类型
     * @return 目标对象列表
     */
    public static <S, T> List<T> copyList(Collection<S> sourceList, Class<T> targetClass, String... ignoreProperties) {
        if (sourceList == null || sourceList.isEmpty() || targetClass == null) {
            return new ArrayList<>();
        }

        List<T> targetList = new ArrayList<>(sourceList.size());
        for (S source : sourceList) {
            T target = copyProperties(source, targetClass, ignoreProperties);
            if (target != null) {
                targetList.add(target);
            }
        }
        return targetList;
    }

    // ================ 属性操作方法 ================

    /**
     * 获取对象属性值
     *
     * @param obj          对象
     * @param propertyName 属性名
     * @return 属性值
     */
    public static Object getProperty(Object obj, String propertyName) {
        if (obj == null || StrUtils.isBlank(propertyName)) {
            return null;
        }

        try {
            PropertyDescriptor[] descriptors = getPropertyDescriptors(obj.getClass());
            for (PropertyDescriptor descriptor : descriptors) {
                if (propertyName.equals(descriptor.getName())) {
                    Method readMethod = descriptor.getReadMethod();
                    if (readMethod != null) {
                        readMethod.setAccessible(true);
                        return readMethod.invoke(obj);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取属性值失败: obj={}, propertyName={}, error={}", 
                obj.getClass().getName(), propertyName, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 设置对象属性值
     *
     * @param obj           对象
     * @param propertyName  属性名
     * @param propertyValue 属性值
     */
    public static void setProperty(Object obj, String propertyName, Object propertyValue) {
        if (obj == null || StrUtils.isBlank(propertyName)) {
            return;
        }

        try {
            PropertyDescriptor[] descriptors = getPropertyDescriptors(obj.getClass());
            for (PropertyDescriptor descriptor : descriptors) {
                if (propertyName.equals(descriptor.getName())) {
                    Method writeMethod = descriptor.getWriteMethod();
                    if (writeMethod != null) {
                        writeMethod.setAccessible(true);
                        writeMethod.invoke(obj, propertyValue);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            log.error("设置属性值失败: obj={}, propertyName={}, propertyValue={}, error={}", 
                obj.getClass().getName(), propertyName, propertyValue, e.getMessage(), e);
        }
    }

    /**
     * 判断对象是否包含指定属性
     *
     * @param obj          对象
     * @param propertyName 属性名
     * @return true-包含，false-不包含
     */
    public static boolean hasProperty(Object obj, String propertyName) {
        if (obj == null || StrUtils.isBlank(propertyName)) {
            return false;
        }

        try {
            PropertyDescriptor[] descriptors = getPropertyDescriptors(obj.getClass());
            for (PropertyDescriptor descriptor : descriptors) {
                if (propertyName.equals(descriptor.getName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("检查属性存在性失败: obj={}, propertyName={}, error={}", 
                obj.getClass().getName(), propertyName, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 获取对象的所有属性名
     *
     * @param obj 对象
     * @return 属性名列表
     */
    public static List<String> getPropertyNames(Object obj) {
        List<String> propertyNames = new ArrayList<>();
        if (obj == null) {
            return propertyNames;
        }

        try {
            PropertyDescriptor[] descriptors = getPropertyDescriptors(obj.getClass());
            for (PropertyDescriptor descriptor : descriptors) {
                String propertyName = descriptor.getName();
                if (!"class".equals(propertyName)) { // 排除class属性
                    propertyNames.add(propertyName);
                }
            }
        } catch (Exception e) {
            log.error("获取属性名列表失败: obj={}, error={}", obj.getClass().getName(), e.getMessage(), e);
        }
        return propertyNames;
    }

    /**
     * 获取对象的所有属性名（指定类型）
     *
     * @param clazz 类类型
     * @return 属性名列表
     */
    public static List<String> getPropertyNames(Class<?> clazz) {
        List<String> propertyNames = new ArrayList<>();
        if (clazz == null) {
            return propertyNames;
        }

        try {
            PropertyDescriptor[] descriptors = getPropertyDescriptors(clazz);
            for (PropertyDescriptor descriptor : descriptors) {
                String propertyName = descriptor.getName();
                if (!"class".equals(propertyName)) { // 排除class属性
                    propertyNames.add(propertyName);
                }
            }
        } catch (Exception e) {
            log.error("获取属性名列表失败: class={}, error={}", clazz.getName(), e.getMessage(), e);
        }
        return propertyNames;
    }

    // ================ 对象比较方法 ================

    /**
     * 比较两个对象的属性差异
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 属性差异映射（属性名 -> [旧值, 新值]）
     */
    public static Map<String, Object[]> compareProperties(Object obj1, Object obj2) {
        Map<String, Object[]> differences = new ConcurrentHashMap<>();
        
        if (obj1 == null && obj2 == null) {
            return differences;
        }
        
        if (obj1 == null || obj2 == null) {
            // 如果其中一个为null，返回所有属性作为差异
            Object notNullObj = obj1 != null ? obj1 : obj2;
            List<String> propertyNames = getPropertyNames(notNullObj);
            for (String propertyName : propertyNames) {
                Object value = getProperty(notNullObj, propertyName);
                differences.put(propertyName, obj1 == null ? new Object[]{null, value} : new Object[]{value, null});
            }
            return differences;
        }
        
        if (!obj1.getClass().equals(obj2.getClass())) {
            log.warn("比较的两个对象类型不同: obj1={}, obj2={}", obj1.getClass().getName(), obj2.getClass().getName());
            return differences;
        }

        List<String> propertyNames = getPropertyNames(obj1);
        for (String propertyName : propertyNames) {
            Object value1 = getProperty(obj1, propertyName);
            Object value2 = getProperty(obj2, propertyName);
            
            if (!isEqual(value1, value2)) {
                differences.put(propertyName, new Object[]{value1, value2});
            }
        }
        
        return differences;
    }

    /**
     * 判断两个值是否相等（处理null值）
     *
     * @param value1 值1
     * @param value2 值2
     * @return true-相等，false-不相等
     */
    private static boolean isEqual(Object value1, Object value2) {
        if (value1 == null && value2 == null) {
            return true;
        }
        if (value1 == null || value2 == null) {
            return false;
        }
        return value1.equals(value2);
    }

    // ================ 对象验证方法 ================

    /**
     * 检查对象是否有空属性
     *
     * @param obj 对象
     * @return 空属性名列表
     */
    public static List<String> checkNullProperties(Object obj) {
        List<String> nullProperties = new ArrayList<>();
        if (obj == null) {
            return nullProperties;
        }

        List<String> propertyNames = getPropertyNames(obj);
        for (String propertyName : propertyNames) {
            Object value = getProperty(obj, propertyName);
            if (value == null) {
                nullProperties.add(propertyName);
            }
        }
        return nullProperties;
    }

    /**
     * 检查对象是否有空白字符串属性
     *
     * @param obj 对象
     * @return 空白字符串属性名列表
     */
    public static List<String> checkBlankStringProperties(Object obj) {
        List<String> blankProperties = new ArrayList<>();
        if (obj == null) {
            return blankProperties;
        }

        List<String> propertyNames = getPropertyNames(obj);
        for (String propertyName : propertyNames) {
            Object value = getProperty(obj, propertyName);
            if (value instanceof String && StrUtils.isBlank((String) value)) {
                blankProperties.add(propertyName);
            }
        }
        return blankProperties;
    }

    // ================ 对象转换方法 ================

    /**
     * 将对象转换为Map
     *
     * @param obj 对象
     * @return Map映射
     */
    public static Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = new ConcurrentHashMap<>();
        if (obj == null) {
            return map;
        }

        List<String> propertyNames = getPropertyNames(obj);
        for (String propertyName : propertyNames) {
            Object value = getProperty(obj, propertyName);
            map.put(propertyName, value);
        }
        return map;
    }

    /**
     * 将Map转换为对象
     *
     * @param map         Map映射
     * @param targetClass 目标类型
     * @param <T>         泛型类型
     * @return 对象实例
     */
    public static <T> T fromMap(Map<String, Object> map, Class<T> targetClass) {
        if (map == null || map.isEmpty() || targetClass == null) {
            return null;
        }

        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String propertyName = entry.getKey();
                Object propertyValue = entry.getValue();
                if (hasProperty(target, propertyName)) {
                    setProperty(target, propertyName, propertyValue);
                }
            }
            return target;
        } catch (Exception e) {
            log.error("Map转对象失败: targetClass={}, error={}", targetClass.getName(), e.getMessage(), e);
            return null;
        }
    }

    // ================ 工具方法 ================

    /**
     * 获取类的属性描述符（带缓存）
     *
     * @param clazz 类类型
     * @return 属性描述符数组
     */
    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        return PROPERTY_DESCRIPTOR_CACHE.computeIfAbsent(clazz, key -> {
            try {
                return java.beans.Introspector.getBeanInfo(key).getPropertyDescriptors();
            } catch (Exception e) {
                log.error("获取属性描述符失败: class={}, error={}", key.getName(), e.getMessage(), e);
                return new PropertyDescriptor[0];
            }
        });
    }

    /**
     * 清除属性描述符缓存
     */
    public static void clearCache() {
        PROPERTY_DESCRIPTOR_CACHE.clear();
    }

    /**
     * 获取缓存大小
     *
     * @return 缓存大小
     */
    public static int getCacheSize() {
        return PROPERTY_DESCRIPTOR_CACHE.size();
    }

    // ================ 深拷贝方法 ================

    /**
     * 深拷贝对象（使用JSON序列化实现）
     *
     * @param obj   源对象
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 深拷贝后的对象
     */
    public static <T> T deepCopy(Object obj, Class<T> clazz) {
        return JsonUtils.deepCopy(obj, clazz);
    }

    /**
     * 简单拷贝（浅拷贝）
     *
     * @param obj   源对象
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 拷贝后的对象
     */
    public static <T> T shallowCopy(Object obj, Class<T> clazz) {
        return copyProperties(obj, clazz);
    }

    // ================ 对象打印方法 ================

    /**
     * 将对象的属性和值格式化为字符串
     *
     * @param obj 对象
     * @return 格式化字符串
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(obj.getClass().getSimpleName()).append("{");
        
        List<String> propertyNames = getPropertyNames(obj);
        boolean first = true;
        for (String propertyName : propertyNames) {
            if (!first) {
                sb.append(", ");
            }
            Object value = getProperty(obj, propertyName);
            sb.append(propertyName).append("=").append(value);
            first = false;
        }
        
        sb.append("}");
        return sb.toString();
    }
}
