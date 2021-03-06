package ru.vampa.disksaver.dbservice.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Created by vampa on 08.02.2016.
 */

@Entity
@Table(name = "profile_category")
public class ProfileCategoryEntity implements Serializable{
    @Serial
    private static final long serialVersionUID = 2016020804L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 64, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 2048, nullable = false)
    private String description;

    public ProfileCategoryEntity() {}

    public ProfileCategoryEntity(@NotNull String name, @NotNull String description) {
        this.setId(-1);
        this.setName(name);
        this.setDescription(description);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
