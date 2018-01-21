package com.example.security.web;

import com.example.security.service.ResourceService;
import com.example.security.web.req.SaveResourceReq;
import com.example.security.web.resp.R;
import com.example.security.web.resp.ResourceResp;
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

@RestController
@RequestMapping("/v1/resources")
@Validated
@Api(description = "资源控制器")
@PreAuthorize("hasAuthority('RESOURCE_ADMIN')")
public class ResourceController {

  @Autowired
  private ResourceService resourceService;

  @PostMapping("/resource")
  @ApiOperation("保存资源")
  public R<Long> save(@Valid @RequestBody SaveResourceReq req) {
    return R.ok(resourceService.save(req));
  }

  @PostMapping("/resource/{resourceId:\\d{1,19}}")
  @ApiOperation("更新资源")
  public R<Void> update(@PathVariable("resourceId") Long resourceId, @Valid @RequestBody SaveResourceReq req) {
    resourceService.update(resourceId, req);
    return R.ok();
  }

  @PostMapping("/resource/{resourceId:\\d{1,19}}/delete")
  @ApiOperation("删除资源")
  public R<Void> delete(@PathVariable("resourceId") Long resourceId) {
    resourceService.delete(resourceId);
    return R.ok();
  }

  @GetMapping("/resource/{resourceId:\\d{1,19}}")
  @ApiOperation("根据ID获取资源")
  public R<ResourceResp> findById(@PathVariable("resourceId") Long resourceId) {
    return R.ok(resourceService.findById(resourceId));
  }

  @GetMapping("/valid")
  @ApiOperation("获取所有有效的资源")
  public R<List<ResourceResp>> findValidResources() {
    return R.ok(resourceService.findValidResources());
  }

}
