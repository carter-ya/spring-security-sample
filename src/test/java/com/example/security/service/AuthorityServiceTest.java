package com.example.security.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.security.entity.Authority;
import com.example.security.enums.AuthorityType;
import com.example.security.enums.CommonState;
import com.example.security.repository.AuthorityRepository;
import com.example.security.web.req.AuthorityQueryReq;
import com.example.security.web.req.SaveAuthorityReq;
import com.example.security.web.req.SaveMenuReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorityServiceTest {

  @Autowired
  private AuthorityService authorityService;
  @Autowired
  private AuthorityRepository authorityRepository;
  @Autowired
  private MenuService menuService;


  @Test
  public void save() {
    Long authorityId = createAuthority();
    assertNotNull(authorityId);
  }

  @Test
  public void update() {
    Long authorityId = createAuthority();
    Long menuId = createMenu("菜单2", "/menu/2");
    SaveAuthorityReq req = new SaveAuthorityReq();
    req.setLinkId(menuId);
    req.setType(AuthorityType.MENU.getValue());
    authorityService.update(authorityId, req);
    assertEquals(menuId, authorityRepository.findOne(authorityId).getLinkId());
  }

  @Test
  public void delete() {
    Long authorityId = createAuthority();
    authorityService.delete(authorityId);
    Authority authority = authorityRepository.findOne(authorityId);
    assertEquals(authority.getState(), CommonState.INVALID_CODE);
  }

  @Test
  public void findByQuery() {
    AuthorityQueryReq req = new AuthorityQueryReq();
    assertEquals(0L, authorityService.findByQuery(req).getCurrentElements());
    createAuthority();
    req.setType(AuthorityType.MENU.getValue());
    assertEquals(1L, authorityService.findByQuery(req).getCurrentElements());
  }

  private Long createMenu(String name, String url) {
    SaveMenuReq req = new SaveMenuReq();
    req.setMenuName(name);
    req.setUrl(url);
    return menuService.save(req);
  }

  private Long createAuthority() {
    return createAuthority(AuthorityType.MENU, createMenu("菜单", "/menu"));
  }

  private Long createAuthority(AuthorityType type, Long linkId) {
    SaveAuthorityReq req = new SaveAuthorityReq();
    req.setType(type.getValue());
    req.setLinkId(linkId);
    return authorityService.save(req);
  }
}