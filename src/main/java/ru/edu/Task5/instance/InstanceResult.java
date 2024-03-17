package ru.edu.Task5.instance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstanceResult {
    private String message;
    private InstanceData data;

    public InstanceResult() {
        data = new InstanceData();
    }
}