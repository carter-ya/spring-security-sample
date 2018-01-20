package com.example.security.entity;

import com.example.security.enums.UserState;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户表
 */
@Table(name = "t_user")
@Entity(name = "User")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends AbstractEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 用户名
   */
  @Column(name = "f_username", nullable = false)
  private String username;

  /**
   * 姓名
   */
  @Column(name = "f_name", nullable = false)
  private String name = "";

  /**
   * 密码
   */
  @Column(name = "f_password", nullable = false)
  private String password;

  /**
   * 用户状态 1 valid 有效 2 locked 锁定 3 invalid 无效
   */
  @Column(name = "f_state", nullable = false)
  private Integer state = UserState.VALID.getCode();

  public User(String username, String name, String password) {
    this.username = username;
    this.name = name;
    this.password = password;
  }

  /**
   * 用户名
   */
  public String getUsername() {
    return username;
  }

  /**
   * 用户名
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * 姓名
   */
  public String getName() {
    return name;
  }

  /**
   * 姓名
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 密码
   */
  public String getPassword() {
    return password;
  }

  /**
   * 密码
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * 用户状态 1 valid 有效 2 locked 冻结 3 invalid 无效
   */
  public Integer getState() {
    return state;
  }

  /**
   * 用户状态 1 valid 有效 2 locked 冻结 3 invalid 无效
   */
  public void setState(Integer state) {
    this.state = state;
  }

}