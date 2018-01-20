package com.example.security.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Data;

@Data
@MappedSuperclass
public class AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * 自增主键
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(insertable = false, name = "f_id", nullable = false)
  private Long id;

  /**
   * 乐观锁版本号
   */
  @Column(name = "f_version", nullable = false)
  private Long version = 1L;

  /**
   * 创建时间
   */
  @Column(name = "f_created_at", nullable = false)
  private Long createdAt;

  /**
   * 更新时间
   */
  @Column(name = "f_updated_at", nullable = false)
  private Long updatedAt;

  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = System.currentTimeMillis();
    }
    updatedAt = createdAt;
  }

  @PreUpdate
  public void perUpdate() {
    updatedAt = System.currentTimeMillis();
  }
}
