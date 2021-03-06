package ru.vampa.disksaver.dbservice.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = 2016020801L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "size")
    private long size;

    @Column(name = "description", length = 2048)
    private String description;

    @Column(name = "modified", nullable = false)
    private Date modified;

    @Column(name = "burned")
    private Date burned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "id")
    private ProfileCategoryEntity category;

    @OneToMany(mappedBy = "diskProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElementsEntity> elements = new ArrayList<>();

    public DiskProfilesEntity() {}

    public DiskProfilesEntity(@NotNull String name, long size, String description,
                              @NotNull Date modified, Date burned, ProfileCategoryEntity category) {
        this.setId(-1);
        this.setName(name);
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
