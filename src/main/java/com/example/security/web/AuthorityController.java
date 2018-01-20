package com.example.security.web;

import com.example.security.service.AuthorityService;
import com.example.security.web.HandlerSnakeCaseGetMethodArgumentResolver.SnakeCaseField;
import com.example.security.web.req.AuthorityQueryReq;
import com.example.security.web.req.SaveAuthorityReq;
import com.example.security.web.resp.AuthorityResp;
import com.example.security.web.resp.DefaultPageResp;
import com.example.security.web.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 权限控制器
 */
@RestController
@RequestMapping("/v1/authorities/")
@Validated
@PreAuthorize("hasAuthority('AUTHORITY_ADMIN')")
@Api(description = "权限控制器")
public class AuthorityController {

  @Autowired
  private AuthorityService authorityService;

  @PostMapping("/authority")
  @ApiOperation("保存权限")
  public R<Long> save(@Valid @RequestBody SaveAuthorityReq req) {
    return R.ok(authorityService.save(req));
  }

  @PostMapping("/authority/{authorityId:\\d{1,19}}")
  @ApiOperation("更新权限")
  public R<Void> update(@PathVariable("authorityId") Long authorityId, @Valid @RequestBody SaveAuthorityReq req) {
    authorityService.update(authorityId, req);
    return R.ok();
  }

  @PostMapping("/authority/{authorityId:\\d{1,19}}/delete")
  @ApiOperation("删除权限")
  public R<Void> delete(@PathVariable("authorityId") Long authorityId) {
    authorityService.delete(authorityId);
    return R.ok();
  }

  @GetMapping("/authority/query")
  @ApiOperation("自定义查询权限")
  public R<DefaultPageResp<AuthorityResp>> findByQuery(@Valid @SnakeCaseField AuthorityQueryReq req) {
    return R.ok(authorityService.findByQuery(req));
  }
}
