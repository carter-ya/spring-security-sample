package com.example.security.web.resp;

import com.example.security.entity.User;
import com.example.security.enums.UserState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("用户信息")
public class UserResp {

  @ApiModelProperty("用户ID")
  private Long id;
  @ApiModelProperty("用户名")
  private String username;
  @ApiModelProperty("名字")
  private String name;
  @ApiModelProperty("用户状态")
  private String state;

  public static UserResp from(User user) {
    UserResp resp = new UserResp();
    BeanUtils.copyProperties(user, resp, "state");
    resp.setState(UserState.find(user.getState()).getValue());
    return resp;
  }
}
