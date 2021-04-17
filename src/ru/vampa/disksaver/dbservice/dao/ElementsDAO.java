package ru.vampa.disksaver.dbservice.dao;

import ru.vampa.disksaver.dbservice.entity.DiskProfilesEntity;
import ru.vampa.disksaver.dbservice.entity.ElementCategoryEntity;
import ru.vampa.disksaver.dbservice.entity.ElementsEntity;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Created by vampa on 08.02.2016.
 */
public class ElementsDAO {
    private final Session session;

    public ElementsDAO(Session session) {
        this.session = session;
    }

    public ElementsEntity get(long id) throws HibernateException {
        return session.get(ElementsEntity.class, id);
    }

    public long addElement(String name, String path, String description, long size,
                           boolean directory, long categoryId, long profileId) {
        ElementCategoryEntity category = session.load(ElementCategoryEntity.class, categoryId);
        DiskProfilesEntity profile = session.load(DiskProfilesEntity.class, profileId);
        if (name == null)
            name = "";
        if (path == null)
            path = "";

        return (Long) session.save(new ElementsEntity(name, path, description, size, directory, category, profile));
    }
}
