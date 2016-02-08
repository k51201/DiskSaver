package disksaver.dbservice.dao;

import disksaver.dbservice.entities.ElementsEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Created by vampa on 08.02.2016.
 */
public class ElementsDAO {
    private Session session;

    public ElementsDAO(Session session) {
        this.session = session;
    }

    public ElementsEntity get(long id) throws HibernateException {
        return session.get(ElementsEntity.class, id);
    }
}
