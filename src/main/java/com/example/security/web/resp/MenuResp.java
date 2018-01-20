package com.example.security.web.resp;

import com.example.security.entity.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("菜单信息")
public class MenuResp {

  @ApiModelProperty("菜单ID")
  private Long id;
  @ApiModelProperty("父级菜单ID")
  private Long parentId;
  @ApiModelProperty("菜单名称")
  private String menuName;
  @ApiModelProperty("菜单链接")
  private String url;
  @ApiModelProperty("菜单图标")
  private String icon;

  public static MenuResp from(Menu menu) {
    MenuResp resp = new MenuResp();
    BeanUtils.copyProperties(menu, resp);
    return resp;
  }
}
