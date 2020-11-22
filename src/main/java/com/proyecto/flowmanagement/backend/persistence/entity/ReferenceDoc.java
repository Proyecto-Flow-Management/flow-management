package com.proyecto.flowmanagement.backend.persistence.entity;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import springfox.documentation.spring.web.readers.operation.OperationReader;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "step_document")
public class ReferenceDoc extends AbstractEntity{

    @Column(name = "name")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
