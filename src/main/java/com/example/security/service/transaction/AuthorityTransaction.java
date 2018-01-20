package com.example.security.service.transaction;

import com.example.security.entity.Authority;
import com.example.security.entity.Menu;
import com.example.security.repository.AuthorityRepository;
import com.example.security.repository.MenuRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthorityTransaction {

  @Autowired
  private MenuRepository menuRepository;
  @Autowired
  private AuthorityRepository authorityRepository;

  public Menu save(Menu menu, Authority authority) {
    menu = menuRepository.save(menu);
    authority.setLinkId(menu.getId());
    authorityRepository.save(authority);
    return menu;
  }
}
