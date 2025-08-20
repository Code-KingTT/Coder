package com.coder.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * 
 * Redis常用操作的封装，包括：
 *     字符串操作：set、get、delete等
 *     哈希操作：hset、hget、hdel等
 *     列表操作：lpush、rpush、lpop等
 *     集合操作：sadd、srem、smembers等
 *     有序集合操作：zadd、zrem、zrange等
 *     过期时间操作：expire、ttl等
 *     分布式锁操作：tryLock、unlock等
 *
 * @author Sunset
 * @date 2025/8/14
 */
@Slf4j
@Component
public final class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 字符串操作
     */
    private ValueOperations<String, Object> valueOps;
    private ValueOperations<String, String> stringValueOps;

    /**
     * 哈希操作
     */
    private HashOperations<String, String, Object> hashOps;

    /**
     * 列表操作
     */
    private ListOperations<String, Object> listOps;

    /**
     * 集合操作
     */
    private SetOperations<String, Object> setOps;

    /**
     * 有序集合操作
     */
    private ZSetOperations<String, Object> zSetOps;

    /**
     * 初始化Redis操作对象
     */
    @PostConstruct
    public void init() {
        this.valueOps = redisTemplate.opsForValue();
        this.stringValueOps = stringRedisTemplate.opsForValue();
        this.hashOps = redisTemplate.opsForHash();
        this.listOps = redisTemplate.opsForList();
        this.setOps = redisTemplate.opsForSet();
        this.zSetOps = redisTemplate.opsForZSet();

        log.info("RedisUtils初始化完成");
    }

    /**
     * 分布式锁脚本 - 释放锁
     */
    private static final String UNLOCK_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "    return redis.call('del', KEYS[1]) " +
        "else " +
        "    return 0 " +
        "end";

    /**
     * 分布式锁脚本 - 续期
     */
    private static final String RENEW_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "    return redis.call('expire', KEYS[1], ARGV[2]) " +
        "else " +
        "    return 0 " +
        "end";

    // ================ 通用操作 ================

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true-存在，false-不存在
     */
    public boolean hasKey(String key) {
        if (StrUtils.isBlank(key)) {
            return false;
        }
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("判断key是否存在失败: key={}, error={}", key, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除key
     *
     * @param key 键
     * @return true-删除成功，false-删除失败
     */
    public boolean delete(String key) {
        if (StrUtils.isBlank(key)) {
            return false;
        }
        try {
            return Boolean.TRUE.equals(redisTemplate.delete(key));
        } catch (Exception e) {
            log.error("删除key失败: key={}, error={}", key, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 批量删除key
     *
     * @param keys 键集合
     * @return 删除成功的数量
     */
    public long delete(Collection<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return 0L;
        }
        try {
            Long result = redisTemplate.delete(keys);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("批量删除key失败: keys={}, error={}", keys, e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 设置key的过期时间
     *
     * @param key     键
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return true-设置成功，false-设置失败
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        if (StrUtils.isBlank(key) || timeout <= 0 || unit == null) {
            return false;
        }
        try {
            return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
        } catch (Exception e) {
            log.error("设置过期时间失败: key={}, timeout={}, unit={}, error={}", 
                key, timeout, unit, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 设置key的过期时间（秒）
     *
     * @param key     键
     * @param seconds 过期时间（秒）
     * @return true-设置成功，false-设置失败
     */
    public boolean expire(String key, long seconds) {
        return expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取key的过期时间
     *
     * @param key 键
     * @return 过期时间（秒），-1表示永不过期，-2表示key不存在
     */
    public long getExpire(String key) {
        if (StrUtils.isBlank(key)) {
            return -2L;
        }
        try {
            Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            return expire != null ? expire : -2L;
        } catch (Exception e) {
            log.error("获取过期时间失败: key={}, error={}", key, e.getMessage(), e);
            return -2L;
        }
    }

    // ================ 字符串操作 ================

    /**
     * 设置字符串值
     *
     * @param key   键
     * @param value 值
     * @return true-设置成功，false-设置失败
     */
    public boolean set(String key, Object value) {
        if (StrUtils.isBlank(key)) {
            return false;
        }
        try {
            valueOps.set(key, value);
            return true;
        } catch (Exception e) {
            log.error("设置字符串值失败: key={}, value={}, error={}", key, value, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 设置字符串值并指定过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return true-设置成功，false-设置失败
     */
    public boolean set(String key, Object value, long timeout, TimeUnit unit) {
        if (StrUtils.isBlank(key) || timeout <= 0 || unit == null) {
            return false;
        }
        try {
            valueOps.set(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            log.error("设置字符串值（带过期时间）失败: key={}, value={}, timeout={}, unit={}, error={}", 
                key, value, timeout, unit, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 设置字符串值并指定过期时间（秒）
     *
     * @param key     键
     * @param value   值
     * @param seconds 过期时间（秒）
     * @return true-设置成功，false-设置失败
     */
    public boolean set(String key, Object value, long seconds) {
        return set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取字符串值
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        if (StrUtils.isBlank(key)) {
            return null;
        }
        try {
            return valueOps.get(key);
        } catch (Exception e) {
            log.error("获取字符串值失败: key={}, error={}", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取字符串值并转换为指定类型
     *
     * @param key   键
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object value = get(key);
        if (value == null || clazz == null) {
            return null;
        }
        try {
            if (clazz.isInstance(value)) {
                return (T) value;
            }
            return JsonUtils.convertValue(value, clazz);
        } catch (Exception e) {
            log.error("获取并转换字符串值失败: key={}, clazz={}, error={}", 
                key, clazz.getName(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 递增
     *
     * @param key 键
     * @return 递增后的值
     */
    public long increment(String key) {
        return increment(key, 1L);
    }

    /**
     * 递增指定步长
     *
     * @param key   键
     * @param delta 步长
     * @return 递增后的值
     */
    public long increment(String key, long delta) {
        if (StrUtils.isBlank(key)) {
            return 0L;
        }
        try {
            Long result = valueOps.increment(key, delta);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("递增失败: key={}, delta={}, error={}", key, delta, e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 递减
     *
     * @param key 键
     * @return 递减后的值
     */
    public long decrement(String key) {
        return decrement(key, 1L);
    }

    /**
     * 递减指定步长
     *
     * @param key   键
     * @param delta 步长
     * @return 递减后的值
     */
    public long decrement(String key, long delta) {
        if (StrUtils.isBlank(key)) {
            return 0L;
        }
        try {
            Long result = valueOps.decrement(key, delta);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("递减失败: key={}, delta={}, error={}", key, delta, e.getMessage(), e);
            return 0L;
        }
    }

    // ================ 哈希操作 ================

    /**
     * 向哈希表中放入数据
     *
     * @param key     键
     * @param hashKey 哈希键
     * @param value   值
     * @return true-设置成功，false-设置失败
     */
    public boolean hset(String key, String hashKey, Object value) {
        if (StrUtils.isBlank(key) || StrUtils.isBlank(hashKey)) {
            return false;
        }
        try {
            hashOps.put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error("设置哈希值失败: key={}, hashKey={}, value={}, error={}", 
                key, hashKey, value, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向哈希表中放入数据并设置过期时间
     *
     * @param key     键
     * @param hashKey 哈希键
     * @param value   值
     * @param seconds 过期时间（秒）
     * @return true-设置成功，false-设置失败
     */
    public boolean hset(String key, String hashKey, Object value, long seconds) {
        boolean result = hset(key, hashKey, value);
        if (result && seconds > 0) {
            expire(key, seconds);
        }
        return result;
    }

    /**
     * 获取哈希表中指定字段的值
     *
     * @param key     键
     * @param hashKey 哈希键
     * @return 值
     */
    public Object hget(String key, String hashKey) {
        if (StrUtils.isBlank(key) || StrUtils.isBlank(hashKey)) {
            return null;
        }
        try {
            return hashOps.get(key, hashKey);
        } catch (Exception e) {
            log.error("获取哈希值失败: key={}, hashKey={}, error={}", key, hashKey, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取哈希表中指定字段的值并转换为指定类型
     *
     * @param key     键
     * @param hashKey 哈希键
     * @param clazz   目标类型
     * @param <T>     泛型类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T hget(String key, String hashKey, Class<T> clazz) {
        Object value = hget(key, hashKey);
        if (value == null || clazz == null) {
            return null;
        }
        try {
            if (clazz.isInstance(value)) {
                return (T) value;
            }
            return JsonUtils.convertValue(value, clazz);
        } catch (Exception e) {
            log.error("获取并转换哈希值失败: key={}, hashKey={}, clazz={}, error={}", 
                key, hashKey, clazz.getName(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取哈希表中所有字段
     *
     * @param key 键
     * @return 哈希表
     */
    public Map<String, Object> hgetAll(String key) {
        if (StrUtils.isBlank(key)) {
            return new HashMap<>();
        }
        try {
            return hashOps.entries(key);
        } catch (Exception e) {
            log.error("获取所有哈希值失败: key={}, error={}", key, e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * 批量设置哈希表字段
     *
     * @param key 键
     * @param map 哈希表
     * @return true-设置成功，false-设置失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        if (StrUtils.isBlank(key) || CollectionUtils.isEmpty(map)) {
            return false;
        }
        try {
            hashOps.putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("批量设置哈希值失败: key={}, map={}, error={}", key, map, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 批量设置哈希表字段并设置过期时间
     *
     * @param key     键
     * @param map     哈希表
     * @param seconds 过期时间（秒）
     * @return true-设置成功，false-设置失败
     */
    public boolean hmset(String key, Map<String, Object> map, long seconds) {
        boolean result = hmset(key, map);
        if (result && seconds > 0) {
            expire(key, seconds);
        }
        return result;
    }

    /**
     * 删除哈希表字段
     *
     * @param key      键
     * @param hashKeys 哈希键数组
     * @return 删除成功的字段数量
     */
    public long hdel(String key, String... hashKeys) {
        if (StrUtils.isBlank(key) || hashKeys == null || hashKeys.length == 0) {
            return 0L;
        }
        try {
            Long result = hashOps.delete(key, (Object[]) hashKeys);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("删除哈希字段失败: key={}, hashKeys={}, error={}", 
                key, Arrays.toString(hashKeys), e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 判断哈希表字段是否存在
     *
     * @param key     键
     * @param hashKey 哈希键
     * @return true-存在，false-不存在
     */
    public boolean hexists(String key, String hashKey) {
        if (StrUtils.isBlank(key) || StrUtils.isBlank(hashKey)) {
            return false;
        }
        try {
            return Boolean.TRUE.equals(hashOps.hasKey(key, hashKey));
        } catch (Exception e) {
            log.error("判断哈希字段是否存在失败: key={}, hashKey={}, error={}", 
                key, hashKey, e.getMessage(), e);
            return false;
        }
    }

    // ================ 列表操作 ================

    /**
     * 将值从左侧推入列表
     *
     * @param key   键
     * @param value 值
     * @return 列表长度
     */
    public long lpush(String key, Object value) {
        if (StrUtils.isBlank(key)) {
            return 0L;
        }
        try {
            Long result = listOps.leftPush(key, value);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("左侧推入列表失败: key={}, value={}, error={}", key, value, e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 将值从右侧推入列表
     *
     * @param key   键
     * @param value 值
     * @return 列表长度
     */
    public long rpush(String key, Object value) {
        if (StrUtils.isBlank(key)) {
            return 0L;
        }
        try {
            Long result = listOps.rightPush(key, value);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("右侧推入列表失败: key={}, value={}, error={}", key, value, e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 从左侧弹出列表元素
     *
     * @param key 键
     * @return 弹出的元素
     */
    public Object lpop(String key) {
        if (StrUtils.isBlank(key)) {
            return null;
        }
        try {
            return listOps.leftPop(key);
        } catch (Exception e) {
            log.error("左侧弹出列表元素失败: key={}, error={}", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从右侧弹出列表元素
     *
     * @param key 键
     * @return 弹出的元素
     */
    public Object rpop(String key) {
        if (StrUtils.isBlank(key)) {
            return null;
        }
        try {
            return listOps.rightPop(key);
        } catch (Exception e) {
            log.error("右侧弹出列表元素失败: key={}, error={}", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取列表指定范围的元素
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 元素列表
     */
    public List<Object> lrange(String key, long start, long end) {
        if (StrUtils.isBlank(key)) {
            return new ArrayList<>();
        }
        try {
            return listOps.range(key, start, end);
        } catch (Exception e) {
            log.error("获取列表范围元素失败: key={}, start={}, end={}, error={}", 
                key, start, end, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取列表长度
     *
     * @param key 键
     * @return 列表长度
     */
    public long llen(String key) {
        if (StrUtils.isBlank(key)) {
            return 0L;
        }
        try {
            Long result = listOps.size(key);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("获取列表长度失败: key={}, error={}", key, e.getMessage(), e);
            return 0L;
        }
    }

    // ================ 集合操作 ================

    /**
     * 向集合添加元素
     *
     * @param key    键
     * @param values 值数组
     * @return 添加成功的元素数量
     */
    public long sadd(String key, Object... values) {
        if (StrUtils.isBlank(key) || values == null || values.length == 0) {
            return 0L;
        }
        try {
            Long result = setOps.add(key, values);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("向集合添加元素失败: key={}, values={}, error={}", 
                key, Arrays.toString(values), e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 从集合移除元素
     *
     * @param key    键
     * @param values 值数组
     * @return 移除成功的元素数量
     */
    public long srem(String key, Object... values) {
        if (StrUtils.isBlank(key) || values == null || values.length == 0) {
            return 0L;
        }
        try {
            Long result = setOps.remove(key, values);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("从集合移除元素失败: key={}, values={}, error={}", 
                key, Arrays.toString(values), e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 获取集合所有元素
     *
     * @param key 键
     * @return 集合元素
     */
    public Set<Object> smembers(String key) {
        if (StrUtils.isBlank(key)) {
            return new HashSet<>();
        }
        try {
            return setOps.members(key);
        } catch (Exception e) {
            log.error("获取集合所有元素失败: key={}, error={}", key, e.getMessage(), e);
            return new HashSet<>();
        }
    }

    /**
     * 判断元素是否在集合中
     *
     * @param key   键
     * @param value 值
     * @return true-存在，false-不存在
     */
    public boolean sismember(String key, Object value) {
        if (StrUtils.isBlank(key)) {
            return false;
        }
        try {
            return Boolean.TRUE.equals(setOps.isMember(key, value));
        } catch (Exception e) {
            log.error("判断元素是否在集合中失败: key={}, value={}, error={}", 
                key, value, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取集合元素数量
     *
     * @param key 键
     * @return 集合大小
     */
    public long scard(String key) {
        if (StrUtils.isBlank(key)) {
            return 0L;
        }
        try {
            Long result = setOps.size(key);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("获取集合大小失败: key={}, error={}", key, e.getMessage(), e);
            return 0L;
        }
    }

    // ================ 有序集合操作 ================

    /**
     * 向有序集合添加元素
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return true-添加成功，false-添加失败
     */
    public boolean zadd(String key, Object value, double score) {
        if (StrUtils.isBlank(key)) {
            return false;
        }
        try {
            return Boolean.TRUE.equals(zSetOps.add(key, value, score));
        } catch (Exception e) {
            log.error("向有序集合添加元素失败: key={}, value={}, score={}, error={}", 
                key, value, score, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 从有序集合移除元素
     *
     * @param key    键
     * @param values 值数组
     * @return 移除成功的元素数量
     */
    public long zrem(String key, Object... values) {
        if (StrUtils.isBlank(key) || values == null || values.length == 0) {
            return 0L;
        }
        try {
            Long result = zSetOps.remove(key, values);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("从有序集合移除元素失败: key={}, values={}, error={}", 
                key, Arrays.toString(values), e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 获取有序集合指定范围的元素
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 元素集合
     */
    public Set<Object> zrange(String key, long start, long end) {
        if (StrUtils.isBlank(key)) {
            return new LinkedHashSet<>();
        }
        try {
            return zSetOps.range(key, start, end);
        } catch (Exception e) {
            log.error("获取有序集合范围元素失败: key={}, start={}, end={}, error={}", 
                key, start, end, e.getMessage(), e);
            return new LinkedHashSet<>();
        }
    }

    /**
     * 获取有序集合指定分数范围的元素
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 元素集合
     */
    public Set<Object> zrangeByScore(String key, double min, double max) {
        if (StrUtils.isBlank(key)) {
            return new LinkedHashSet<>();
        }
        try {
            return zSetOps.rangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("按分数获取有序集合元素失败: key={}, min={}, max={}, error={}", 
                key, min, max, e.getMessage(), e);
            return new LinkedHashSet<>();
        }
    }

    /**
     * 获取有序集合元素数量
     *
     * @param key 键
     * @return 集合大小
     */
    public long zcard(String key) {
        if (StrUtils.isBlank(key)) {
            return 0L;
        }
        try {
            Long result = zSetOps.size(key);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("获取有序集合大小失败: key={}, error={}", key, e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 获取元素在有序集合中的分数
     *
     * @param key   键
     * @param value 值
     * @return 分数，null表示元素不存在
     */
    public Double zscore(String key, Object value) {
        if (StrUtils.isBlank(key)) {
            return null;
        }
        try {
            return zSetOps.score(key, value);
        } catch (Exception e) {
            log.error("获取有序集合元素分数失败: key={}, value={}, error={}", 
                key, value, e.getMessage(), e);
            return null;
        }
    }

    // ================ 分布式锁操作 ================

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey     锁键
     * @param requestId   请求标识
     * @param expireTime  锁过期时间（毫秒）
     * @return true-获取成功，false-获取失败
     */
    public boolean tryLock(String lockKey, String requestId, long expireTime) {
        if (StrUtils.isBlank(lockKey) || StrUtils.isBlank(requestId) || expireTime <= 0) {
            return false;
        }
        try {
            Boolean result = stringValueOps.setIfAbsent(lockKey, requestId, 
                Duration.ofMillis(expireTime));
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("获取分布式锁失败: lockKey={}, requestId={}, expireTime={}, error={}", 
                lockKey, requestId, expireTime, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁键
     * @param requestId 请求标识
     * @return true-释放成功，false-释放失败
     */
    public boolean releaseLock(String lockKey, String requestId) {
        if (StrUtils.isBlank(lockKey) || StrUtils.isBlank(requestId)) {
            return false;
        }
        try {
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(UNLOCK_SCRIPT, Long.class);
            Long result = redisTemplate.execute(redisScript, 
                Collections.singletonList(lockKey), requestId);
            return result != null && result > 0;
        } catch (Exception e) {
            log.error("释放分布式锁失败: lockKey={}, requestId={}, error={}", 
                lockKey, requestId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 续期分布式锁
     *
     * @param lockKey    锁键
     * @param requestId  请求标识
     * @param expireTime 续期时间（秒）
     * @return true-续期成功，false-续期失败
     */
    public boolean renewLock(String lockKey, String requestId, long expireTime) {
        if (StrUtils.isBlank(lockKey) || StrUtils.isBlank(requestId) || expireTime <= 0) {
            return false;
        }
        try {
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RENEW_SCRIPT, Long.class);
            Long result = redisTemplate.execute(redisScript, 
                Collections.singletonList(lockKey), requestId, String.valueOf(expireTime));
            return result != null && result > 0;
        } catch (Exception e) {
            log.error("续期分布式锁失败: lockKey={}, requestId={}, expireTime={}, error={}", 
                lockKey, requestId, expireTime, e.getMessage(), e);
            return false;
        }
    }
}
