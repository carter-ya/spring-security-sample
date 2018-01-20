package com.example.security.web.resp;

import com.example.security.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("角色信息")
public class RoleResp {

  @ApiModelProperty("角色ID")
  private Long id;
  @ApiModelProperty("角色英文名")
  private String roleNameEn;
  @ApiModelProperty("角色中文名")
  private String roleNameCn;

  public static RoleResp from(Role role) {
    RoleResp resp = new RoleResp();
    BeanUtils.copyProperties(role, resp);
    return resp;
  }
}
