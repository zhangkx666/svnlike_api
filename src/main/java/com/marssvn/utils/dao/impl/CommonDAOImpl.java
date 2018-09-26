package com.marssvn.utils.dao.impl;

import com.marssvn.api.model.entity.Entity;
import com.marssvn.utils.dao.ICommonDAO;
import com.marssvn.utils.exception.BusinessException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * CommonDAOImpl
 * @author zhangkx
 */
@Repository
public class CommonDAOImpl extends SqlSessionDaoSupport implements ICommonDAO {

    @Override
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    /**
     * @param sqlId sql id in mapper.xml
     * @return Object
     */
    @Override
    public <E> E queryForInt(String sqlId) {
        return queryForObject(sqlId, null, Integer.class);
    }

    /**
     * @param sqlId    sql id in mapper.xml
     * @param paramMap params
     * @param <E>      Object Type
     * @return Object
     */
    @Override
    public <E> E queryForInt(String sqlId, Map paramMap) {
        return queryForObject(sqlId, paramMap, Integer.class);
    }

    /**
     * @param sqlId    sql id in mapper.xml
     * @param clazz    class
     * @param <E>      Object Type
     * @return Object
     */
    @Override
    public <E> E queryForObject(String sqlId, Class clazz) {
        return queryForObject(sqlId, null, clazz);
    }

    /**
     * @param sqlId    sql id in mapper.xml
     * @param paramMap params
     * @param clazz    class
     * @param <E>      Object Type
     * @return Object
     */
    @Override
    @SuppressWarnings("unchecked")
    public <E> E queryForObject(String sqlId, Map paramMap, Class clazz) {
        Object obj;
        if (paramMap == null) {
            obj = this.getSqlSession().selectOne(sqlId);
        } else {
            obj = this.getSqlSession().selectOne(sqlId, paramMap);
        }
        try {
            if (clazz != null && obj != null) {
                return (E) clazz.cast(obj);
            }
        } catch (ClassCastException e) {
            throw new BusinessException("Data type not right");
        }
        return null;
    }

    /**
     * execute for list
     * @param sqlId sql id in mapper.xml
     * @return ArrayList
     */
    @Override
    public <E> List<E> queryForList(String sqlId) {
        return this.getSqlSession().selectList(sqlId);
    }

    /**
     * execute for list
     * @param sqlId sql id in mapper.xml
     * @param paramMap parameters
     * @return ArrayList
     */
    @Override
    public <E> List<E> queryForList(String sqlId, Map paramMap) {
        return this.getSqlSession().selectList(sqlId, paramMap);
    }

    /**
     * execute sql
     * @param sqlId sql id in mapper.xml
     * @param paramMap parameters
     * @return int
     */
    @Override
    public int execute(String sqlId, Map paramMap) {
        return this.getSqlSession().update(sqlId, paramMap);
    }

    /**
     * execute sql
     * @param sqlId sql id in mapper.xml
     * @param entity parameter entity
     * @return primary key
     */
    @Override
    public int execute(String sqlId, Entity entity) {
        return this.getSqlSession().update(sqlId, entity);
    }
}
