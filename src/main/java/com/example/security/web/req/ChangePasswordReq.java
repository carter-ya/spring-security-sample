package com.example.security.web.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ApiModel("修改密码")
public class ChangePasswordReq {

  @ApiModelProperty("原始密码")
  @NotEmpty(message = "原始密码不能为空")
  private String rawPassword;
  @ApiModelProperty("新密码")
  @NotEmpty(message = "新密码不能为空")
  private String newPassword;

  public ResetPasswordReq toResetPasswordReq() {
    ResetPasswordReq req = new ResetPasswordReq();
    req.setPassword(newPassword);
    return req;
  }
}
