package com.example.security.repository;

import com.example.security.entity.Authority;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthorityRepository extends JpaRepository<Authority, Long>, JpaSpecificationExecutor<Authority> {

  Authority findByTypeAndLinkId(Integer type, Long linkId);

  List<Authority> findByTypeAndLinkIdIn(Integer type, Collection<Long> linkIds);

  List<Authority> findByIdInAndType(Collection<Long> ids, Integer type);

  long countByIdIn(List<Long> ids);
}
