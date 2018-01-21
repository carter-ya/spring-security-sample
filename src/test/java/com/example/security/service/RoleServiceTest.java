package com.example.security.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.security.entity.Role;
import com.example.security.enums.CommonState;
import com.example.security.repository.RoleRepository;
import com.example.security.web.req.SaveRoleReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleServiceTest {

  @Autowired
  private RoleService roleService;
  @Autowired
  private RoleRepository roleRepository;

  @Test
  public void save() {
    assertNotNull(createRole());
    assertNotNull(createRole("ADMIN", "管理员"));
  }

  @Test
  public void update() {
    Long roleId = createRole();
    SaveRoleReq req = new SaveRoleReq();
    req.setRoleNameEn("ADMIN");
    req.setRoleNameCn("管理员");
    roleService.update(roleId, req);
    Role role = roleRepository.findOne(roleId);
    assertEquals("ADMIN", role.getRoleNameEn());
  }

  @Test
  public void delete() {
    Long roleId = createRole();
    roleService.delete(roleId);
    Role role = roleRepository.findOne(roleId);
    assertEquals(role.getState(), CommonState.INVALID_CODE);
  }

  @Test
  public void listValidRoles() {
    Long roleId = createRole();
    createRole("ADMIN", "管理员");
    assertEquals(2, roleService.findValidRoles().size());
    roleService.delete(roleId);
    assertEquals(1, roleService.findValidRoles().size());
  }

  @Test
  public void addAuthority() {
  }

  @Test
  public void removeAuthority() {
  }

  @Test
  public void batchUpdateAuthority() {
  }

  private Long createRole() {
    return createRole("USER", "用户");
  }

  private Long createRole(String roleNameEn, String roleNameCn) {
    SaveRoleReq req = new SaveRoleReq();
    req.setRoleNameEn(roleNameEn);
    req.setRoleNameCn(roleNameCn);
    return roleService.save(req);
  }
}