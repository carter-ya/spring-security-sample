package com.example.security.web.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ApiModel("保存资源")
public class SaveResourceReq {

  @NotEmpty(message = "资源名称不能为空")
  @ApiModelProperty("资源名称")
  private String resourceName;
  @ApiModelProperty("资源链接")
  private String url = "";
}
