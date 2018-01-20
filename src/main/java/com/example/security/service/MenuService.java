package com.example.security.service;

import com.example.security.entity.Authority;
import com.example.security.entity.Menu;
import com.example.security.entity.RoleAuthority;
import com.example.security.entity.RoleUser;
import com.example.security.enums.AuthorityType;
import com.example.security.enums.CommonState;
import com.example.security.repository.AuthorityRepository;
import com.example.security.repository.MenuRepository;
import com.example.security.repository.RoleAuthorityRepository;
import com.example.security.repository.RoleUserRepository;
import com.example.security.service.transaction.AuthorityTransaction;
import com.example.security.util.Extractor;
import com.example.security.web.req.SaveMenuReq;
import com.example.security.web.resp.MenuResp;
import com.example.security.web.resp.TreeMenuResp;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 菜单服务
 */
@Service
@Slf4j
public class MenuService {

  @Autowired
  private MenuRepository menuRepository;
  @Autowired
  private AuthorityTransaction authorityTransaction;
  @Autowired
  private RoleUserRepository roleUserRepository;
  @Autowired
  private RoleAuthorityRepository roleAuthorityRepository;
  @Autowired
  private AuthorityRepository authorityRepository;

  /**
   * 新增菜单
   */
  public Long save(SaveMenuReq req) {
    Authority authority = new Authority();
    authority.setType(AuthorityType.MENU.getCode());
    Menu menu = authorityTransaction.save(req.toMenu(), authority);
    log.info("菜单{}({})保存成功", menu.getMenuName(), menu.getUrl());
    return menu.getId();
  }

  /**
   * 更新菜单
   */
  public void update(Long menuId, SaveMenuReq req) {
    Menu menu = Optional.ofNullable(menuRepository.findOne(menuId)).orElseThrow(NoSuchElementException::new);
    BeanUtils.copyProperties(req, menu);
    menu = menuRepository.save(menu);
    log.info("菜单{}({})更新成功", menu.getMenuName(), menu.getUrl());
  }

  /**
   * 删除菜单
   */
  public void delete(Long menuId) {
    Menu menu = Optional.ofNullable(menuRepository.findOne(menuId)).orElseThrow(NoSuchElementException::new);
    if (CommonState.INVALID_CODE.equals(menu.getState())) {
      throw new IllegalStateException();
    }
    menu.setState(CommonState.INVALID_CODE);
    menuRepository.save(menu);
    log.info("菜单{}({})删除成功", menu.getMenuName(), menu.getUrl());
  }

  public MenuResp findById(Long id) {
    return Optional.ofNullable(menuRepository.findOne(id)).map(MenuResp::from).orElseThrow(NoSuchElementException::new);
  }

  /**
   * 获取菜单树
   */
  public List<TreeMenuResp> treeMenu() {
    List<Menu> menus = menuRepository.findByState(CommonState.VALID_CODE);
    return createTreeMenu(menus);
  }

  /**
   * 获取指定用户的有效菜单树
   *
   * @param userId 用户ID
   */
  public List<TreeMenuResp> treeMenu(Long userId) {
    List<RoleUser> roleUsers = roleUserRepository
        .findByUserIdAndState(userId, CommonState.VALID_CODE);
    if (roleUsers.isEmpty()) {
      return Collections.emptyList();
    }
    List<Long> roleIds = roleUsers.stream().map(RoleUser::getRoleId).collect(Collectors.toList());
    List<RoleAuthority> roleAuthorities = roleAuthorityRepository
        .findByRoleIdInAndState(roleIds, CommonState.VALID_CODE);
    List<Long> authorityIds = roleAuthorities.stream().map(RoleAuthority::getAuthorityId).collect(Collectors.toList());
    List<Authority> authorities = authorityRepository.findByIdInAndType(authorityIds, CommonState.VALID_CODE);
    if (authorities.isEmpty()) {
      return Collections.emptyList();
    }
    List<Menu> menus = menuRepository.findByIdInAndState(authorities.stream().map(Authority::getLinkId).collect(
        Collectors.toList()), CommonState.VALID_CODE);
    return createTreeMenu(menus);
  }

  /**
   * 创建树形菜单
   */
  private List<TreeMenuResp> createTreeMenu(List<Menu> menus) {
    Map<Long, List<Menu>> parentIdGroupMenuMap = Extractor.groupingBy(menus, Menu::getParentId);
    return createChildrenTreeMenu(parentIdGroupMenuMap, null, 1);
  }

  private List<TreeMenuResp> createChildrenTreeMenu(Map<Long, List<Menu>> parentIdGroupMenuMap, TreeMenuResp parent,
      int level) {
    List<Menu> children = parentIdGroupMenuMap
        .getOrDefault(Optional.ofNullable(parent).map(p -> p.getMenu().getId()).orElse(0L), Collections.emptyList());

    List<TreeMenuResp> treeMenus = children.isEmpty() ? Collections.emptyList() : new LinkedList<>();

    Optional.ofNullable(parent).ifPresent(p -> p.setChildren(treeMenus));
    for (Menu child : children) {
      TreeMenuResp treeMenu = TreeMenuResp.from(level, child);
      treeMenus.add(treeMenu);
      createChildrenTreeMenu(parentIdGroupMenuMap, treeMenu, level + 1);
    }
    return treeMenus;
  }
}
