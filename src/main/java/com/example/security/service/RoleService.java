package com.example.security.service;

import com.example.security.entity.Authority;
import com.example.security.entity.Role;
import com.example.security.entity.RoleAuthority;
import com.example.security.enums.AuthorityType;
import com.example.security.enums.CommonState;
import com.example.security.repository.AuthorityRepository;
import com.example.security.repository.RoleAuthorityRepository;
import com.example.security.repository.RoleRepository;
import com.example.security.service.transaction.RoleTransactionService;
import com.example.security.util.Extractor;
import com.example.security.web.req.FindRoleAuthorityReq;
import com.example.security.web.req.SaveRoleReq;
import com.example.security.web.resp.RoleResp;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色服务
 */
@Service
@Slf4j
public class RoleService {

  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private RoleAuthorityRepository roleAuthorityRepository;
  @Autowired
  private RoleTransactionService roleTransactionService;
  @Autowired
  private AuthorityRepository authorityRepository;

  /**
   * 保存角色
   */
  public Long save(SaveRoleReq req) {
    Role role = roleRepository.save(req.toRole());
    log.info("角色{}({})保存成功", role.getRoleNameEn(), role.getRoleNameCn());
    return role.getId();
  }

  /**
   * 更新角色
   */
  public void update(Long roleId, SaveRoleReq req) {
    Role role = Optional.ofNullable(roleRepository.findOne(roleId)).orElseThrow(NoSuchElementException::new);
    BeanUtils.copyProperties(req, role);
    roleRepository.save(role);
    log.info("角色{}({})保存成功", role.getRoleNameEn(), role.getRoleNameCn());
  }

  /**
   * 删除角色
   */
  public void delete(Long roleId) {
    Role role = Optional.ofNullable(roleRepository.findOne(roleId)).orElseThrow(NoSuchElementException::new);
    if (CommonState.INVALID_CODE.equals(role.getState())) {
      throw new IllegalStateException();
    }
    role.setState(CommonState.INVALID_CODE);
    roleRepository.save(role);
    log.info("角色{}({})删除成功", role.getRoleNameEn(), role.getRoleNameCn());
  }

  /**
   * 获取所有有效的角色
   */
  public List<RoleResp> findValidRoles() {
    return Extractor.map(roleRepository.findByState(CommonState.VALID_CODE), RoleResp::from);
  }

  /**
   * 增加权限
   *
   * @param roleId 角色ID
   * @param authorityId 权限ID
   */
  public void addAuthority(Long roleId, Long authorityId) {
    if (!roleRepository.exists(roleId)) {
      throw new NoSuchElementException("角色" + roleId + "不存在");
    }
    if (!authorityRepository.exists(authorityId)) {
      throw new NoSuchElementException("权限" + authorityId + "不存在");
    }
    RoleAuthority roleAuthority = roleAuthorityRepository
        .findByRoleIdAndAuthorityId(roleId, authorityId);
    if (roleAuthority != null) {
      if (!CommonState.INVALID_CODE.equals(roleAuthority.getState())) {
        throw new IllegalStateException();
      }
      // 从删除态恢复到正常态
      roleAuthority.setState(CommonState.VALID_CODE);
    } else {
      // 新增授权
      roleAuthority = new RoleAuthority(roleId, authorityId);
    }
    roleAuthorityRepository.save(roleAuthority);
    log.info("角色{}新增授权{}成功", roleId, authorityId);
  }

  /**
   * 批量移除角色权限
   *
   * @param roleId 角色ID
   * @param authorityIds 权限ID列表
   */
  public void removeAuthorities(Long roleId, List<Long> authorityIds) {
    if (!roleRepository.exists(roleId)) {
      throw new NoSuchElementException("角色" + roleId + "不存在");
    }
    List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findByRoleIdAndAuthorityIdIn(roleId, authorityIds);
    if (roleAuthorities.size() != authorityIds.size()) {
      throw new NoSuchElementException("角色资源数量不匹配");
    }
    roleAuthorities.forEach(roleAuthority -> roleAuthority.setState(CommonState.INVALID_CODE));
    roleAuthorityRepository.save(roleAuthorities);
    log.info("角色{}批量移除授权{}成功", roleId, authorityIds);
  }

  /**
   * 批量更新角色授权
   * <br>
   * 只保留在authorityIds中存在的授权，其它授权全部取消，新增不存在的授权
   *
   * @param roleId 角色ID
   * @param authorityIds 当前实际的授权
   */
  public void batchUpdateAuthorities(Long roleId, List<Long> authorityIds) {
    Set<Long> authorityIdSet = new HashSet<>(authorityIds);
    int authoritySize = authorityIds.size();
    if (authoritySize != authorityIdSet.size()) {
      throw new NoSuchElementException("存在重复的权限ID");
    }
    if (authorityRepository.countByIdIn(authorityIds) != authoritySize) {
      throw new NoSuchElementException("存在无法识别的权限");
    }
    List<RoleAuthority> existsRoleAuthorities = roleAuthorityRepository.findByRoleId(roleId);
    Map<Long, RoleAuthority> existsRoleAuthorityMap = Extractor
        .toMap(existsRoleAuthorities, RoleAuthority::getAuthorityId);

    // 增增不存在的授权列表
    List<RoleAuthority> newRoleAuthorities = new LinkedList<>();
    // 被取消/被重新授权的授权列表
    List<RoleAuthority> updateRoleAuthorities = new LinkedList<>();
    authorityIds.forEach(authorityId -> {
      RoleAuthority removeAuthority = existsRoleAuthorityMap.remove(authorityId);
      // 新增不存在的授权
      if (removeAuthority == null) {
        removeAuthority = new RoleAuthority(roleId, authorityId);
        newRoleAuthorities.add(removeAuthority);
      } else if (CommonState.INVALID_CODE.equals(removeAuthority.getState())) {
        // 被重新授权
        removeAuthority.setState(CommonState.VALID_CODE);
        updateRoleAuthorities.add(removeAuthority);
      }
    });
    existsRoleAuthorityMap.forEach((key, value) -> value.setState(CommonState.INVALID_CODE));
    updateRoleAuthorities.addAll(existsRoleAuthorityMap.values());
    roleTransactionService.save(newRoleAuthorities, updateRoleAuthorities);
    log.info("批量更新角色{}授权成功", roleId);
  }

  /**
   * 查询指定角色，指定权限类型的所有有效ID
   *
   * @param roleId 角色ID
   * @param req 权限类型
   */
  public List<Long> findByAuthorityType(Long roleId, FindRoleAuthorityReq req) {
    List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findByRoleIdAndState(roleId, CommonState.VALID_CODE);
    if (roleAuthorities.isEmpty()) {
      return Collections.emptyList();
    }
    AuthorityType type = AuthorityType.find(req.getType());
    List<Long> authorityIds = Extractor.map(roleAuthorities, RoleAuthority::getAuthorityId);
    return Extractor
        .map(authorityRepository.findByTypeAndLinkIdIn(type.getCode(), authorityIds), Authority::getLinkId);
  }
}
