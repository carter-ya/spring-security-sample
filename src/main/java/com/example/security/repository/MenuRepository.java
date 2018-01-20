package com.example.security.repository;

import com.example.security.entity.Menu;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  List<Menu> findByState(Integer state);

  List<Menu> findByIdInAndState(Collection<Long> ids, Integer state);

  long countByIdIn(Collection<Long> ids);
}
