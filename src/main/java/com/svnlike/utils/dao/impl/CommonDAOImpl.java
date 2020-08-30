package com.svnlike.utils.dao.impl;

import com.svnlike.api.model.entity.Entity;
import com.svnlike.utils.dao.ICommonDAO;
import com.svnlike.utils.exception.SvnLikeException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
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
    public Long queryForLong(String sqlId) {
        return queryForObject(sqlId, null, Long.class);
    }

    /**
     * @param sqlId    sql id in mapper.xml
     * @param paramMap params
     * @return Object
     */
    @Override
    public Long queryForLong(String sqlId, Map paramMap) {
        return queryForObject(sqlId, paramMap, Long.class);
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
     * query for object with parameters
     *
     * @param sqlId    sql id in mapper.xml
     * @param paramMap params
     * @return Object
     */
    @Override
    public <E> E queryForObject(String sqlId, Map paramMap) {
        return this.getSqlSession().selectOne(sqlId, paramMap);
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
            throw new SvnLikeException("Data type not right");
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

    @Override
    public <E> List<E> queryForList(String sqlId, Entity entity) {
        return this.getSqlSession().selectList(sqlId, entity);
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

    /**
     * check exists
     *
     * @param sqlId sql id in mapper.xml
     * @param params parameters
     * @return boolean
     */
    @Override
    public boolean checkExists(String sqlId, HashMap<String, Object> params) {
        return queryForLong(sqlId, params) > 0;
    }
}
