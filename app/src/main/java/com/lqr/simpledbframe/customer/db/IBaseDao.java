package com.lqr.simpledbframe.customer.db;

import java.util.List;

public interface IBaseDao<M> {

    long insert(M entity);

    int remove(M where);

    int update(M entity, M where);

    List<M> query(M where);

    List<M> query(M where, String orderBy);

    List<M> query(M where, String orderBy, Integer page, Integer pageCount);

}
