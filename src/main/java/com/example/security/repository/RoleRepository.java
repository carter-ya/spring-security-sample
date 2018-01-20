package com.example.security.repository;

import com.example.security.entity.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  List<Role> findByIdInAndState(List<Long> ids, Integer state);

  List<Role> findByState(Integer state);
}
