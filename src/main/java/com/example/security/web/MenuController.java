package com.example.security.web;

import com.example.security.service.MenuService;
import com.example.security.web.req.SaveMenuReq;
import com.example.security.web.resp.R;
import com.example.security.web.resp.TreeMenuResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单控制器
 */
@RestController
@RequestMapping("/v1/menus")
@Validated
@PreAuthorize("hasAuthority('MENU_ADMIN')")
@Api(description = "菜单控制器")
public class MenuController {

  @Autowired
  private MenuService menuService;

  @PostMapping("/menu")
  @ApiOperation("保存菜单")
  public R<Long> save(@Valid @RequestBody SaveMenuReq req) {
    return R.ok(menuService.save(req));
  }

  @PostMapping("/menu/{menuId:\\d{1,19}}")
  @ApiOperation("更新菜单")
  public R<Void> update(@PathVariable("menuId") Long menuId, @Valid @RequestBody SaveMenuReq req) {
    menuService.update(menuId, req);
    return R.ok();
  }

  @PostMapping("/menu/{menuId:\\d{1,19}}/delete")
  @ApiOperation("删除菜单")
  public R<Void> delete(@PathVariable("menuId") Long menuId) {
    menuService.delete(menuId);
    return R.ok();
  }

  @GetMapping("/menu/tree")
  @ApiOperation("获取所有有效的树形菜单")
  public R<List<TreeMenuResp>> treeMenu() {
    return R.ok(menuService.treeMenu());
  }

  @GetMapping("/menu/tree/user/{userId:\\d{1,19}}")
  @ApiOperation("获取指定用户所有有效的树形菜单")
  public R<List<TreeMenuResp>> treeMenu(@PathVariable("userId") Long userId) {
    return R.ok(menuService.treeMenu(userId));
  }
}
