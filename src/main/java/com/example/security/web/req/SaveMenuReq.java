package com.example.security.web.req;

import com.example.security.entity.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ApiModel("保存菜单")
public class SaveMenuReq {

  @ApiModelProperty("父菜单ID，默认为0")
  @NotNull(message = "父菜单ID不能为空")
  private Long parentId = 0L;
  @ApiModelProperty("菜单名称")
  @NotEmpty(message = "菜单名称不能为空")
  private String menuName;
  @ApiModelProperty("菜单链接，可以为空")
  private String url = "";
  @ApiModelProperty("图标，可以为空")
  private String icon = "";

  public Menu toMenu() {
    return new Menu(parentId, menuName, url, icon);
  }
}
