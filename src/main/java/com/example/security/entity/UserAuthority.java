package com.example.security.entity;

import com.example.security.enums.CommonState;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户权限映射表
 */
@Table(name = "t_user_authority")
@Entity(name = "UserAuthority")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAuthority extends AbstractEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 用户ID
   */
  @Column(name = "f_user_id", nullable = false)
  private Long userId;

  /**
   * 权限ID
   */
  @Column(name = "f_authority_id", nullable = false)
  private Long authorityId;

  /**
   * 状态 1 valid 有效 2 invalid 删除
   */
  @Column(name = "f_state", nullable = false)
  private Integer state = CommonState.VALID.getCode();

  /**
   * 用户ID
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * 用户ID
   */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  /**
   * 权限ID
   */
  public Long getAuthorityId() {
    return authorityId;
  }

  /**
   * 权限ID
   */
  public void setAuthorityId(Long authorityId) {
    this.authorityId = authorityId;
  }

  /**
   * 状态 1 valid 有效 2 invalid 删除
   */
  public Integer getState() {
    return state;
  }

  /**
   * 状态 1 valid 有效 2 invalid 删除
   */
  public void setState(Integer state) {
    this.state = state;
  }

}