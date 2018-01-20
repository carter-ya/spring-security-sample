package com.example.security.service;

import com.example.security.entity.Authority;
import com.example.security.enums.AuthorityType;
import com.example.security.enums.CommonState;
import com.example.security.repository.AuthorityRepository;
import com.example.security.web.req.AuthorityQueryReq;
import com.example.security.web.req.SaveAuthorityReq;
import com.example.security.web.resp.AuthorityResp;
import com.example.security.web.resp.DefaultPageResp;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * 权限服务
 */
@Service
@Slf4j
public class AuthorityService {

  @Autowired
  private AuthorityRepository authorityRepository;
  @Autowired
  private AuthorityTypeService authorityTypeService;

  /**
   * 新增权限
   */
  public Long save(SaveAuthorityReq req) {
    AuthorityType type = AuthorityType.find(req.getType());
    Authority authority = authorityRepository.findByTypeAndLinkId(type.getCode(), req.getLinkId());
    if (authority != null) {
      if (CommonState.VALID_CODE.equals(authority.getState())) {
        throw new IllegalStateException();
      }
      authority.setState(CommonState.INVALID_CODE);
    } else {
      if (!authorityTypeService.existsAuthorityLinkId(type, req.getLinkId())) {
        throw new NoSuchElementException("权限类型" + req.getType() + "，主键ID" + req.getLinkId() + "不存在");
      }
      authority = req.toAuthority();
    }
    authority = authorityRepository.save(authority);
    log.info("权限{}保存成功{}({})", authority.getId(), req.getType(), req.getLinkId());
    return authority.getId();
  }

  /**
   * 更新权限
   */
  public void update(Long id, SaveAuthorityReq req) {
    Authority authority = Optional.ofNullable(authorityRepository.findOne(id))
        .orElseThrow(NoSuchElementException::new);
    AuthorityType type = AuthorityType.find(req.getType());
    authority.setType(type.getCode());
    if (!authorityTypeService.existsAuthorityLinkId(type, req.getLinkId())) {
      throw new NoSuchElementException("权限类型" + req.getType() + "，主键ID" + req.getLinkId() + "不存在");
    }
    authority.setLinkId(req.getLinkId());
    authorityRepository.save(authority);
    log.info("权限{}更新成功{}({})", id, req.getType(), req.getLinkId());
  }

  /**
   * 删除权限
   */
  public void delete(Long id) {
    Authority authority = Optional.ofNullable(authorityRepository.findOne(id))
        .orElseThrow(NoSuchElementException::new);
    if (CommonState.INVALID_CODE.equals(authority.getState())) {
      throw new IllegalStateException();
    }
    authority.setState(CommonState.INVALID_CODE);
    authorityRepository.save(authority);
    log.info("权限{}删除成功{}({})", id, authority.getType(), authority.getState());
  }

  /**
   * 自定义查询权限
   */
  public DefaultPageResp<AuthorityResp> findByQuery(AuthorityQueryReq req) {
    Specification<Authority> spec = (Root<Authority> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      List<Predicate> predicates = new LinkedList<>();
      if (StringUtils.isNotBlank(req.getType())) {
        predicates.add(cb.equal(root.get("type"), AuthorityType.find(req.getType()).getCode()));
      }
      if (req.getLinkId() != null) {
        predicates.add(cb.equal(root.get("linkId"), req.getLinkId()));
      }
      return cb.and(predicates.toArray(new Predicate[]{}));
    };
    Page<Authority> page = authorityRepository
        .findAll(spec, new PageRequest(req.getZeroBasedPage(), req.getPageSize()));
    return new DefaultPageResp<>(page.map(AuthorityResp::from));
  }
}
