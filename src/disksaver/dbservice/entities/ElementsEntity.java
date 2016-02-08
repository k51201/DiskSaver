package disksaver.dbservice.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by vampa on 08.02.2016.
 */

@Entity
@Table(name = "elements")
public class ElementsEntity implements Serializable{
    private static final long serialVersionUID = 2016020803L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "path", length = 256, nullable = false)
    private String path;

    @Column(name = "description", length = 2048, nullable = false)
    private String description;

    @Column(name = "size")
    private int size;

    @ManyToOne
    @JoinColumn (name = "category_id", referencedColumnName = "id")
    private ElementCategoryEntity elementCategory;

    @ManyToOne
    @JoinColumn (name = "disk_profile_id", referencedColumnName = "id")
    private CDProfilesEntity diskProfileId;

    public ElementsEntity() {
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ElementCategoryEntity getElementCategory() {
        return elementCategory;
    }

    public void setElementCategory(ElementCategoryEntity elementCategory) {
        this.elementCategory = elementCategory;
    }

    public CDProfilesEntity getDiskProfileId() {
        return diskProfileId;
    }

    public void setDiskProfileId(CDProfilesEntity diskProfileId) {
        this.diskProfileId = diskProfileId;
    }
}
