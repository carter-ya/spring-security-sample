package com.example.security.web.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import lombok.Data;

@Data
@ApiModel("自定义查询基类")
public abstract class AbstractQueryReq {

  @ApiModelProperty("页码，从1开始，默认为1")
  @Min(value = 1, message = "页码不能小于1")
  private int page = 1;
  @ApiModelProperty("查询数量，从1开始，默认为40")
  @Min(value = 1, message = "查询数量不能小于1")
  private int pageSize = 40;

  /**
   * 获取当前页码-1后的值
   */
  public int getZeroBasedPage() {
    return page - 1;
  }

  public String toLike(String value) {
    return "%" + value + "%";
  }
}
