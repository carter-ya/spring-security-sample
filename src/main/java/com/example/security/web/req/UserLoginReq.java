package com.example.security.web.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ApiModel("用户登录")
public class UserLoginReq {

  @ApiModelProperty("用户名")
  @NotEmpty(message = "用户名不能为空")
  private String username;
  @ApiModelProperty("密码")
  @NotEmpty(message = "密码不能为空")
  private String password;
}
