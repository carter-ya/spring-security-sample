package com.example.security.web.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("权限自定义查询")
public class AuthorityQueryReq extends AbstractQueryReq {

  @ApiModelProperty("权限类型，为空则不限. menu 菜单;resource 资源;")
  private String type;
  @ApiModelProperty("关联权限ID，为空则不限")
  private Long linkId;
}
