package com.example.security.service.transaction;

import com.example.security.entity.RoleAuthority;
import com.example.security.repository.RoleAuthorityRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色事务服务
 */
@Service
@Transactional
public class RoleTransactionService {

  @Autowired
  private RoleAuthorityRepository roleAuthorityRepository;

  public void save(List<RoleAuthority> newRoleAuthorities, List<RoleAuthority> updateRoleAuthorities) {
    if (!newRoleAuthorities.isEmpty()) {
      roleAuthorityRepository.save(newRoleAuthorities);
    }
    if (!updateRoleAuthorities.isEmpty()) {
      roleAuthorityRepository.save(updateRoleAuthorities);
    }
  }
}
