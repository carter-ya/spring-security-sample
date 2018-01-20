package com.example.security.web.req;

import com.example.security.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Data
@ApiModel("保存用户")
public class SaveUserReq {

  @ApiModelProperty("用户名")
  @NotEmpty(message = "用户名不能为空")
  private String username;
  @ApiModelProperty("名字")
  @NotEmpty(message = "名字不能为空")
  private String name;
  @ApiModelProperty("密码")
  @NotEmpty(message = "密码不能为空")
  private String password;

  public User toUser(String salt) {
    return new User(username, name, BCrypt.hashpw(password, salt));
  }

}
