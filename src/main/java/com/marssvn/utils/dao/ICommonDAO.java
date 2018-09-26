package com.marssvn.utils.dao;

import com.marssvn.api.model.entity.Entity;

import java.util.List;
import java.util.Map;

/**
 * common dao
 * @author zhangkx
 */
public interface ICommonDAO {

    /**
     * query for int
     * @param sqlId    sql id in mapper.xml
     * @param <E>      Object Type
     * @return Object
     */
    <E> E queryForInt(String sqlId);

    /**
     * query for int with parameters
     * @param sqlId    sql id in mapper.xml
     * @param paramMap params
     * @param <E>      Object Type
     * @return Object
     */
    <E> E queryForInt(String sqlId, Map paramMap);

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
}
