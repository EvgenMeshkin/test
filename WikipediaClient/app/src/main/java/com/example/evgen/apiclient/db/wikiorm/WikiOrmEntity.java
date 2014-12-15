package com.example.evgen.apiclient.db.wikiorm;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by User on 15.12.2014.
 */
public abstract class WikiOrmEntity {

//    protected static <T extends WikiOrmEntity> List<T> getAllEntities(Class<T> entityClass) throws IllegalAccessException, InstantiationException {
//        return getAllEntities(entityClass, false);
//    }

//    protected static <T extends WikiOrmEntity> List<T> getAllEntities(Class<T> entityClass, boolean includeLeftChild) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
//        return false;//OrmUtils.getAllEntities(entityClass, includeLeftChild);
//    }

//    protected static OrmWhere Where(Class<? extends WikiOrmEntity> entityClass){
//        return new OrmWhere(entityClass);
//    }

    public boolean alter() {
        return false;//WikiOrmUtils.alter(this);
    }

    public static boolean alterInTransaction(WikiOrmEntity entity) throws IllegalAccessException {
        return false;//OrmUtils.alterInTransaction(entity);
    }

    protected boolean delete()  {
        return false;//WikiOrmUtils.delete(this);
    }
}