package com.example.security.entity;

import com.example.security.enums.CommonState;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色权限映射表
 */
@Table(name = "t_role_authority")
@Entity(name = "RoleAuthority")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RoleAuthority extends AbstractEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 角色ID
   */
  @Column(name = "f_role_id", nullable = false)
  private Long roleId;

  /**
   * 权限ID
   */
  @Column(name = "f_authority_id", nullable = false)
  private Long authorityId;

  /**
   * 状态 1 valid 有效 2 invalid 删除
   */
  @Column(name = "f_state", nullable = false)
  private Integer state = CommonState.VALID_CODE;

  public RoleAuthority(Long roleId, Long authorityId) {
    this.roleId = roleId;
    this.authorityId = authorityId;
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