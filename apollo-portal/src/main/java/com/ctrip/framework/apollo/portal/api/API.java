package com.ctrip.framework.apollo.portal.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.portal.service.ServiceLocator;

public class API {

  @Autowired
  protected ServiceLocator serviceLocator;

  @Autowired
  private RestTemplateFactory restTemplateFactory;

  protected RestTemplate restTemplate;

  @PostConstruct
  private void postConstruct() {
    restTemplate = restTemplateFactory.getObject();
  }

  public String getAdminServiceHost(Env env) {
    return serviceLocator.getServiceAddress(env).getHomepageUrl();
  }
}
