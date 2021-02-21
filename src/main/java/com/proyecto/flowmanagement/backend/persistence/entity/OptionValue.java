package com.proyecto.flowmanagement.backend.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name= "option_value_parameter")
public class OptionValue extends AbstractEntity  implements Serializable {

    @Column(name = "option_value_name")
    private String optionValueName;

    public String getOptionValueName() {
        return optionValueName;
    }

    public void setOptionValueName(String optionValueName) {
        this.optionValueName = optionValueName;
    }
}
