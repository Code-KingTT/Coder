package com.coder.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 *
 * @author Sunset
 * @date 2025/8/13
 */
@Slf4j
public final class JsonUtils {

    /**
     * 私有构造方法，防止实例化
     */
    private JsonUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * ObjectMapper实例
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 配置ObjectMapper
        initObjectMapper();
    }

    /**
     * 初始化ObjectMapper配置
     */
    private static void initObjectMapper() {
        // 注册JavaTime模块，支持LocalDateTime等类型
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        
        // 禁用时间戳序列化，使用字符串格式
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // 忽略未知属性，避免反序列化失败
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // 忽略null值属性
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        // 允许单引号
        OBJECT_MAPPER.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        
        // 允许不带引号的字段名
        OBJECT_MAPPER.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    /**
     * 获取ObjectMapper实例
     *
     * @return ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    // ================ 对象转JSON字符串 ================

    /**
     * 对象转JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 对象转格式化的JSON字符串（带缩进）
     *
     * @param obj 对象
     * @return 格式化的JSON字符串
     */
    public static String toPrettyJson(Object obj) {
        if (obj == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转格式化JSON失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 对象转JSON字节数组
     *
     * @param obj 对象
     * @return JSON字节数组
     */
    public static byte[] toJsonBytes(Object obj) {
        if (obj == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON字节数组失败: {}", e.getMessage(), e);
            return null;
        }
    }

    // ================ JSON字符串转对象 ================

    /**
     * JSON字符串转对象
     *
     * @param json  JSON字符串
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 对象实例
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || clazz == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON转对象失败: json={}, class={}, error={}", json, clazz.getName(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * JSON字符串转对象（使用TypeReference，支持泛型）
     *
     * @param json          JSON字符串
     * @param typeReference 类型引用
     * @param <T>           泛型类型
     * @return 对象实例
     */
    public static <T> T parseObject(String json, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(json) || typeReference == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("JSON转对象失败: json={}, typeReference={}, error={}", json, typeReference.getType(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * JSON字节数组转对象
     *
     * @param jsonBytes JSON字节数组
     * @param clazz     目标类型
     * @param <T>       泛型类型
     * @return 对象实例
     */
    public static <T> T parseObject(byte[] jsonBytes, Class<T> clazz) {
        if (jsonBytes == null || jsonBytes.length == 0 || clazz == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.readValue(jsonBytes, clazz);
        } catch (Exception e) {
            log.error("JSON字节数组转对象失败: class={}, error={}", clazz.getName(), e.getMessage(), e);
            return null;
        }
    }

    // ================ JSON转集合类型 ================

    /**
     * JSON字符串转List
     *
     * @param json  JSON字符串
     * @param clazz 元素类型
     * @param <T>   泛型类型
     * @return List集合
     */
    public static <T> List<T> parseList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || clazz == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.readValue(json, 
                OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            log.error("JSON转List失败: json={}, class={}, error={}", json, clazz.getName(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * JSON字符串转Map
     *
     * @param json JSON字符串
     * @return Map对象
     */
    public static Map<String, Object> parseMap(String json) {
        return parseObject(json, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * JSON字符串转Map（指定value类型）
     *
     * @param json       JSON字符串
     * @param valueClass value类型
     * @param <T>        value泛型类型
     * @return Map对象
     */
    public static <T> Map<String, T> parseMap(String json, Class<T> valueClass) {
        if (StringUtils.isBlank(json) || valueClass == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.readValue(json,
                OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, String.class, valueClass));
        } catch (JsonProcessingException e) {
            log.error("JSON转Map失败: json={}, valueClass={}, error={}", json, valueClass.getName(), e.getMessage(), e);
            return null;
        }
    }

    // ================ JSON节点操作 ================

    /**
     * 解析JSON字符串为JsonNode
     *
     * @param json JSON字符串
     * @return JsonNode
     */
    public static JsonNode parseTree(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("JSON解析为树结构失败: json={}, error={}", json, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从JsonNode中获取字符串值
     *
     * @param jsonNode JsonNode
     * @param fieldName 字段名
     * @return 字符串值
     */
    public static String getString(JsonNode jsonNode, String fieldName) {
        if (jsonNode == null || StringUtils.isBlank(fieldName)) {
            return null;
        }
        
        JsonNode fieldNode = jsonNode.get(fieldName);
        return fieldNode != null && !fieldNode.isNull() ? fieldNode.asText() : null;
    }

    /**
     * 从JsonNode中获取整数值
     *
     * @param jsonNode JsonNode
     * @param fieldName 字段名
     * @return 整数值
     */
    public static Integer getInteger(JsonNode jsonNode, String fieldName) {
        if (jsonNode == null || StringUtils.isBlank(fieldName)) {
            return null;
        }
        
        JsonNode fieldNode = jsonNode.get(fieldName);
        return fieldNode != null && !fieldNode.isNull() ? fieldNode.asInt() : null;
    }

    /**
     * 从JsonNode中获取长整数值
     *
     * @param jsonNode JsonNode
     * @param fieldName 字段名
     * @return 长整数值
     */
    public static Long getLong(JsonNode jsonNode, String fieldName) {
        if (jsonNode == null || StringUtils.isBlank(fieldName)) {
            return null;
        }
        
        JsonNode fieldNode = jsonNode.get(fieldName);
        return fieldNode != null && !fieldNode.isNull() ? fieldNode.asLong() : null;
    }

    /**
     * 从JsonNode中获取布尔值
     *
     * @param jsonNode JsonNode
     * @param fieldName 字段名
     * @return 布尔值
     */
    public static Boolean getBoolean(JsonNode jsonNode, String fieldName) {
        if (jsonNode == null || StringUtils.isBlank(fieldName)) {
            return null;
        }
        
        JsonNode fieldNode = jsonNode.get(fieldName);
        return fieldNode != null && !fieldNode.isNull() ? fieldNode.asBoolean() : null;
    }

    // ================ 对象类型转换 ================

    /**
     * 对象转换（通过JSON序列化/反序列化实现）
     *
     * @param fromValue 源对象
     * @param toClass   目标类型
     * @param <T>       泛型类型
     * @return 目标对象
     */
    public static <T> T convertValue(Object fromValue, Class<T> toClass) {
        if (fromValue == null || toClass == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.convertValue(fromValue, toClass);
        } catch (Exception e) {
            log.error("对象转换失败: fromValue={}, toClass={}, error={}", 
                fromValue, toClass.getName(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 对象转换（使用TypeReference）
     *
     * @param fromValue     源对象
     * @param typeReference 类型引用
     * @param <T>           泛型类型
     * @return 目标对象
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
        if (fromValue == null || typeReference == null) {
            return null;
        }
        
        try {
            return OBJECT_MAPPER.convertValue(fromValue, typeReference);
        } catch (Exception e) {
            log.error("对象转换失败: fromValue={}, typeReference={}, error={}", 
                fromValue, typeReference.getType(), e.getMessage(), e);
            return null;
        }
    }

    // ================ JSON有效性验证 ================

    /**
     * 验证JSON字符串是否有效
     *
     * @param json JSON字符串
     * @return true-有效，false-无效
     */
    public static boolean isValidJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    /**
     * 验证JSON字符串是否为有效的对象格式
     *
     * @param json JSON字符串
     * @return true-有效对象格式，false-无效
     */
    public static boolean isValidJsonObject(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
            return jsonNode.isObject();
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    /**
     * 验证JSON字符串是否为有效的数组格式
     *
     * @param json JSON字符串
     * @return true-有效数组格式，false-无效
     */
    public static boolean isValidJsonArray(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
            return jsonNode.isArray();
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    // ================ JSON合并操作 ================

    /**
     * 合并两个JSON对象
     *
     * @param json1 JSON对象1
     * @param json2 JSON对象2
     * @return 合并后的JSON字符串
     */
    public static String mergeJson(String json1, String json2) {
        if (StringUtils.isBlank(json1)) {
            return json2;
        }
        if (StringUtils.isBlank(json2)) {
            return json1;
        }
        
        try {
            JsonNode node1 = OBJECT_MAPPER.readTree(json1);
            JsonNode node2 = OBJECT_MAPPER.readTree(json2);
            
            if (!node1.isObject() || !node2.isObject()) {
                return json2; // 如果不是对象类型，返回第二个JSON
            }
            
            return OBJECT_MAPPER.writeValueAsString(mergeJsonNodes(node1, node2));
        } catch (JsonProcessingException e) {
            log.error("JSON合并失败: json1={}, json2={}, error={}", json1, json2, e.getMessage(), e);
            return json2;
        }
    }

    /**
     * 合并JsonNode
     *
     * @param mainNode   主节点
     * @param updateNode 更新节点
     * @return 合并后的JsonNode
     */
    private static JsonNode mergeJsonNodes(JsonNode mainNode, JsonNode updateNode) {
        var mergedNode = mainNode.deepCopy();
        
        if (updateNode.isObject()) {
            updateNode.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey();
                JsonNode fieldValue = entry.getValue();
                
                if (mergedNode.has(fieldName) && mergedNode.get(fieldName).isObject() && fieldValue.isObject()) {
                    // 递归合并嵌套对象
                    JsonNode mergedChild = mergeJsonNodes(mergedNode.get(fieldName), fieldValue);
                    ((com.fasterxml.jackson.databind.node.ObjectNode) mergedNode).replace(fieldName, mergedChild);
                } else {
                    // 直接替换字段值
                    ((com.fasterxml.jackson.databind.node.ObjectNode) mergedNode).replace(fieldName, fieldValue);
                }
            });
        }
        
        return mergedNode;
    }

    // ================ 工具方法 ================

    /**
     * 深拷贝对象
     *
     * @param obj   源对象
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 拷贝后的对象
     */
    public static <T> T deepCopy(Object obj, Class<T> clazz) {
        if (obj == null || clazz == null) {
            return null;
        }
        
        try {
            String json = toJson(obj);
            return parseObject(json, clazz);
        } catch (Exception e) {
            log.error("深拷贝失败: obj={}, class={}, error={}", obj, clazz.getName(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取JSON字符串的大小（字节数）
     *
     * @param json JSON字符串
     * @return 字节数
     */
    public static int getJsonSize(String json) {
        if (StringUtils.isBlank(json)) {
            return 0;
        }
        return json.getBytes().length;
    }

    /**
     * 格式化JSON字符串（美化输出）
     *
     * @param json JSON字符串
     * @return 格式化后的JSON字符串
     */
    public static String formatJson(String json) {
        if (StringUtils.isBlank(json)) {
            return json;
        }
        
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            log.error("JSON格式化失败: json={}, error={}", json, e.getMessage(), e);
            return json;
        }
    }

    /**
     * 压缩JSON字符串（移除空格和换行）
     *
     * @param json JSON字符串
     * @return 压缩后的JSON字符串
     */
    public static String compactJson(String json) {
        if (StringUtils.isBlank(json)) {
            return json;
        }
        
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
            return OBJECT_MAPPER.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            log.error("JSON压缩失败: json={}, error={}", json, e.getMessage(), e);
            return json;
        }
    }
}
