package disksaver.dbservice.dao;

import disksaver.dbservice.entities.ElementCategoryEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Created by vampa on 08.02.2016.
 */
public class ElementCategoryDAO {
    private Session session;

    public ElementCategoryDAO(Session session) {
        this.session = session;
    }

    public ElementCategoryEntity get(long id) throws HibernateException {
        return session.get(ElementCategoryEntity.class, id);
    }
}
