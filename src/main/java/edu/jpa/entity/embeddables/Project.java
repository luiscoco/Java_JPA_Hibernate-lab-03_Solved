package edu.jpa.entity.embeddables;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Project implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
