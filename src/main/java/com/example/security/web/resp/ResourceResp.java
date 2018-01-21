package com.example.security.web.resp;

import com.example.security.entity.Resource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("资源")
public class ResourceResp {

  @ApiModelProperty("资源ID")
  private Long id;
  @ApiModelProperty("资源名称")
  private String resourceName;
  @ApiModelProperty("资源链接")
  private String url;

  public static ResourceResp from(Resource resource) {
    ResourceResp resp = new ResourceResp();
    BeanUtils.copyProperties(resource, resp);
    return resp;
  }
}
