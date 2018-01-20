package com.example.security.web.req;

import com.example.security.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ApiModel("保存角色")
public class SaveRoleReq {

  @ApiModelProperty("角色英文名称")
  @NotEmpty(message = "角色英文名称不能为空")
  private String roleNameEn;
  @ApiModelProperty("角色中文名称")
  @NotEmpty(message = "角色中文名称不能为空")
  private String roleNameCn;

  public Role toRole() {
    return new Role(roleNameEn, roleNameCn);
  }
}
