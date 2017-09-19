package ru.vampa.disksaver.dbservice.dao;

import ru.vampa.disksaver.dbservice.entity.ElementCategoryEntity;
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
public class ElementCategoryDAO {
    private Session session;

    public ElementCategoryDAO(Session session) {
        this.session = session;
    }

    public ElementCategoryEntity get(long id) throws HibernateException {
        return session.get(ElementCategoryEntity.class, id);
    }

    public List<String> getCategories() {
        Criteria criteria = session.createCriteria(ElementCategoryEntity.class);
        List<ElementCategoryEntity> categoriesDataSetList = criteria.list();

        List<String> categories = new ArrayList<>(categoriesDataSetList.size());
        if (!categoriesDataSetList.isEmpty()) {
            categories.addAll(categoriesDataSetList.stream().map(ElementCategoryEntity::getName).collect(Collectors.toList()));
            return categories;
        } else
            return null;
    }

    public long addCategory(String name, String description) {
        if (name == null)
            name = "no name";
        if (description == null)
            description = "";
        return (Long) session.save(new ElementCategoryEntity(name, description));
    }

    public long getCategoryId(String name) {
        Criteria criteria = session.createCriteria(ElementCategoryEntity.class);
        ElementCategoryEntity elementCategoryEntity =
                (ElementCategoryEntity) criteria.add(Restrictions.eq("name", name)).uniqueResult();
        return elementCategoryEntity == null ? 0 : elementCategoryEntity.getId();
    }
}
