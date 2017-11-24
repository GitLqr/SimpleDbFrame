package com.lqr.simpledbframe.customer.db;

import java.util.List;

/**
 * 基本的数据操作
 */
public interface IBaseDao<M> {

    Long insert(M entity);

    Integer delete(M where);

    Integer update(M entitiy, M where);

    List<M> query(M where);

    List<M> query(M where, String orderBy);

    List<M> query(M where, String orderBy, Integer page, Integer pageCount);

}
