package com.example.security.web.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel("API响应")
public class R<T> {

  private static final String SUCCESS_STATUS = "success";
  private static final String ERROR_STATUS = "error";
  @ApiModelProperty("响应状态")
  private String status;
  @ApiModelProperty("错误码")
  private String errCode;
  @ApiModelProperty("错误信息")
  private String msg;
  @ApiModelProperty("响应结果")
  private T data;

  private R(String status, String errCode, String msg, T data) {
    this.status = status;
    this.errCode = errCode;
    this.msg = msg;
    this.data = data;
  }

  public static <T> R<T> ok() {
    return ok(null);
  }

  public static <T> R<T> ok(T data) {
    return new R<>(SUCCESS_STATUS, "", "", data);
  }

  public static <T> R<T> error(String errCode, String msg) {
    return new R<>(ERROR_STATUS, errCode, msg, null);
  }
}
