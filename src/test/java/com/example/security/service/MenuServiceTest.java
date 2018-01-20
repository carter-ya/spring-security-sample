package com.example.security.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.security.entity.Menu;
import com.example.security.enums.CommonState;
import com.example.security.repository.MenuRepository;
import com.example.security.web.req.SaveMenuReq;
import com.example.security.web.resp.MenuResp;
import com.example.security.web.resp.TreeMenuResp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MenuServiceTest {

  @Autowired
  private MenuService menuService;
  @Autowired
  private MenuRepository menuRepository;

  @Test
  public void save() {
    SaveMenuReq req = new SaveMenuReq();
    req.setMenuName("菜单");
    req.setUrl("/menu");
    req.setIcon("fa fa-icon");
    Long menuId = menuService.save(req);
    assertNotNull(menuId);
  }

  @Test
  public void update() {
    SaveMenuReq req = new SaveMenuReq();
    req.setMenuName("菜单");
    req.setUrl("/menu");
    req.setIcon("fa fa-icon");
    Long menuId = menuService.save(req);
    MenuResp menu = menuService.findById(menuId);
    assertEquals(req.getUrl(), menu.getUrl());
    req.setUrl("/menu_new");
    menuService.update(menuId, req);
    assertEquals(req.getUrl(), menuService.findById(menuId).getUrl());
  }

  @Test
  public void delete() {
    SaveMenuReq req = new SaveMenuReq();
    req.setMenuName("菜单");
    req.setUrl("/menu");
    Long menuId = menuService.save(req);
    menuService.delete(menuId);
    Menu menu = menuRepository.findOne(menuId);
    assertEquals(menu.getState(), CommonState.INVALID_CODE);
  }

  @Test
  public void treeMenu() throws JsonProcessingException {
    int size = 5;
    List<Long> firstLevel = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      SaveMenuReq req = new SaveMenuReq();
      req.setMenuName("菜单" + (i + 1));
      req.setUrl("/menu_" + (i + 1));
      firstLevel.add(menuService.save(req));
    }
    List<Long> secondLevel = new ArrayList<>(size);
    for (Long parentId : firstLevel) {
      SaveMenuReq req = new SaveMenuReq();
      req.setMenuName("菜单" + (parentId));
      req.setUrl("/second_menu_" + parentId);
      req.setParentId(parentId);
      secondLevel.add(menuService.save(req));
    }

    List<Long> thirdLevel = new ArrayList<>(size);
    for (Long parentId : secondLevel) {
      SaveMenuReq req = new SaveMenuReq();
      req.setMenuName("菜单" + parentId);
      req.setUrl("/third_menu_" + parentId);
      req.setParentId(parentId);
      thirdLevel.add(menuService.save(req));
    }
    SaveMenuReq req = new SaveMenuReq();
    req.setMenuName("菜单" + thirdLevel.get(0));
    req.setUrl("/fourth_menu_" + thirdLevel.get(0));
    req.setParentId(thirdLevel.get(0));
    menuService.save(req);
    List<TreeMenuResp> treeMenu = menuService.treeMenu();
    assertEquals(size, treeMenu.size());
    ObjectMapper mapper = new ObjectMapper();
    System.out.println(mapper.writeValueAsString(treeMenu));
  }
}