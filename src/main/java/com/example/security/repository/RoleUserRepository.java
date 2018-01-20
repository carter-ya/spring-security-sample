package com.example.security.repository;

import com.example.security.entity.RoleUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {

  List<RoleUser> findByUserIdAndState(Long userId, Integer state);

  List<RoleUser> findByUserIdAndRoleIdIn(Long userId, List<Long> roleIds);

  List<RoleUser> findByUserId(Long userId);

  RoleUser findByUserIdAndRoleId(Long userId, Long roleId);
}
