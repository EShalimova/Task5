package ru.edu.Task5.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.edu.Task5.instance.InstanceCreate;
import ru.edu.Task5.instance.InstanceResult;
import ru.edu.Task5.service.InstanceService;

@RestController
public class RestInstance {
    private final InstanceService instanceService;

    @Autowired
    public RestInstance(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    @RequestMapping(value = "/corporate-settlement-instance/create", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public ResponseEntity<Object> createInstance(@RequestBody @Valid InstanceCreate instanceCreate, BindingResult result) {
        InstanceResult instanceResult = new InstanceResult();
        int iError;

        if (result.hasErrors()) {
            for( ObjectError er : result.getAllErrors()) {
                instanceResult.setMessage( er.getDefaultMessage());
                return ResponseEntity.status( 400).body( instanceResult);
            }
        }

        iError = instanceService.setProduct( instanceCreate, instanceResult);

        if (iError != 200) {
            instanceResult.setMessage(instanceService.getMessage());
            return ResponseEntity.status(iError).body(instanceResult);
        }

        return ResponseEntity.ok( instanceResult);
    }
}