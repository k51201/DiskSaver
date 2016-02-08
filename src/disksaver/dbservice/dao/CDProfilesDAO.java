package disksaver.dbservice.dao;

import disksaver.dbservice.entities.CDProfilesEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Created by vampa on 08.02.2016.
 */
public class CDProfilesDAO {
    private Session session;

    public CDProfilesDAO(Session session) {
        this.session = session;
    }

    public CDProfilesEntity get(long id) throws HibernateException {
        return session.get(CDProfilesEntity.class, id);
    }
}
