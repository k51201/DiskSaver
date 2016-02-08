package disksaver.dbservice;

import disksaver.dbservice.entities.CDProfilesEntity;
import disksaver.dbservice.entities.ElementCategoryEntity;
import disksaver.dbservice.entities.ElementsEntity;
import disksaver.dbservice.entities.ProfileCategoryEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;

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
            SessionImplementor sessionImplementor = (SessionImplementor) sessionFactory.getCurrentSession();
            Connection connection = sessionImplementor.getJdbcConnectionAccess().obtainConnection();
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
