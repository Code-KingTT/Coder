package com.coder.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 基础实体类
 *
 * @author Sunset
 * @date 2025/8/13
 */
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 每次更新记录时自动更新此字段
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     * 记录创建此记录的用户ID
     */
    private Long createBy;

    /**
     * 更新人ID
     * 记录最后更新此记录的用户ID
     */
    private Long updateBy;

    /**
     * 逻辑删除标记
     * 0-未删除，1-已删除
     * 使用@JsonIgnore避免序列化到前端
     */
    @JsonIgnore
    private Integer deleted;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 构造方法
     * 只设置基础默认值
     */
    public BaseEntity() {
        this.deleted = 0;
    }

    /**
     * 设置创建信息
     * 新增数据时必须调用此方法
     *
     * @param createBy 创建人ID
     */
    public void setCreateInfo(Long createBy) {
        LocalDateTime now = LocalDateTime.now();
        this.createBy = createBy;
        this.createTime = now;
        this.updateBy = createBy;
        this.updateTime = now;
    }

    /**
     * 设置更新信息
     * 用于更新时设置更新人和更新时间
     *
     * @param updateBy 更新人ID
     */
    public void setUpdateInfo(Long updateBy) {
        this.updateBy = updateBy;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 判断是否为新记录
     * ID为null则认为是新记录
     *
     * @return true-新记录，false-已存在记录
     */
    public boolean isNew() {
        return this.id == null;
    }

    /**
     * 判断是否已删除
     *
     * @return true-已删除，false-未删除
     */
    public boolean checkDeleted() {
        return Objects.equals(this.deleted, 1);
    }

    /**
     * 标记为删除
     * 逻辑删除，不物理删除数据
     *
     * @param deleteBy 删除人ID
     */
    public void markAsDeleted(Long deleteBy) {
        this.deleted = 1;
        this.updateBy = deleteBy;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 恢复删除
     * 将逻辑删除的记录恢复
     *
     * @param recoverBy 恢复人ID
     */
    public void recover(Long recoverBy) {
        this.deleted = 0;
        this.updateBy = recoverBy;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 预插入处理
     * 数据库插入前的兜底检查，确保必要字段不为null
     * 一般情况下应该先调用setCreateInfo()方法
     */
    public void preInsert() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
        if (this.updateTime == null) {
            this.updateTime = LocalDateTime.now();
        }
        if (this.deleted == null) {
            this.deleted = 0;
        }
    }

    /**
     * 预更新处理
     * 在更新数据库前调用，更新时间戳
     */
    public void preUpdate() {
            this.updateTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BaseEntity that = (BaseEntity) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", deleted=" + deleted +
                ", remark='" + remark + '\'' +
                '}';
    }
}
