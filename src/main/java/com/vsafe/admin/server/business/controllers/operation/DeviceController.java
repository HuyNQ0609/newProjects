package com.vsafe.admin.server.business.controllers.operation;

import com.vsafe.admin.server.business.services.opearation.DeviceService;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/device")
public class DeviceController {

    private final DeviceService deviceService;
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
    @GetMapping("/list-by-customer/{id}")
    public ResponseEntity<?> saveTwoFactor(@PathVariable String id) {
        return new ResponseEntity<>(deviceService.searchByCustomer(id), HttpStatus.OK);
    }
}
