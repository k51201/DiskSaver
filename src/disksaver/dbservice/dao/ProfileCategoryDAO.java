package disksaver.dbservice.dao;

import disksaver.dbservice.entities.ProfileCategoryEntity;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return (Long) session.save(new ProfileCategoryEntity(name, description));
    }
}
