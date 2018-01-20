package com.example.security.web.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@ApiModel("分页结果")
public class DefaultPageResp<T> {

  @ApiModelProperty("分页结果")
  private List<T> content;
  @ApiModelProperty("当前页结果数量")
  private int currentElements;
  @ApiModelProperty("总页数")
  private int totalPages;
  @ApiModelProperty("总元素数量")
  private long totalElements;
  @ApiModelProperty("是否有下一页")
  private boolean hasNext;

  public DefaultPageResp(Page<T> page) {
    this.content = page.getContent();
    this.currentElements = page.getNumberOfElements();
    this.totalPages = page.getTotalPages();
    this.totalElements = page.getTotalElements();
    this.hasNext = page.hasNext();
  }
}
