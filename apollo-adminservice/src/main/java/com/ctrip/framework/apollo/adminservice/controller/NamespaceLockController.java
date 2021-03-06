package com.ctrip.framework.apollo.adminservice.controller;

import com.ctrip.framework.apollo.biz.entity.Namespace;
import com.ctrip.framework.apollo.biz.entity.NamespaceLock;
import com.ctrip.framework.apollo.biz.service.NamespaceLockService;
import com.ctrip.framework.apollo.biz.service.NamespaceService;
import com.ctrip.framework.apollo.biz.utils.ApolloSwitcher;
import com.ctrip.framework.apollo.common.utils.BeanUtils;
import com.ctrip.framework.apollo.core.dto.NamespaceLockDTO;
import com.ctrip.framework.apollo.core.exception.BadRequestException;
import com.ctrip.framework.apollo.core.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NamespaceLockController {


  @Autowired
  private NamespaceLockService namespaceLockService;
  @Autowired
  private NamespaceService namespaceService;
  @Autowired
  private ApolloSwitcher apolloSwitcher;

  @RequestMapping("/apps/{appId}/clusters/{clusterName}/namespaces/{namespaceName}/lock")
  public NamespaceLockDTO getNamespaceLockOwner(@PathVariable String appId, @PathVariable String clusterName,
                                                @PathVariable String namespaceName) {
    Namespace namespace = namespaceService.findOne(appId, clusterName, namespaceName);
    if (namespace == null) {
      throw new BadRequestException("namespace not exist.");
    }

    if (apolloSwitcher.isNamespaceLockSwitchOff()) {
      return null;
    }

    NamespaceLock lock = namespaceLockService.findLock(namespace.getId());

    if (lock == null) {
      return null;
    }

    return BeanUtils.transfrom(NamespaceLockDTO.class, lock);
  }

}
