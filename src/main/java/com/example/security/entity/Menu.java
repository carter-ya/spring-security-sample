package com.example.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 菜单表
 */
@Table(name = "t_menu")
@Entity(name = "Menu")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Menu extends AbstractEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 父级菜单ID
   */
  @Column(name = "f_parent_id", nullable = false)
  private Long parentId = 0L;

  /**
   * 菜单名称
   */
  @Column(name = "f_menu_name", nullable = false)
  private String menuName;

  /**
   * 菜单连接
   */
  @Column(name = "f_url", nullable = false)
  private String url;

  /**
   * 图标
   */
  @Column(name = "f_icon", nullable = false)
  private String icon = "";

  /**
   * 菜单状态 1 valid 有效 2 invalid 删除
   */
  @Column(name = "f_state", nullable = false)
  private Integer state = 1;

  public Menu(String menuName, String url, String icon) {
    this(0L, menuName, url, icon);
  }

  public Menu(Long parentId, String menuName, String url, String icon) {
    this.parentId = parentId;
    this.menuName = menuName;
    this.url = url;
    this.icon = icon;
  }

  /**
   * 父级菜单ID
   */
  public Long getParentId() {
    return parentId;
  }

  /**
   * 父级菜单ID
   */
  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  /**
   * 菜单名称
   */
  public String getMenuName() {
    return menuName;
  }

  /**
   * 菜单名称
   */
  public void setMenuName(String menuName) {
    this.menuName = menuName;
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
   * 菜单状态 1 valid 有效 2 invalid 删除
   */
  public Integer getState() {
    return state;
  }

  /**
   * 菜单状态 1 valid 有效 2 invalid 删除
   */
  public void setState(Integer state) {
    this.state = state;
  }
}