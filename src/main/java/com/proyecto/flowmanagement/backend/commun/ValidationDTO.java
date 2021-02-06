package com.proyecto.flowmanagement.backend.commun;

import java.util.LinkedList;
import java.util.List;

public class ValidationDTO {

    public ValidationDTO()
    {
        this.error = new LinkedList<>();
        this.validationDTOList  = new LinkedList<>();
        this.validationDTOListSecond  = new LinkedList<>();
    }

    String label;

    private List<ValidationDTO> validationDTOList;

    private List<ValidationDTO> validationDTOListSecond;

    private List<String> error;

    public List<ValidationDTO> getValidationDTOList() {
        return validationDTOList;
    }

    public void setValidationDTOList(List<ValidationDTO> validationDTOList) {
        this.validationDTOList = validationDTOList;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }

    public void addError(String error) {
        this.error.add(error);
    }

    public void addListError(List<String> error) {
        this.error.addAll(error);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void addList(ValidationDTO validationDTO){
        this.validationDTOList.add(validationDTO);
    }

    public List<ValidationDTO> getValidationDTOListSecond() {
        return validationDTOListSecond;
    }

    public void setValidationDTOListSecond(List<ValidationDTO> validationDTOListSecond) {
        this.validationDTOListSecond = validationDTOListSecond;
    }

    public void addListSecond(ValidationDTO validationDTO){
        this.validationDTOListSecond.add(validationDTO);
    }
}
