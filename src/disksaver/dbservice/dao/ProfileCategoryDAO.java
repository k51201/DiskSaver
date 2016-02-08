package disksaver.dbservice.dao;

import disksaver.dbservice.entities.ProfileCategoryEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Created by vampa on 08.02.2016.
 */
public class ProfileCategoryDAO {
    private Session session;

    public ProfileCategoryDAO(Session session) {
        this.session = session;
    }

    public ProfileCategoryEntity get(long id) throws HibernateException {
        return session.get(ProfileCategoryEntity.class, id);
    }
}
