package com.svnlike.api;

import com.svnlike.BaseApplicationTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext
@SuppressWarnings("unchecked")
public class RepositoryControllerTest extends BaseApplicationTest {

    /**
     * repository id
     */
    private static int repositoryId;

//    @Test
//    public void test01_createRepository() {
//        // params
//        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
//        params.put("projectId", 1);
//        params.put("title", "marssvn.com");
//        params.put("name", "marssvn");
//        params.put("description", "a svn repository for marssvn.com");
//        params.put("autoMakeDir", true);
//
//        HttpEntity<LinkedHashMap> jsonEntity = this.getJsonHttpEntity(params);
//        LinkedHashMap result =
//                testRestTemplate.postForObject("/repository", jsonEntity, LinkedHashMap.class);
//        repositoryId = ((int) ((LinkedHashMap) result.get("data")).get("id"));
//        Assert.isTrue(result.get("status").equals(1), "failed to create repository");
//    }
//
//    @Test
//    public void test02_createRepositoryFailed() {
//        // params
//        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
//        params.put("projectId", 1);
//        params.put("title", "marssvn.com");
//        params.put("name", "marssvn");
//        params.put("description", "a svn repository for marssvn.com");
//        params.put("autoMakeDir", true);
//
//        HttpEntity<LinkedHashMap> jsonEntity = this.getJsonHttpEntity(params);
//        LinkedHashMap result =
//                testRestTemplate.postForObject("/repository", jsonEntity, LinkedHashMap.class);
//        Assert.isTrue(result.get("status").equals(0), "repository created successfully");
//    }
//
//    @Test
//    public void test03_getRepositoryList() {
//        LinkedHashMap result =
//                this.testRestTemplate.getForObject("/repository", LinkedHashMap.class);
//
//        // status check
//        Assert.isTrue(result.get("status").equals(1), "status is not 1:success");
//
//        // data list
//        ArrayList<LinkedHashMap> dataList = (ArrayList<LinkedHashMap>) result.get("data");
//        Assert.notEmpty(dataList, "repository list is empty");
//
//        // data
//        LinkedHashMap data = dataList.get(0);
//        Assert.isTrue(result.get("status").equals(1), "failed to get repository list");
//        Assert.isTrue("marssvn".equals(data.get("projectName")), "project name not right");
//    }

//    @Test
//    public void test04_getRepositoryById() {
//        LinkedHashMap result =
//                this.testRestTemplate.getForObject("/repository/" + repositoryId, LinkedHashMap.class);
//
//        // status check
//        Assert.isTrue(result.get("status").equals(1), "status is not 1:success");
//
//        // data list
//        LinkedHashMap data = (LinkedHashMap) result.get("data");
//        Assert.isTrue(result.get("status").equals(1), "failed to get repository");
//        Assert.isTrue("marssvn".equals(data.get("projectName")), "project name not right");
//    }
//
//    @Test
//    public void test05_getRepositoryTreeById() {
//        LinkedHashMap result =
//                this.testRestTemplate.getForObject("/repository/" + repositoryId + "/tree", LinkedHashMap.class);
//
//        // status check
//        Assert.isTrue(result.get("status").equals(1), "failed to get repository tree");
//    }
//
//    @Test
//    public void test06_updateRepositoryById() {
//        // support HttpMethod.PATCH
//        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//
//        // params
//        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
//        params.put("title", "title_new");
//        params.put("name", "name_new");
//        params.put("description", "new description");
//
//        HttpEntity<LinkedHashMap> jsonEntity = this.getJsonHttpEntity(params);
//        LinkedHashMap result =
//                testRestTemplate.patchForObject(
//                        "/repository/" + repositoryId, jsonEntity, LinkedHashMap.class);
//
//        Assert.isTrue(result.get("status").equals(1), "failed to update repository update");
//    }
//
//    @Test
//    public void test07_deleteRepositoryById() {
//        ResponseEntity<LinkedHashMap> result =
//                testRestTemplate.exchange("/repository/" + repositoryId, HttpMethod.DELETE, null, LinkedHashMap.class);
//
//        Assert.isTrue(result.getStatusCode() == HttpStatus.OK, "");
//        Assert.isTrue(result.getBody().get("status").equals(1), "failed to delete repository");
//    }
//
//    @Test
//    public void test08_deleteNotExistsRepository() {
//        ResponseEntity<LinkedHashMap> result =
//                testRestTemplate.exchange("/repository/" + repositoryId, HttpMethod.DELETE, null, LinkedHashMap.class);
//
//        Assert.isTrue(result.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR, "");
//        Assert.isTrue(result.getBody().get("status").equals(0), "repository deleted successfully");
//    }
//
//    @Test
//    public void test09_getNotExistsRepository() {
//        LinkedHashMap result =
//                this.testRestTemplate.getForObject("/repository/" + repositoryId, LinkedHashMap.class);
//
//        // status check
//        Assert.isTrue(result.get("status").equals(0), "status is not 0:failed");
//        Assert.isTrue("BUSINESS_ERROR".equals(result.get("code")), "code is not BUSINESS_ERROR");
//    }
//
//    @Test
//    public void test10_updateNotExistsRepository() {
//        // solve "ResourceAccessException Caused by: java.net.ProtocolException: Invalid HTTP method: PATCH"
//        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//
//        // params
//        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
//        params.put("title", "title_new");
//        params.put("name", "name_new");
//        params.put("description", "new description");
//
//        HttpEntity<LinkedHashMap> jsonEntity = this.getJsonHttpEntity(params);
//        LinkedHashMap result =
//                testRestTemplate.patchForObject("/repository/" + repositoryId, jsonEntity, LinkedHashMap.class);
//
//        Assert.isTrue(result.get("status").equals(0), "repository updated successfully");
//    }
//
//    @Test
//    public void test11_getNotExistsRepositoryTree() {
//        LinkedHashMap result = this.testRestTemplate.getForObject("/repository/" + repositoryId + "/tree", LinkedHashMap.class);
//
//        // status check
//        Assert.isTrue(result.get("status").equals(0), "get repository tree successfully");
//    }
}
