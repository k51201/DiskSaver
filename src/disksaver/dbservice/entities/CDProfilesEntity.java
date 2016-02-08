package disksaver.dbservice.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vampa on 08.02.2016.
 */

@Entity
@Table(name = "cd_profiles")
public class CDProfilesEntity implements Serializable{
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

    @Column(name = "modification_date", nullable = false)
    private Date modificationDate;

    @Column(name = "burned_at_date", nullable = false)
    private Date burnedAtDate;

    @ManyToOne
    @JoinColumn (name = "category", referencedColumnName = "id")
    private ProfileCategoryEntity profileCategory;

    @OneToMany(mappedBy = "diskProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElementsEntity> elements = new ArrayList<>();

    public CDProfilesEntity() {
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

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Date getBurnedAtDate() {
        return burnedAtDate;
    }

    public void setBurnedAtDate(Date burnedAtDate) {
        this.burnedAtDate = burnedAtDate;
    }

    public ProfileCategoryEntity getProfileCategory() {
        return profileCategory;
    }

    public void setProfileCategory(ProfileCategoryEntity profileCategory) {
        this.profileCategory = profileCategory;
    }

    public List<ElementsEntity> getElements() {
        return elements;
    }

    public void setElements(List<ElementsEntity> elements) {
        this.elements = elements;
    }

}
