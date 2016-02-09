package disksaver.dbservice;

import disksaver.dbservice.dao.DiskProfilesDAO;
import disksaver.dbservice.dao.ElementCategoryDAO;
import disksaver.dbservice.dao.ElementsDAO;
import disksaver.dbservice.dao.ProfileCategoryDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by vampa on 08.02.2016.
 */
public abstract class DBService {
    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    protected abstract Configuration getConfiguration();

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public void printConnectInfo() {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Connection connection = ((SessionImplementor) session).getJdbcConnectionAccess().obtainConnection();
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
            transaction.commit();
            session.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        sessionFactory.close();
    }

    public List<String> getProfileCategories() throws DBException {
        try {
            Session session = sessionFactory.openSession();

            ProfileCategoryDAO profileCategoryDAO = new ProfileCategoryDAO(session);
            List<String> categories = profileCategoryDAO.getCategories();

            session.close();
            return categories;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addProfileCategory(String categoryName, String categoryDesc) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            ProfileCategoryDAO profileCategoryDAO = new ProfileCategoryDAO(session);
            long id = profileCategoryDAO.addCategory(categoryName, categoryDesc);

            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public List<String> getElementCategories() throws DBException {
        try {
            Session session = sessionFactory.openSession();

            ElementCategoryDAO elementCategoryDAO = new ElementCategoryDAO(session);
            List<String> categories = elementCategoryDAO.getCategories();

            session.close();
            return categories;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addElementCategory(String categoryName, String categoryDesc) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            ElementCategoryDAO elementCategoryDAO = new ElementCategoryDAO(session);
            long id = elementCategoryDAO.addCategory(categoryName, categoryDesc);

            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addDiskProfile(String name, String volumeName, long size, String description,
                               Date modified, Date burned, long categoryId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            DiskProfilesDAO diskProfilesDAO = new DiskProfilesDAO(session);
            long id = diskProfilesDAO.addProfile(name, volumeName, size, description, modified, burned, categoryId);

            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addElement(String name, String path, String description, long size,
                           boolean directory, long categoryId, long profileId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            ElementsDAO elementsDAO = new ElementsDAO(session);
            long id = elementsDAO.addElement(name, path, description, size, directory, categoryId, profileId);

            DiskProfilesDAO diskProfilesDAO = new DiskProfilesDAO(session);
            diskProfilesDAO.addElementToProfile(profileId, id);

            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }
}
