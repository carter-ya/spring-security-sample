package com.example.security.service;

import com.example.security.enums.AuthorityType;
import java.util.Set;

/**
 * 授权类型服务
 */
public interface AuthorityTypeService {

  /**
   * 返回是否存在指定类型的权限类型主键
   *
   * @param type 权限类型
   * @param linkId 主键
   */
  boolean existsAuthorityLinkId(AuthorityType type, Long linkId);

  /**
   * 返回是否全部存在指定类型的权限类型主键
   *
   * @param type 权限类型
   * @param linkIds 主键列表
   */
  boolean existsAuthorityLinkIds(AuthorityType type, Set<Long> linkIds);
}
