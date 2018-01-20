package com.example.security.repository;

import com.example.security.entity.UserAuthority;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {

  List<UserAuthority> findByUserIdAndState(Long userId, Integer state);
}
