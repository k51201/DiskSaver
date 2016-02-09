package disksaver.dbservice.dao;

import disksaver.dbservice.entity.ElementCategoryEntity;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

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
        List<ElementCategoryEntity> categoriesDataSetList;
        Criteria criteria = session.createCriteria(ElementCategoryEntity.class);
        categoriesDataSetList = criteria.list();
        List<String> categories = new ArrayList<>(categoriesDataSetList.size());
        if (!categoriesDataSetList.isEmpty()) {
            categories.addAll(categoriesDataSetList.stream().map(ElementCategoryEntity::getName).collect(Collectors.toList()));
            return categories;
        } else
            return null;
    }

    public long addCategory(String name, String description) {
        return (Long) session.save(new ElementCategoryEntity(name, description));
    }
}
