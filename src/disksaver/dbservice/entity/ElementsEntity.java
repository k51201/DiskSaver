package disksaver.dbservice.entity;

import com.sun.istack.internal.NotNull;

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

    @Column(name = "description", length = 2048)
    private String description;

    @Column(name = "size")
    private long size;

    @Column(name = "directory")
    private boolean directory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "id")
    private ElementCategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disk_profile", referencedColumnName = "id")
    private DiskProfilesEntity diskProfile;

    public ElementsEntity() {
    }

    public ElementsEntity(@NotNull String name, @NotNull String path, String description, long size,
                          boolean directory, ElementCategoryEntity category, DiskProfilesEntity diskProfile) {
        this.setId(-1);
        this.setName(name);
        this.setPath(path);
        this.setDescription(description);
        this.setSize(size);
        this.setDirectory(directory);
        this.setCategory(category);
        this.setDiskProfile(diskProfile);
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public ElementCategoryEntity getCategory() {
        return category;
    }

    public void setCategory(ElementCategoryEntity category) {
        this.category = category;
    }

    public DiskProfilesEntity getDiskProfile() {
        return diskProfile;
    }

    public void setDiskProfile(DiskProfilesEntity diskProfile) {
        this.diskProfile = diskProfile;
    }
}
