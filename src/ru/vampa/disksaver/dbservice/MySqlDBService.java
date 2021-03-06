package ru.vampa.disksaver.dbservice;

import org.hibernate.cfg.Configuration;
import ru.vampa.disksaver.dbservice.entity.DiskProfilesEntity;
import ru.vampa.disksaver.dbservice.entity.ElementCategoryEntity;
import ru.vampa.disksaver.dbservice.entity.ElementsEntity;
import ru.vampa.disksaver.dbservice.entity.ProfileCategoryEntity;

/**
 * Created by vampa on 09.02.2016.
 */
public class MySqlDBService extends DBService{
    private static final String hibernate_show_sql = "false";
    private static final String hibernate_hbm2ddl_auto = "update";

    @Override
    protected Configuration getConfiguration() {
        final var configuration = new Configuration();

        configuration.addAnnotatedClass(DiskProfilesEntity.class);
        configuration.addAnnotatedClass(ProfileCategoryEntity.class);
        configuration.addAnnotatedClass(ElementsEntity.class);
        configuration.addAnnotatedClass(ElementCategoryEntity.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/disksaver");
        configuration.setProperty("hibernate.connection.username", "disksaver");
        configuration.setProperty("hibernate.connection.password", "rhjkbregjkysqgbpltw111");
        configuration.setProperty("hibernate.c3p0.max_statements", "800");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }
}
