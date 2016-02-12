package disksaver.dbservice.dao;

import disksaver.dbservice.entity.DiskProfilesEntity;
import disksaver.dbservice.entity.ElementsEntity;
import disksaver.dbservice.entity.ProfileCategoryEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.Date;

/**
 * Created by vampa on 08.02.2016.
 */
public class DiskProfilesDAO {
    private Session session;

    public DiskProfilesDAO(Session session) {
        this.session = session;
    }

    public DiskProfilesEntity get(long id) throws HibernateException {
        return session.get(DiskProfilesEntity.class, id);
    }

    public long addProfile(String name, String volumeName, long size, String description,
                           Date modified, Date burned, long categoryId) {
        ProfileCategoryEntity profileCategory = session.load(ProfileCategoryEntity.class, categoryId);
        if (name == null)
            name = "";
        if (volumeName == null)
            volumeName = "";
        if (modified == null)
            modified = new Date();

        return (Long) session.save(
                new DiskProfilesEntity(name, volumeName, size, description, modified, burned, profileCategory)
        );
    }

    public void addElementToProfile(long profileId, long elementId) {
        DiskProfilesEntity diskProfile = session.load(DiskProfilesEntity.class, profileId);
        ElementsEntity element = session.load(ElementsEntity.class, elementId);

        diskProfile.getElements().add(element);
        session.update(diskProfile);
    }
}
