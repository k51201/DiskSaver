package ru.vampa.disksaver.dbservice.dao;

import ru.vampa.disksaver.dbservice.entity.ProfileCategoryEntity;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vampa on 08.02.2016.
 */
public class ProfileCategoryDAO {
    private final Session session;

    public ProfileCategoryDAO(Session session) {
        this.session = session;
    }

    public ProfileCategoryEntity get(long id) throws HibernateException {
        return session.get(ProfileCategoryEntity.class, id);
    }

    public List<String> getCategories() {
        Criteria criteria = session.createCriteria(ProfileCategoryEntity.class);
        List<ProfileCategoryEntity> categoriesDataSetList = criteria.list();

        List<String> categories = new ArrayList<>(categoriesDataSetList.size());
        if (!categoriesDataSetList.isEmpty()) {
            categories.addAll(categoriesDataSetList.stream().map(ProfileCategoryEntity::getName).collect(Collectors.toList()));
            return categories;
        } else
            return null;
    }

    public long addCategory(String name, String description) {
        if (name == null)
            name = "no name";
        if (description == null)
            description = "";
        return (Long) session.save(new ProfileCategoryEntity(name, description));
    }

    public long getCategoryId(String name) {
        Criteria criteria = session.createCriteria(ProfileCategoryEntity.class);
        ProfileCategoryEntity profileCategoryEntity =
                (ProfileCategoryEntity) criteria.add(Restrictions.eq("name", name)).uniqueResult();
        return profileCategoryEntity == null ? 0 : profileCategoryEntity.getId();
    }
}
