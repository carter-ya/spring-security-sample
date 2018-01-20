package com.example.security.service;

import static java.util.stream.Collectors.toList;

import com.example.security.entity.Role;
import com.example.security.entity.RoleUser;
import com.example.security.entity.User;
import com.example.security.enums.CommonState;
import com.example.security.enums.UserState;
import com.example.security.repository.RoleRepository;
import com.example.security.repository.RoleUserRepository;
import com.example.security.repository.TokenBasedSecurityContextRepository;
import com.example.security.repository.UserRepository;
import com.example.security.util.Extractor;
import com.example.security.web.HandlerRequireOperatorMethodArgumentResolver.Operator;
import com.example.security.web.HandlerRequireOperatorMethodArgumentResolver.OperatorResolver;
import com.example.security.web.req.ChangePasswordReq;
import com.example.security.web.req.ResetPasswordReq;
import com.example.security.web.req.SaveUserReq;
import com.example.security.web.req.UserLoginReq;
import com.example.security.web.req.UserQueryReq;
import com.example.security.web.resp.DefaultPageResp;
import com.example.security.web.resp.RoleResp;
import com.example.security.web.resp.UserResp;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * 用户服务
 */
@Service
@Slf4j
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleUserRepository roleUserRepository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private UserTokenService userTokenService;
  @Autowired
  private TokenBasedSecurityContextRepository securityContextRepository;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = Optional.ofNullable(userRepository.findByUsername(username))
        .orElseThrow(() -> new UsernameNotFoundException("账号" + username + "不存在"));
    // 账户被冻结或删除
    if (!UserState.VALID.getCode().equals(user.getState())) {
      return new SecurityUser(user);
    }
    // 查找用户角色
    List<RoleUser> roleUsers = roleUserRepository
        .findByUserIdAndState(user.getId(), CommonState.VALID_CODE);
    if (!roleUsers.isEmpty()) {
      List<Role> roles = roleRepository
          .findByIdInAndState(Extractor.map(roleUsers, RoleUser::getRoleId), CommonState.VALID_CODE);
      List<SimpleGrantedAuthority> grantedAuthorities = Extractor
          .map(roles, role -> new SimpleGrantedAuthority(role.getRoleNameEn()));
      return new SecurityUser(user, grantedAuthorities);
    }
    return new SecurityUser(user);
  }

  /**
   * 用户登录
   */
  public String login(UserLoginReq req) {
    UserDetails userDetails = loadUserByUsername(req.getUsername());
    if (!BCrypt.checkpw(req.getPassword(), userDetails.getPassword())) {
      throw new IllegalStateException("密码不正确");
    }
    SecurityContext securityContext = new SecurityContextImpl();
    securityContext.setAuthentication(
        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    String token = UUID.randomUUID().toString().replace("-", "");
    userTokenService.saveToken(token, securityContext,
        securityContextRepository.getTokenExpired() * 60 * 1_000 + System.currentTimeMillis());
    return token;
  }

  /**
   * 注册用户
   */
  public Long save(SaveUserReq req) {
    if (userRepository.existsByUsername(req.getUsername())) {
      throw new IllegalStateException();
    }
    User user = req.toUser(BCrypt.gensalt());
    user = userRepository.save(user);
    log.info("用户{}({})保存成功", user.getUsername(), user.getName());
    return user.getId();
  }

  /**
   * 锁定用户
   */
  public void lock(Long userId) {
    User user = Optional.ofNullable(userRepository.findOne(userId)).orElseThrow(NoSuchElementException::new);
    if (UserState.LOCKED.getCode().equals(user.getState())) {
      throw new IllegalStateException();
    }
    user.setState(UserState.LOCKED.getCode());
    user = userRepository.save(user);
    log.info("用户{}({})锁定成功", user.getUsername(), user.getName());
  }

  /**
   * 删除用户
   */
  public void delete(Long userId) {
    User user = Optional.ofNullable(userRepository.findOne(userId)).orElseThrow(NoSuchElementException::new);
    if (UserState.INVALID.getCode().equals(user.getState())) {
      throw new IllegalStateException();
    }
    user.setState(UserState.INVALID.getCode());
    user = userRepository.save(user);
    log.info("用户{}({})删除成功", user.getUsername(), user.getName());
  }

  /**
   * 直接重置用户密码，不验证原始密码
   */
  public void resetPassword(Long userId, ResetPasswordReq req) {
    User user = Optional.ofNullable(userRepository.findOne(userId)).orElseThrow(NoSuchElementException::new);
    user.setPassword(BCrypt.hashpw(req.getPassword(), BCrypt.gensalt()));
    userRepository.save(user);
    log.info("用户{}({})重置密码成功", user.getUsername(), user.getName());
    //TODO 移除用户登录凭证？
  }

  /**
   * 在验证原始密码的情况下修改用户密码
   */
  public void changePassword(Long userId, ChangePasswordReq req) {
    if (req.getRawPassword().equals(req.getNewPassword())) {
      throw new IllegalArgumentException("修改后的密码不能与修改前的密码一致");
    }
    User user = Optional.ofNullable(userRepository.findOne(userId)).orElseThrow(NoSuchElementException::new);
    if (!BCrypt.checkpw(req.getRawPassword(), user.getPassword())) {
      throw new IllegalArgumentException("密码不正确");
    }
    resetPassword(userId, req.toResetPasswordReq());
  }

  /**
   * 自定义查询
   */
  public DefaultPageResp<UserResp> findByQuery(UserQueryReq req) {
    Specification<User> spec = (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      List<Predicate> predicates = new LinkedList<>();
      if (StringUtils.isNotBlank(req.getState())) {
        Integer state = UserState.find(req.getState()).getCode();
        predicates.add(cb.equal(root.get("state"), state));
      }
      if (StringUtils.isNotBlank(req.getUsername())) {
        predicates.add(cb.like(root.get("username"), req.toLike(req.getUsername())));
      }
      if (StringUtils.isNotBlank(req.getName())) {
        predicates.add(cb.like(root.get("name"), req.toLike(req.getName())));
      }
      return cb.and(predicates.toArray(new Predicate[]{}));
    };
    return new DefaultPageResp<>(
        userRepository.findAll(spec, new PageRequest(req.getZeroBasedPage(), req.getPageSize())).map(UserResp::from));
  }

  /**
   * 获取指定用户所有的角色
   */
  public List<RoleResp> findRoleByUserId(Long userId) {
    List<RoleUser> roleUsers = roleUserRepository.findByUserIdAndState(userId, CommonState.VALID_CODE);
    if (roleUsers.isEmpty()) {
      return Collections.emptyList();
    }
    List<Role> roles = roleRepository
        .findByIdInAndState(roleUsers.stream().map(RoleUser::getRoleId).collect(toList()),
            CommonState.VALID_CODE);
    return roles.stream().map(RoleResp::from).collect(toList());
  }

  /**
   * 批量授予用户角色
   */
  public void addRoles(Long userId, List<Long> roleIds) {
    if (!userRepository.exists(userId)) {
      throw new NoSuchElementException("用户" + userId + "不存在");
    }
    List<RoleUser> roleUsers = roleUserRepository.findByUserIdAndRoleIdIn(userId, roleIds);
    roleUsers.forEach(roleUser -> {
      if (CommonState.VALID_CODE.equals(roleUser.getState())) {
        throw new IllegalStateException();
      }
    });
    List<Role> roles = roleRepository.findAll(roleIds);
    if (roles.size() != roleIds.size()) {
      throw new IllegalStateException();
    }
    Map<Long, RoleUser> roleIdMappingMap = new HashMap<>(roleIds.size());
    roleUsers.forEach(roleUser -> {
      roleUser.setState(CommonState.VALID_CODE);
      roleIdMappingMap.put(roleUser.getRoleId(), roleUser);
    });
    roles.forEach(role -> {
      if (!roleIdMappingMap.containsKey(role.getId())) {
        roleIdMappingMap.put(role.getId(), new RoleUser(role.getId(), userId));
      }
    });
    roleUserRepository.save(roleIdMappingMap.values());
    log.info("用户{}批量增加角色{}成功", userId, roleIds);
  }

  /**
   * 批量移除指定用户的角色
   */
  public void removeRoles(Long userId, List<Long> roleIds) {
    if (!userRepository.exists(userId)) {
      throw new NoSuchElementException("用户" + userId + "不存在");
    }
    List<RoleUser> roleUsers = roleUserRepository.findByUserIdAndRoleIdIn(userId, roleIds);
    if (roleUsers.size() != roleIds.size()) {
      throw new NoSuchElementException("用户角色数量不匹配");
    }
    roleUsers.forEach(roleUser -> roleUser.setState(CommonState.INVALID_CODE));
    roleUserRepository.save(roleUsers);
    log.info("用户{}批量移除角色{}成功", userId, roleIds);
  }

  public static class SecurityUser implements UserDetails, Operator {

    @Getter
    private final User user;
    private final Collection<? extends GrantedAuthority> grantedAuthorities;

    SecurityUser(User user) {
      this.user = user;
      this.grantedAuthorities = Collections.emptyList();
    }

    SecurityUser(User user, Collection<? extends GrantedAuthority> grantedAuthorities) {
      this.user = user;
      this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return grantedAuthorities;
    }

    @Override
    public String getPassword() {
      return user.getPassword();
    }

    @Override
    public String getUsername() {
      return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
      return true;
    }

    @Override
    public boolean isAccountNonLocked() {
      return !UserState.LOCKED.getCode().equals(user.getState());
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    @Override
    public boolean isEnabled() {
      return UserState.VALID.getCode().equals(user.getState());
    }

    @Override
    public Long getUserId() {
      return user.getId();
    }

    @Override
    public String getName() {
      return user.getName();
    }
  }

  /**
   * 从安全上下文加载操作用户
   *
   * @see SecurityContextHolder#getContext()
   * @see SecurityUser
   */
  public static class SecurityContextOperatorResolver implements OperatorResolver {

    @Override
    public Operator resolve(NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
      return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
  }
}
