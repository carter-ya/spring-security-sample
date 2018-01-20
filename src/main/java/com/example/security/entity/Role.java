package com.example.security.entity;

import com.example.security.enums.CommonState;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色表
 */
@Table(name = "t_role")
@Entity(name = "Role")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Role extends AbstractEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 角色英文名称
   */
  @Column(name = "f_role_name_en", nullable = false)
  private String roleNameEn;

  /**
   * 角色中文名称
   */
  @Column(name = "f_role_name_cn", nullable = false)
  private String roleNameCn;

  /**
   * 角色状态 1 valid 有效 2 invalid 删除
   */
  @Column(name = "f_state", nullable = false)
  private Integer state = CommonState.VALID_CODE;

  public Role(String roleNameEn, String roleNameCn) {
    this.roleNameEn = roleNameEn;
    this.roleNameCn = roleNameCn;
  }

  /**
   * 角色英文名称
   */
  public String getRoleNameEn() {
    return roleNameEn;
  }

  /**
   * 角色英文名称
   */
  public void setRoleNameEn(String roleNameEn) {
    this.roleNameEn = roleNameEn;
  }

  /**
   * 角色中文名称
   */
  public String getRoleNameCn() {
    return roleNameCn;
  }

  /**
   * 角色中文名称
   */
  public void setRoleNameCn(String roleNameCn) {
    this.roleNameCn = roleNameCn;
  }

  /**
   * 角色状态 1 valid 有效 2 invalid 删除
   */
  public Integer getState() {
    return state;
  }

  /**
   * 角色状态 1 valid 有效 2 invalid 删除
   */
  public void setState(Integer state) {
    this.state = state;
  }

}