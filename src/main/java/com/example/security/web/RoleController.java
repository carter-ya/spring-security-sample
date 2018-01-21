package com.example.security.web;

import com.example.security.service.RoleService;
import com.example.security.web.HandlerSnakeCaseGetMethodArgumentResolver.SnakeCaseField;
import com.example.security.web.req.FindRoleAuthorityReq;
import com.example.security.web.req.IdListReq;
import com.example.security.web.req.SaveRoleReq;
import com.example.security.web.resp.R;
import com.example.security.web.resp.RoleResp;
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
 * 角色控制器
 */
@RestController
@RequestMapping("/v1/roles/")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Validated
@Api(description = "角色控制器")
public class RoleController {

  @Autowired
  private RoleService roleService;

  @PostMapping("/role")
  @ApiOperation("保存角色")
  public R<Long> save(@Valid @RequestBody SaveRoleReq req) {
    return R.ok(roleService.save(req));
  }

  @PostMapping("/role/{roleId:\\d{1,19}}")
  @ApiOperation("更新角色")
  public R<Void> update(@PathVariable("roleId") Long roleId, @Valid @RequestBody SaveRoleReq req) {
    roleService.update(roleId, req);
    return R.ok();
  }

  @PostMapping("/role/{roleId:\\d{1,19}}/delete")
  @ApiOperation("删除角色")
  public R<Void> delete(@PathVariable("roleId") Long roleId) {
    roleService.delete(roleId);
    return R.ok();
  }

  @GetMapping("/role/valid")
  @PreAuthorize("isAuthenticated()")
  @ApiOperation("获取当前用户所有有效的角色")
  public R<List<RoleResp>> validRoles() {
    return R.ok(roleService.findValidRoles());
  }

  @PostMapping("/role/{roleId:\\d{1,19}}/authorities/authority/{authorityId:\\d{1,19}}")
  @ApiOperation("授权角色权限")
  public R<Void> addAuthority(@PathVariable("roleId") Long roleId, @PathVariable("authorityId") Long authorityId) {
    roleService.addAuthority(roleId, authorityId);
    return R.ok();
  }

  @PostMapping("/role/{roleId:\\d{1,19}}/authorities/remove")
  @ApiOperation("移除角色授权")
  public R<Void> removeAuthorities(@PathVariable("roleId") Long roleId, @Valid @RequestBody IdListReq req) {
    roleService.removeAuthorities(roleId, req.getIds());
    return R.ok();
  }

  @PostMapping("/role/{roleId:\\d{1,19}}/authorites/batch_update")
  @ApiOperation("批量授权/移除角色授权")
  public R<Void> batchUpdateAuthorities(@PathVariable("roleId") Long roleId, @Valid @RequestBody IdListReq req) {
    roleService.batchUpdateAuthorities(roleId, req.getIds());
    return R.ok();
  }

  @GetMapping("/role/{roleId:\\d{1,19}}/authorities")
  @ApiOperation("获取指定角色，指定权限类型的所有权限ID")
  public R<List<Long>> findByAuthorityType(@PathVariable("roleId") Long roleId,
      @Valid @SnakeCaseField FindRoleAuthorityReq req) {
    return R.ok(roleService.findByAuthorityType(roleId, req));
  }
}
