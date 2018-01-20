package com.example.security.web.req;

import com.example.security.entity.Authority;
import com.example.security.enums.AuthorityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("保存权限")
public class SaveAuthorityReq {

  @ApiModelProperty("权限类型. menu 菜单;resource 资源")
  @NotEmpty
  private String type;
  @ApiModelProperty("关联权限ID")
  @NotNull
  private Long linkId;

  public Authority toAuthority() {
    return new Authority(AuthorityType.find(type).getCode(), linkId);
  }
}
