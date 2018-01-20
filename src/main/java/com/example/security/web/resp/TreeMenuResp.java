package com.example.security.web.resp;

import com.example.security.entity.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collections;
import java.util.List;
import lombok.Data;

/**
 * 树形菜单结构
 */
@Data
@ApiModel("树形菜单")
public class TreeMenuResp {

  /**
   * 菜单等级，从1开始
   */
  @ApiModelProperty("菜单等级，从1开始")
  private int level;
  /**
   * 当前菜单信息
   */
  @ApiModelProperty("菜单信息")
  private MenuResp menu;
  /**
   * 子菜单列表
   */
  @ApiModelProperty("子菜单列表")
  private List<TreeMenuResp> children = Collections.emptyList();

  public static TreeMenuResp from(int level, Menu menu) {
    TreeMenuResp resp = new TreeMenuResp();
    resp.setLevel(level);
    resp.setMenu(MenuResp.from(menu));
    return resp;
  }
}
