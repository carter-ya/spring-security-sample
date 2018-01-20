package com.example.security.web.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("自定义用户查询")
public class UserQueryReq extends AbstractQueryReq {

  @ApiModelProperty("用户名. 可以为空")
  private String username;
  @ApiModelProperty("名字. 可以为空")
  private String name;
  @ApiModelProperty("用户状态. 可以为空 valid 有效;locked 锁定;invalid 无效")
  private String state;
}
