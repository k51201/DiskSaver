package ru.vampa.disksaver.dbservice;

import org.hibernate.HibernateException;

/**
 * Created by vampa on 09.02.2016.
 */
public class DBException extends Throwable {
    public DBException(Throwable cause) {
        super(cause);
    }
}
