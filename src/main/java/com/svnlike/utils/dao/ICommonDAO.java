package com.svnlike.utils.dao;

import com.svnlike.api.model.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * common dao
 * @author zhangkx
 */
public interface ICommonDAO {

    /**
     * query for long
     * @param sqlId    sql id in mapper.xml
     * @param <E>      Object Type
     * @return Object
     */
    <E> E queryForLong(String sqlId);

    /**
     * query for long with parameters
     * @param sqlId    sql id in mapper.xml
     * @param paramMap params
     * @param <E>      Object Type
     * @return Object
     */
    <E> E queryForLong(String sqlId, Map paramMap);

    /**
     * query for object
     * @param sqlId    sql id in mapper.xml
     * @param clazz    class
     * @param <E>      Object Type
     * @return Object
     */
    <E> E queryForObject(String sqlId, Class clazz);

    /**
     * query for object with parameters
     * @param sqlId    sql id in mapper.xml
     * @param paramMap params
     * @param <E>      Object Type
     * @return Object
     */
    <E> E queryForObject(String sqlId, Map paramMap);

    /**
     * query for object with parameters
     * @param sqlId    sql id in mapper.xml
     * @param paramMap params
     * @param clazz    class
     * @param <E>      Object Type
     * @return Object
     */
    <E> E queryForObject(String sqlId, Map paramMap, Class clazz);

    /**
     * query for list
     * @param sqlId sql id in mapper.xml
     * @return ArrayList
     */
    <E> List<E> queryForList(String sqlId);

    /**
     * execute for list with parameters
     * @param sqlId sql id in mapper.xml
     * @param paramMap parameters
     * @return ArrayList
     */
    <E> List<E> queryForList(String sqlId, Map paramMap);

    /**
     * execute for list with parameters
     * @param sqlId sql id in mapper.xml
     * @param entity parameters
     * @return ArrayList
     */
    <E> List<E> queryForList(String sqlId, Entity entity);

    /**
     * execute sql with parameter map
     * @param sqlId sql id in mapper.xml
     * @param paramMap parameters
     * @return row count
     */
    int execute(String sqlId, Map paramMap);

    /**
     * execute sql with parameter entity
     * @param sqlId sql id in mapper.xml
     * @param entity parameter entity
     * @return primary key
     */
    int execute(String sqlId, Entity entity);

    /**
     * check exists
     * @param sqlId sql id in mapper.xml
     * @param params parameter
     * @return boolean
     */
    boolean checkExists(String sqlId, HashMap<String, Object> params);
}
