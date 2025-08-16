package com.coder.utils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 集合工具类
 *
 * @author Sunset
 * @date 2025/8/13
 */
public final class CollectionUtils {

    /**
     * 私有构造方法，防止实例化
     */
    private CollectionUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ================ 基础判断方法 ================

    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @return true-为空，false-不为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否不为空
     *
     * @param collection 集合
     * @return true-不为空，false-为空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是否为空
     *
     * @param map Map集合
     * @return true-为空，false-不为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断Map是否不为空
     *
     * @param map Map集合
     * @return true-不为空，false-为空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @return true-为空，false-不为空
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否不为空
     *
     * @param array 数组
     * @return true-不为空，false-为空
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    // ================ 集合大小方法 ================

    /**
     * 获取集合大小
     *
     * @param collection 集合
     * @return 集合大小
     */
    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    /**
     * 获取Map大小
     *
     * @param map Map集合
     * @return Map大小
     */
    public static int size(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    /**
     * 获取数组长度
     *
     * @param array 数组
     * @return 数组长度
     */
    public static int size(Object[] array) {
        return array == null ? 0 : array.length;
    }

    // ================ 集合创建方法 ================

    /**
     * 创建ArrayList
     *
     * @param elements 元素
     * @param <T>      元素类型
     * @return ArrayList
     */
    @SafeVarargs
    public static <T> List<T> newArrayList(T... elements) {
        List<T> list = new ArrayList<>();
        if (elements != null) {
            Collections.addAll(list, elements);
        }
        return list;
    }

    /**
     * 创建LinkedList
     *
     * @param elements 元素
     * @param <T>      元素类型
     * @return LinkedList
     */
    @SafeVarargs
    public static <T> List<T> newLinkedList(T... elements) {
        List<T> list = new LinkedList<>();
        if (elements != null) {
            Collections.addAll(list, elements);
        }
        return list;
    }

    /**
     * 创建HashSet
     *
     * @param elements 元素
     * @param <T>      元素类型
     * @return HashSet
     */
    @SafeVarargs
    public static <T> Set<T> newHashSet(T... elements) {
        Set<T> set = new HashSet<>();
        if (elements != null) {
            Collections.addAll(set, elements);
        }
        return set;
    }

    /**
     * 创建LinkedHashSet
     *
     * @param elements 元素
     * @param <T>      元素类型
     * @return LinkedHashSet
     */
    @SafeVarargs
    public static <T> Set<T> newLinkedHashSet(T... elements) {
        Set<T> set = new LinkedHashSet<>();
        if (elements != null) {
            Collections.addAll(set, elements);
        }
        return set;
    }

    /**
     * 创建HashMap
     *
     * @param <K> 键类型
     * @param <V> 值类型
     * @return HashMap
     */
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * 创建LinkedHashMap
     *
     * @param <K> 键类型
     * @param <V> 值类型
     * @return LinkedHashMap
     */
    public static <K, V> Map<K, V> newLinkedHashMap() {
        return new LinkedHashMap<>();
    }

    // ================ 集合操作方法 ================

    /**
     * 获取集合的第一个元素
     *
     * @param collection 集合
     * @param <T>        元素类型
     * @return 第一个元素，如果集合为空则返回null
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(0);
        }
        return collection.iterator().next();
    }

    /**
     * 获取集合的最后一个元素
     *
     * @param collection 集合
     * @param <T>        元素类型
     * @return 最后一个元素，如果集合为空则返回null
     */
    public static <T> T getLast(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        if (collection instanceof List) {
            List<T> list = (List<T>) collection;
            return list.get(list.size() - 1);
        }
        T last = null;
        for (T element : collection) {
            last = element;
        }
        return last;
    }

    /**
     * 安全获取List中指定索引的元素
     *
     * @param list  List集合
     * @param index 索引
     * @param <T>   元素类型
     * @return 元素，如果索引越界则返回null
     */
    public static <T> T get(List<T> list, int index) {
        if (isEmpty(list) || index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    /**
     * 安全获取List中指定索引的元素，如果不存在则返回默认值
     *
     * @param list         List集合
     * @param index        索引
     * @param defaultValue 默认值
     * @param <T>          元素类型
     * @return 元素或默认值
     */
    public static <T> T get(List<T> list, int index, T defaultValue) {
        T result = get(list, index);
        return result != null ? result : defaultValue;
    }

    // ================ 集合转换方法 ================

    /**
     * 将集合转换为数组
     *
     * @param collection 集合
     * @param clazz      数组元素类型
     * @param <T>        元素类型
     * @return 数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> collection, Class<T> clazz) {
        if (isEmpty(collection)) {
            return (T[]) java.lang.reflect.Array.newInstance(clazz, 0);
        }
        return collection.toArray((T[]) java.lang.reflect.Array.newInstance(clazz, collection.size()));
    }

    /**
     * 将数组转换为List
     *
     * @param array 数组
     * @param <T>   元素类型
     * @return List
     */
    @SafeVarargs
    public static <T> List<T> toList(T... array) {
        if (isEmpty(array)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * 将数组转换为Set
     *
     * @param array 数组
     * @param <T>   元素类型
     * @return Set
     */
    @SafeVarargs
    public static <T> Set<T> toSet(T... array) {
        if (isEmpty(array)) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(array));
    }

    // ================ 集合过滤方法 ================

    /**
     * 过滤集合元素
     *
     * @param collection 集合
     * @param predicate  过滤条件
     * @param <T>        元素类型
     * @return 过滤后的List
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection) || predicate == null) {
            return new ArrayList<>();
        }
        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * 移除集合中的null元素
     *
     * @param collection 集合
     * @param <T>        元素类型
     * @return 移除null后的List
     */
    public static <T> List<T> removeNulls(Collection<T> collection) {
        return filter(collection, Objects::nonNull);
    }

    /**
     * 移除集合中的空字符串元素
     *
     * @param collection 字符串集合
     * @return 移除空字符串后的List
     */
    public static List<String> removeBlankStrings(Collection<String> collection) {
        return filter(collection, StrUtils::isNotBlank);
    }

    // ================ 集合转换映射方法 ================

    /**
     * 将集合元素转换为另一种类型
     *
     * @param collection 集合
     * @param mapper     转换函数
     * @param <T>        源元素类型
     * @param <R>        目标元素类型
     * @return 转换后的List
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        if (isEmpty(collection) || mapper == null) {
            return new ArrayList<>();
        }
        return collection.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * 将集合转换为Map（以指定属性作为key）
     *
     * @param collection  集合
     * @param keyMapper   key映射函数
     * @param <T>         元素类型
     * @param <K>         key类型
     * @return Map
     */
    public static <T, K> Map<K, T> toMap(Collection<T> collection, Function<T, K> keyMapper) {
        if (isEmpty(collection) || keyMapper == null) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, Function.identity(), (existing, replacement) -> replacement));
    }

    /**
     * 将集合转换为Map
     *
     * @param collection   集合
     * @param keyMapper    key映射函数
     * @param valueMapper  value映射函数
     * @param <T>          元素类型
     * @param <K>          key类型
     * @param <V>          value类型
     * @return Map
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        if (isEmpty(collection) || keyMapper == null || valueMapper == null) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper, (existing, replacement) -> replacement));
    }

    /**
     * 将集合按照指定条件分组
     *
     * @param collection 集合
     * @param classifier 分类函数
     * @param <T>        元素类型
     * @param <K>        分组key类型
     * @return 分组后的Map
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> classifier) {
        if (isEmpty(collection) || classifier == null) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.groupingBy(classifier));
    }

    // ================ 集合交并差运算 ================

    /**
     * 获取两个集合的交集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         元素类型
     * @return 交集
     */
    public static <T> Set<T> intersection(Collection<T> collection1, Collection<T> collection2) {
        if (isEmpty(collection1) || isEmpty(collection2)) {
            return new HashSet<>();
        }
        Set<T> result = new HashSet<>(collection1);
        result.retainAll(collection2);
        return result;
    }

    /**
     * 获取两个集合的并集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         元素类型
     * @return 并集
     */
    public static <T> Set<T> union(Collection<T> collection1, Collection<T> collection2) {
        Set<T> result = new HashSet<>();
        if (isNotEmpty(collection1)) {
            result.addAll(collection1);
        }
        if (isNotEmpty(collection2)) {
            result.addAll(collection2);
        }
        return result;
    }

    /**
     * 获取两个集合的差集（collection1 - collection2）
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         元素类型
     * @return 差集
     */
    public static <T> Set<T> difference(Collection<T> collection1, Collection<T> collection2) {
        if (isEmpty(collection1)) {
            return new HashSet<>();
        }
        Set<T> result = new HashSet<>(collection1);
        if (isNotEmpty(collection2)) {
            result.removeAll(collection2);
        }
        return result;
    }

    // ================ 集合比较方法 ================

    /**
     * 判断两个集合是否相等（忽略顺序）
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @return true-相等，false-不相等
     */
    public static boolean isEqual(Collection<?> collection1, Collection<?> collection2) {
        if (collection1 == collection2) {
            return true;
        }
        if (collection1 == null || collection2 == null) {
            return false;
        }
        if (collection1.size() != collection2.size()) {
            return false;
        }
        return new HashSet<>(collection1).equals(new HashSet<>(collection2));
    }

    /**
     * 判断集合是否包含指定元素
     *
     * @param collection 集合
     * @param element    元素
     * @param <T>        元素类型
     * @return true-包含，false-不包含
     */
    public static <T> boolean contains(Collection<T> collection, T element) {
        return isNotEmpty(collection) && collection.contains(element);
    }

    /**
     * 判断集合是否包含所有指定元素
     *
     * @param collection 集合
     * @param elements   元素
     * @param <T>        元素类型
     * @return true-包含所有，false-不包含所有
     */
    @SafeVarargs
    public static <T> boolean containsAll(Collection<T> collection, T... elements) {
        if (isEmpty(collection) || isEmpty(elements)) {
            return false;
        }
        return collection.containsAll(Arrays.asList(elements));
    }

    /**
     * 判断集合是否包含任意一个指定元素
     *
     * @param collection 集合
     * @param elements   元素
     * @param <T>        元素类型
     * @return true-包含任意一个，false-都不包含
     */
    @SafeVarargs
    public static <T> boolean containsAny(Collection<T> collection, T... elements) {
        if (isEmpty(collection) || isEmpty(elements)) {
            return false;
        }
        for (T element : elements) {
            if (collection.contains(element)) {
                return true;
            }
        }
        return false;
    }

    // ================ 集合去重方法 ================

    /**
     * 去除集合中的重复元素
     *
     * @param collection 集合
     * @param <T>        元素类型
     * @return 去重后的List
     */
    public static <T> List<T> distinct(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 根据指定属性去除集合中的重复元素
     *
     * @param collection 集合
     * @param keyMapper  属性映射函数
     * @param <T>        元素类型
     * @param <K>        属性类型
     * @return 去重后的List
     */
    public static <T, K> List<T> distinctBy(Collection<T> collection, Function<T, K> keyMapper) {
        if (isEmpty(collection) || keyMapper == null) {
            return new ArrayList<>();
        }
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    // ================ 集合分页方法 ================

    /**
     * 对集合进行分页
     *
     * @param collection 集合
     * @param pageNum    页码（从1开始）
     * @param pageSize   每页大小
     * @param <T>        元素类型
     * @return 分页后的List
     */
    public static <T> List<T> page(Collection<T> collection, int pageNum, int pageSize) {
        if (isEmpty(collection) || pageNum < 1 || pageSize < 1) {
            return new ArrayList<>();
        }
        
        List<T> list = new ArrayList<>(collection);
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, list.size());
        
        if (startIndex >= list.size()) {
            return new ArrayList<>();
        }
        
        return list.subList(startIndex, endIndex);
    }

    /**
     * 将集合分批处理
     *
     * @param collection 集合
     * @param batchSize  批次大小
     * @param <T>        元素类型
     * @return 分批后的List
     */
    public static <T> List<List<T>> batch(Collection<T> collection, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        if (isEmpty(collection) || batchSize < 1) {
            return batches;
        }
        
        List<T> list = new ArrayList<>(collection);
        for (int i = 0; i < list.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, list.size());
            batches.add(list.subList(i, endIndex));
        }
        
        return batches;
    }

    // ================ 集合统计方法 ================

    /**
     * 统计集合中满足条件的元素个数
     *
     * @param collection 集合
     * @param predicate  条件
     * @param <T>        元素类型
     * @return 满足条件的元素个数
     */
    public static <T> long count(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection) || predicate == null) {
            return 0;
        }
        return collection.stream()
                .filter(predicate)
                .count();
    }

    /**
     * 判断集合中是否存在满足条件的元素
     *
     * @param collection 集合
     * @param predicate  条件
     * @param <T>        元素类型
     * @return true-存在，false-不存在
     */
    public static <T> boolean anyMatch(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection) || predicate == null) {
            return false;
        }
        return collection.stream()
                .anyMatch(predicate);
    }

    /**
     * 判断集合中是否所有元素都满足条件
     *
     * @param collection 集合
     * @param predicate  条件
     * @param <T>        元素类型
     * @return true-所有都满足，false-不是所有都满足
     */
    public static <T> boolean allMatch(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection) || predicate == null) {
            return false;
        }
        return collection.stream()
                .allMatch(predicate);
    }

    /**
     * 判断集合中是否没有元素满足条件
     *
     * @param collection 集合
     * @param predicate  条件
     * @param <T>        元素类型
     * @return true-没有满足，false-有满足
     */
    public static <T> boolean noneMatch(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection) || predicate == null) {
            return true;
        }
        return collection.stream()
                .noneMatch(predicate);
    }
}
