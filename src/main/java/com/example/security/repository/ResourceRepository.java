package com.example.security.repository;

import com.example.security.entity.Resource;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

  long countByIdIn(Collection<Long> ids);
}
