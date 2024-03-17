package ru.edu.Task5.instance;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class InstanceData {
    private String instanceId;
    private ArrayList<String> registerId;
    private ArrayList<String> supplementaryAgreementId;

    public InstanceData() {
        this.registerId = new ArrayList<>();
        this.supplementaryAgreementId = new ArrayList<>();
    }
    public void setAgrId(String id) {
        this.supplementaryAgreementId.add( id);
    }
    public void setRegId(String id) {
        this.registerId.add( id);
    }
}