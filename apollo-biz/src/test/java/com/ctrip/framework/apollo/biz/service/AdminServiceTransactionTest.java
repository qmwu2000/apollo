package com.ctrip.framework.apollo.biz.service;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import com.ctrip.framework.apollo.biz.BizTestConfiguration;
import com.ctrip.framework.apollo.common.entity.App;
import com.ctrip.framework.apollo.biz.repository.AppNamespaceRepository;
import com.ctrip.framework.apollo.biz.repository.AppRepository;
import com.ctrip.framework.apollo.biz.repository.ClusterRepository;
import com.ctrip.framework.apollo.biz.repository.NamespaceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BizTestConfiguration.class)
@Transactional
@Commit
public class AdminServiceTransactionTest {

  @Autowired
  AdminService adminService;

  @Autowired
  private AppRepository appRepository;

  @Autowired
  private AppNamespaceRepository appNamespaceRepository;

  @Autowired
  private NamespaceRepository namespaceRepository;

  @Autowired
  private ClusterRepository clusterRepository;

  @BeforeTransaction
  public void verifyInitialDatabaseState() {
    for (App app : appRepository.findAll()) {
      System.out.println(app.getAppId());
    }
    Assert.assertEquals(0, appRepository.count());
    Assert.assertEquals(7, appNamespaceRepository.count());
    Assert.assertEquals(0, namespaceRepository.count());
    Assert.assertEquals(0, clusterRepository.count());
  }

  @Before
  public void setUpTestDataWithinTransaction() {
    Assert.assertEquals(0, appRepository.count());
    Assert.assertEquals(7, appNamespaceRepository.count());
    Assert.assertEquals(0, namespaceRepository.count());
    Assert.assertEquals(0, clusterRepository.count());
  }

  @Test
  @Rollback
  public void modifyDatabaseWithinTransaction() {
    String appId = "someAppId";
    App app = new App();
    app.setAppId(appId);
    app.setName("someAppName");
    String owner = "someOwnerName";
    app.setOwnerName(owner);
    app.setOwnerEmail("someOwnerName@ctrip.com");
    app.setDataChangeCreatedBy(owner);
    app.setDataChangeLastModifiedBy(owner);
    app.setDataChangeCreatedTime(new Date());
    adminService.createNewApp(app);
  }

  @After
  public void tearDownWithinTransaction() {
    Assert.assertEquals(1, appRepository.count());
    Assert.assertEquals(8, appNamespaceRepository.count());
    Assert.assertEquals(1, namespaceRepository.count());
    Assert.assertEquals(1, clusterRepository.count());
  }

  @AfterTransaction
  public void verifyFinalDatabaseState() {
    Assert.assertEquals(0, appRepository.count());
    Assert.assertEquals(7, appNamespaceRepository.count());
    Assert.assertEquals(0, namespaceRepository.count());
    Assert.assertEquals(0, clusterRepository.count());
  }
}
