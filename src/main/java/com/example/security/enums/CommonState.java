package com.example.security.enums;

public enum CommonState {
  VALID(1, "valid"),
  INVALID(2, "invalid");
  private final Integer code;
  private final String value;
  public static final Integer VALID_CODE = 1;
  public static final Integer INVALID_CODE = 2;

  CommonState(Integer code, String value) {
    this.code = code;
    this.value = value;
  }

  public Integer getCode() {
    return code;
  }

  public String getValue() {
    return value;
  }

  public static CommonState find(Integer code) {
    for (CommonState codeEnum : values()) {
      if (codeEnum.getCode().equals(code)) {
        return codeEnum;
      }
    }
    throw new IllegalArgumentException("无法识别的状态码:" + code);
  }

  public static CommonState find(String value) {
    for (CommonState codeEnum : values()) {
      if (codeEnum.getValue().equals(value)) {
        return codeEnum;
      }
    }
    throw new IllegalArgumentException("无法识别的状态码:" + value);
  }
}
