package com.example.security.web.resp;

import com.example.security.entity.Authority;
import com.example.security.enums.AuthorityType;
import com.example.security.enums.CommonState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("授权信息")
public class AuthorityResp {

  @ApiModelProperty("授权ID")
  private Long id;
  @ApiModelProperty("授权类型")
  private String type;
  @ApiModelProperty("授权类型ID")
  private Long linkId;
  @ApiModelProperty("授权状态")
  private String state;

  public static AuthorityResp from(Authority authority) {
    AuthorityResp resp = new AuthorityResp();
    BeanUtils.copyProperties(authority, resp, "type", "state");
    resp.setType(AuthorityType.find(authority.getType()).getValue());
    resp.setState(CommonState.find(authority.getState()).getValue());
    return resp;
  }
}
