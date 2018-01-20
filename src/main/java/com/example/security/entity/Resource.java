package com.example.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源表
 */
@Table(name = "t_resource")
@Entity(name = "Resource")
@Data
@EqualsAndHashCode(callSuper = true)
public class Resource extends AbstractEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 资源名称
   */
  @Column(name = "f_resource_name", nullable = false)
  private String resourceName = "";

  /**
   * 菜单连接
   */
  @Column(name = "f_url", nullable = false)
  private String url;

  /**
   * 角色状态 1 valid 有效 2 frozen 冻结 3 invalid 删除
   */
  @Column(name = "f_state", nullable = false)
  private Integer state = 1;

  /**
   * 资源名称
   */
  public String getResourceName() {
    return resourceName;
  }

  /**
   * 资源名称
   */
  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  /**
   * 菜单连接
   */
  public String getUrl() {
    return url;
  }

  /**
   * 菜单连接
   */
  public void setUrl(String url) {
    this.url = url;
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