package com.example.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色用户映射表
 */
@Table(name = "t_role_user")
@Entity(name = "RoleUser")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RoleUser extends AbstractEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 角色ID
   */
  @Column(name = "f_role_id", nullable = false)
  private Long roleId;

  /**
   * 用户ID
   */
  @Column(name = "f_user_id", nullable = false)
  private Long userId;

  /**
   * 角色状态 1 valid 有效 2 frozen 冻结 3 invalid 删除
   */
  @Column(name = "f_state", nullable = false)
  private Integer state = 1;

  public RoleUser(Long roleId, Long userId) {
    this.roleId = roleId;
    this.userId = userId;
  }

  /**
   * 角色ID
   */
  public Long getRoleId() {
    return roleId;
  }

  /**
   * 角色ID
   */
  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

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
   * 角色状态 1 valid 有效 2 frozen 冻结 3 invalid 删除
   */
  public Integer getState() {
    return state;
  }

  /**
   * 角色状态 1 valid 有效 2 frozen 冻结 3 invalid 删除
   */
  public void setState(Integer state) {
    this.state = state;
  }

}