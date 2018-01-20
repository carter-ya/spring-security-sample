package com.example.security.web.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ApiModel("重置密码")
public class ResetPasswordReq {

  @ApiModelProperty("新密码")
  @NotEmpty(message = "密码不能为空")
  private String password;
}
