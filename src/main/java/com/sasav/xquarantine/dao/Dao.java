package com.sasav.xquarantine.dao;

import java.util.Collection;
import java.util.List;

public interface Dao {
    void delete(Object object);
    
    void deleteAll(Collection coll);

    <T> List<T> getAll(Class<T> clazz);

    Object getById(String clazzString, final long id);

    Object getById(String clazzString, final String id);

    List getByQuery(final String queryS, final Object[] values);

    List getBySQLQuery(final String queryS);
    
    List getByHQLQuery(final String queryS);
    
    void savaOrUpdateAll(Collection coll);

    void saveOrUpdate(Object object); 
    
    
    //specify
    Object getOneValue(final String queryS);
}
