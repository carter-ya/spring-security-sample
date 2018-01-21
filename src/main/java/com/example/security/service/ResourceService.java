package com.example.security.service;

import static java.util.stream.Collectors.toList;

import com.example.security.entity.Resource;
import com.example.security.enums.CommonState;
import com.example.security.repository.ResourceRepository;
import com.example.security.web.req.SaveResourceReq;
import com.example.security.web.resp.ResourceResp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资源服务
 */
@Service
@Slf4j
public class ResourceService {

  @Autowired
  private ResourceRepository resourceRepository;

  public Long save(SaveResourceReq req) {
    Resource resource = resourceRepository.findByResourceNameAndUrl(req.getResourceName(), req.getUrl());
    if (resource != null) {
      if (CommonState.VALID_CODE.equals(resource.getState())) {
        throw new IllegalStateException();
      }
      resource.setState(CommonState.VALID_CODE);
    } else {
      resource = new Resource(req.getResourceName(), req.getUrl());
    }
    resource = resourceRepository.save(resource);
    log.info("资源{}({})保存成功", req.getResourceName(), req.getUrl());
    return resource.getId();
  }

  public void update(Long resourceId, SaveResourceReq req) {
    Resource resource = Optional.ofNullable(resourceRepository.findOne(resourceId))
        .orElseThrow(NoSuchElementException::new);
    BeanUtils.copyProperties(req, resource);
    resourceRepository.save(resource);
    log.info("资源{}({})更新成功", resourceId, req.getResourceName());
  }

  public void delete(Long resourceId) {
    Resource resource = Optional.ofNullable(resourceRepository.findOne(resourceId))
        .orElseThrow(NoSuchElementException::new);
    if (CommonState.INVALID_CODE.equals(resource.getState())) {
      throw new IllegalStateException();
    }
    resource.setState(CommonState.INVALID_CODE);
    resourceRepository.save(resource);
    log.info("资源{}({})删除成功", resourceId, resource.getResourceName());
  }

  public ResourceResp findById(Long resourceId) {
    Resource resource = Optional.ofNullable(resourceRepository.findOne(resourceId))
        .orElseThrow(NoSuchElementException::new);
    return ResourceResp.from(resource);
  }

  public List<ResourceResp> findValidResources() {
    return resourceRepository.findByState(CommonState.VALID_CODE).stream().map(ResourceResp::from).collect(toList());
  }
}
