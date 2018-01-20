package com.example.security.service;

import com.example.security.enums.AuthorityType;
import com.example.security.repository.MenuRepository;
import com.example.security.repository.ResourceRepository;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 默认的权限类型服务
 */
@Service
@Primary
public class DefaultAuthorityTypeService implements AuthorityTypeService {

  @Autowired
  private MenuRepository menuRepository;
  @Autowired
  private ResourceRepository resourceRepository;

  @Override
  public boolean existsAuthorityLinkId(AuthorityType type, Long linkId) {
    switch (type) {
      case MENU:
        return menuRepository.exists(linkId);
      case RESOURCE:
        return resourceRepository.exists(linkId);
      default:
        throw new IllegalStateException();
    }
  }

  @Override
  public boolean existsAuthorityLinkIds(AuthorityType type, Set<Long> linkIds) {
    switch (type) {
      case MENU:
        return linkIds.size() == menuRepository.countByIdIn(linkIds);
      case RESOURCE:
        return linkIds.size() == resourceRepository.countByIdIn(linkIds);
      default:
        throw new IllegalStateException();
    }
  }
}
