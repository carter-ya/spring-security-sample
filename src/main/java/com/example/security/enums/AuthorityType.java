package com.example.security.enums;

/**
 * 权限类型
 */
public enum AuthorityType {
  MENU(1, "menu"),
  RESOURCE(2, "resource");
  private final Integer code;
  private final String value;

  AuthorityType(Integer code, String value) {
    this.code = code;
    this.value = value;
  }

  public Integer getCode() {
    return code;
  }

  public String getValue() {
    return value;
  }

  public static AuthorityType find(Integer code) {
    for (AuthorityType codeEnum : values()) {
      if (codeEnum.getCode().equals(code)) {
        return codeEnum;
      }
    }
    throw new IllegalArgumentException("无法识别的状态码:" + code);
  }

  public static AuthorityType find(String value) {
    for (AuthorityType codeEnum : values()) {
      if (codeEnum.getValue().equals(value)) {
        return codeEnum;
      }
    }
    throw new IllegalArgumentException("无法识别的状态码:" + value);
  }
}
