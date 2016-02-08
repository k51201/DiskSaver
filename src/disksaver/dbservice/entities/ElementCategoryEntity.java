package disksaver.dbservice.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by vampa on 08.02.2016.
 */

@Entity
@Table(name = "element_category")
public class ElementCategoryEntity implements Serializable{
    private static final long serialVersionUID = 2016020802L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "description", length = 2048, nullable = false)
    private String description;

    public ElementCategoryEntity() {
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
