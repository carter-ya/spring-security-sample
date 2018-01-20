package com.example.security.web.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

@Data
@ApiModel("ID列表查询")
public class IdListReq {

  @ApiModelProperty("id列表")
  private List<Long> ids;
}
