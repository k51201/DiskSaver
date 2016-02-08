package disksaver.dbservice;

import disksaver.dbservice.dao.ElementCategoryDAO;
import disksaver.dbservice.dao.ProfileCategoryDAO;
import disksaver.dbservice.entities.CDProfilesEntity;
import disksaver.dbservice.entities.ElementCategoryEntity;
import disksaver.dbservice.entities.ElementsEntity;
import disksaver.dbservice.entities.ProfileCategoryEntity;
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
import java.util.List;

/**
 * Created by vampa on 08.02.2016.
 */
public class H2DBService implements DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update";

    private final SessionFactory sessionFactory;

    public H2DBService() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(CDProfilesEntity.class);
        configuration.addAnnotatedClass(ProfileCategoryEntity.class);
        configuration.addAnnotatedClass(ElementsEntity.class);
        configuration.addAnnotatedClass(ElementCategoryEntity.class);

        configuration.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:./h2db");
        configuration.setProperty("hibernate.connection.username", "disksaver");
        configuration.setProperty("hibernate.connection.password", "disksaver");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);

        sessionFactory = createSessionFactory(configuration);
    }

    @Override
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

    @Override
    public void close() {
        sessionFactory.close();
    }

    @Override
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

    @Override
    public long addNewProfileCategory(String categoryName, String categoryDesc) throws DBException {
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

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();

        return configuration.buildSessionFactory(serviceRegistry);
    }
}
