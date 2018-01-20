package com.example.security.entity;

import com.example.security.enums.CommonState;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 权限表
 */
@Table(name = "t_authority")
@Entity(name = "Authority")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Authority extends AbstractEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 权限类型 1 menu 菜单; 2 resource 资源
   */
  @Column(name = "f_type", nullable = false)
  private Integer type;

  /**
   * 关联表主键
   */
  @Column(name = "f_link_id", nullable = false)
  private Long linkId;

  /**
   * 权限状态 1 valid 有效 2 invalid 删除
   */
  @Column(name = "f_state", nullable = false)
  private Integer state = CommonState.VALID_CODE;

  public Authority(Integer type, Long linkId) {
    this.type = type;
    this.linkId = linkId;
  }

  /**
   * 权限类型 1 menu 菜单; 2 resource 资源
   */
  public Integer getType() {
    return type;
  }

  /**
   * 权限类型 1 menu 菜单; 2 resource 资源
   */
  public void setType(Integer type) {
    this.type = type;
  }

  /**
   * 关联表主键
   */
  public Long getLinkId() {
    return linkId;
  }

  /**
   * 关联表主键
   */
  public void setLinkId(Long linkId) {
    this.linkId = linkId;
  }

  /**
   * 权限状态 1 valid 有效 2 invalid 删除
   */
  public Integer getState() {
    return state;
  }

  /**
   * 权限状态 1 valid 有效 2 invalid 删除
   */
  public void setState(Integer state) {
    this.state = state;
  }

}