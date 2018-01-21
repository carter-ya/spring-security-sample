package com.example.security.repository;

import com.example.security.entity.Resource;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

  Resource findByResourceNameAndUrl(String resourceName, String url);

  List<Resource> findByState(Integer state);

  long countByIdIn(Collection<Long> ids);
}
