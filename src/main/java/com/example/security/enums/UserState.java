package com.example.security.enums;

public enum UserState {

  VALID(1, "valid"),
  LOCKED(2, "locked"),
  INVALID(3, "invalid");
  private final Integer code;
  private final String value;

  UserState(Integer code, String value) {
    this.code = code;
    this.value = value;
  }

  public Integer getCode() {
    return code;
  }

  public String getValue() {
    return value;
  }

  public static UserState find(Integer code) {
    for (UserState codeEnum : values()) {
      if (codeEnum.getCode().equals(code)) {
        return codeEnum;
      }
    }
    throw new IllegalArgumentException("无法识别的状态码:" + code);
  }

  public static UserState find(String value) {
    for (UserState codeEnum : values()) {
      if (codeEnum.getValue().equals(value)) {
        return codeEnum;
      }
    }
    throw new IllegalArgumentException("无法识别的状态码:" + value);
  }

}
