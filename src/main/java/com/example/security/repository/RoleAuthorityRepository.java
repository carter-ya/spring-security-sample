package com.example.security.repository;

import com.example.security.entity.RoleAuthority;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, Long> {

  RoleAuthority findByRoleIdAndAuthorityId(Long roleId, Long authorityId);

  List<RoleAuthority> findByRoleIdAndAuthorityIdIn(Long roleId, List<Long> authorityIds);

  List<RoleAuthority> findByRoleId(Long roleId);

  List<RoleAuthority> findByRoleIdAndState(Long roleId, Integer state);

  List<RoleAuthority> findByRoleIdInAndState(Collection<Long> roleId, Integer state);
}
