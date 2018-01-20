package com.example.security.web;

import com.example.security.service.UserService;
import com.example.security.web.HandlerRequireOperatorMethodArgumentResolver.Operator;
import com.example.security.web.HandlerRequireOperatorMethodArgumentResolver.RequireOperator;
import com.example.security.web.HandlerSnakeCaseGetMethodArgumentResolver.SnakeCaseField;
import com.example.security.web.req.ChangePasswordReq;
import com.example.security.web.req.IdListReq;
import com.example.security.web.req.ResetPasswordReq;
import com.example.security.web.req.SaveUserReq;
import com.example.security.web.req.UserLoginReq;
import com.example.security.web.req.UserQueryReq;
import com.example.security.web.resp.DefaultPageResp;
import com.example.security.web.resp.R;
import com.example.security.web.resp.RoleResp;
import com.example.security.web.resp.UserResp;
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
 * 用户控制器
 */
@RestController
@RequestMapping("/v1/users/")
@PreAuthorize("hasAuthority('USER_ADMIN')")
@Validated
@Api(description = "用户控制器")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/user")
  @ApiOperation("保存用户")
  public R<Long> save(@Valid @RequestBody SaveUserReq req) {
    return R.ok(userService.save(req));
  }

  @PostMapping("/user/{userId:\\d{1,19}}/lock")
  @ApiOperation("锁定用户账号")
  public R<Void> lock(@PathVariable("userId") Long userId) {
    userService.lock(userId);
    return R.ok();
  }

  @PostMapping("/user/{userId:\\d{1,19}}/delete")
  @ApiOperation("删除用户账号")
  public R<Void> delete(@PathVariable("userId") Long userId) {
    userService.delete(userId);
    return R.ok();
  }

  @PostMapping("/user/{userId:\\d{1,19}}/reset_password")
  @ApiOperation("重置用户密码")
  public R<Void> resetPassword(@PathVariable("userId") Long userId, @Valid @RequestBody ResetPasswordReq req) {
    userService.resetPassword(userId, req);
    return R.ok();
  }

  @PostMapping("/user/{userId:\\d{1,19}}/change_password")
  @ApiOperation("修改用户密码")
  @PreAuthorize("isAuthenticated()")
  public R<Void> changePassword(@PathVariable("userId") Long userId, @Valid @RequestBody ChangePasswordReq req) {
    userService.changePassword(userId, req);
    return R.ok();
  }

  @GetMapping("/user/query")
  @ApiOperation("自定义查询用户列表")
  public R<DefaultPageResp<UserResp>> findByQuery(@Valid @SnakeCaseField UserQueryReq req) {
    return R.ok(userService.findByQuery(req));
  }

  @PostMapping("/user/login")
  @PreAuthorize("permitAll")
  @ApiOperation("用户登录")
  public R<String> login(@Valid @RequestBody UserLoginReq req) {
    return R.ok(userService.login(req));
  }

  // 操作用户角色

  @PostMapping("/user/{userId:\\d{1,19}}/roles")
  @ApiOperation("批量授予用户角色")
  public R<Void> addRoles(@PathVariable("userId") Long userId, @Valid @RequestBody IdListReq req) {
    userService.addRoles(userId, req.getIds());
    return R.ok();
  }

  @PostMapping("/user/{userId:\\d{1,19}}/roles/remove")
  @ApiOperation("批量移除用户角色")
  public R<Void> removeRoles(@PathVariable("userId") Long userId, @Valid @RequestBody IdListReq req) {
    userService.removeRoles(userId, req.getIds());
    return R.ok();
  }

  @GetMapping("/user/roles")
  @ApiOperation("查询当前登录用户的所有有效角色")
  @PreAuthorize("isAuthenticated()")
  public R<List<RoleResp>> roles(@RequireOperator Operator operator) {
    return R.ok(userService.findRoleByUserId(operator.getUserId()));
  }

  @GetMapping("/user/{userId:\\d{1,19}}/roles")
  @ApiOperation("查询指定用户的所有有效角色")
  public R<List<RoleResp>> roles(@PathVariable("userId") Long userId) {
    return R.ok(userService.findRoleByUserId(userId));
  }
}
