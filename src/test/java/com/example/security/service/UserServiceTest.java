package com.example.security.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.security.entity.User;
import com.example.security.enums.UserState;
import com.example.security.repository.UserRepository;
import com.example.security.service.UserService.SecurityUser;
import com.example.security.web.req.SaveUserReq;
import com.example.security.web.req.UserQueryReq;
import com.example.security.web.resp.DefaultPageResp;
import com.example.security.web.resp.UserResp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@Sql("classpath:schema.sql")
public class UserServiceTest {

  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;


  @Test
  public void loadUserByUsername() {
    Long userId = createUser();
    User user = userRepository.findOne(userId);
    SecurityUser securityUser = (SecurityUser) userService.loadUserByUsername(user.getUsername());
    assertTrue(securityUser.isAccountNonLocked());
    assertTrue(securityUser.isAccountNonExpired());
  }

  @Test
  public void save() {
    Long userId = createUser();
    assertNotNull(userId);
  }

  @Test
  public void lock() {
    Long userId = createUser();
    userService.lock(userId);
    User user = userRepository.findOne(userId);
    assertEquals(user.getState(), UserState.LOCKED.getCode());
  }

  @Test
  public void delete() {
    Long userId = createUser();
    userService.delete(userId);
    User user = userRepository.findOne(userId);
    assertEquals(user.getState(), UserState.INVALID.getCode());
  }

  @Test
  public void findByQuery() {
    UserQueryReq req = new UserQueryReq();
    DefaultPageResp<UserResp> pageResp = userService
        .findByQuery(req);
    assertEquals(0, pageResp.getCurrentElements());
    createUser();
    req.setPageSize(1);
    assertEquals(1, userService.findByQuery(req).getCurrentElements());
    createUser("刘备", "liubei@sanguo.com");
    assertEquals(true, userService.findByQuery(req).isHasNext());
  }

  private Long createUser() {
    return createUser("孙尚香", "sunshangxiang@sanguo.com");
  }

  private Long createUser(String name, String username) {
    SaveUserReq req = new SaveUserReq();
    req.setName(name);
    req.setUsername(username);
    req.setPassword("1234qwer");
    return userService.save(req);
  }
}