package com.example.security.web.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ApiModel("查找角色授权")
public class FindRoleAuthorityReq {

  @ApiModelProperty("权限类型. menu 菜单;resource 资源")
  @NotEmpty(message = "权限类型不能为空")
  private String type;
}
