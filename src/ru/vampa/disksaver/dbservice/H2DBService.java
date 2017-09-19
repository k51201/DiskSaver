package ru.vampa.disksaver.dbservice;

import ru.vampa.disksaver.dbservice.entity.DiskProfilesEntity;
import ru.vampa.disksaver.dbservice.entity.ElementCategoryEntity;
import ru.vampa.disksaver.dbservice.entity.ElementsEntity;
import ru.vampa.disksaver.dbservice.entity.ProfileCategoryEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.service.ServiceRegistry;

/**
 * Created by vampa on 08.02.2016.
 */
public class H2DBService extends DBService {
    private static final String hibernate_show_sql = "false";
    private static final String hibernate_hbm2ddl_auto = "update";

    @Override
    protected Configuration getConfiguration() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(DiskProfilesEntity.class);
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

        return configuration;
    }
}
