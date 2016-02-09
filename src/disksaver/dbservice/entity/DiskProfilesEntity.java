package disksaver.dbservice.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vampa on 08.02.2016.
 */

@Entity
@Table(name = "disk_profiles")
public class DiskProfilesEntity implements Serializable{
    private static final long serialVersionUID = 2016020801L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "volume_name", length = 64, nullable = false)
    private String volumeName;

    @Column(name = "size")
    private long size;

    @Column(name = "description", length = 2048, nullable = false)
    private String description;

    @Column(name = "modified", nullable = false)
    private Date modified;

    @Column(name = "burned", nullable = false)
    private Date burned;

    @ManyToOne
    @JoinColumn (name = "category", referencedColumnName = "id")
    private ProfileCategoryEntity category;

    @OneToMany(mappedBy = "diskProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElementsEntity> elements = new ArrayList<>();

    public DiskProfilesEntity() {
    }

    public DiskProfilesEntity(String name, String volumeName, long size, String description,
                              Date modified, Date burned, ProfileCategoryEntity category) {
        this.setId(-1);
        this.setName(name);
        this.setVolumeName(volumeName);
        this.setSize(size);
        this.setDescription(description);
        this.setModified(modified);
        this.setBurned(burned);
        this.setCategory(category);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getBurned() {
        return burned;
    }

    public void setBurned(Date burned) {
        this.burned = burned;
    }

    public ProfileCategoryEntity getCategory() {
        return category;
    }

    public void setCategory(ProfileCategoryEntity category) {
        this.category = category;
    }

    public List<ElementsEntity> getElements() {
        return elements;
    }

    public void setElements(List<ElementsEntity> elements) {
        this.elements = elements;
    }

}
