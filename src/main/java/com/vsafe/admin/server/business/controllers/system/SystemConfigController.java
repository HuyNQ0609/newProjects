package com.vsafe.admin.server.business.controllers.system;

import com.vsafe.admin.server.business.request.system.SearchSystemConfigRequest;
import com.vsafe.admin.server.business.request.system.SystemConfigSaveRequest;
import com.vsafe.admin.server.business.services.system.SystemConfigService;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/system-config")
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    public SystemConfigController(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }

    @PostMapping("/list")
    public ResponseEntity<?> getListSysConfig(@RequestBody(required = false) SearchSystemConfigRequest request) {
        return new ResponseEntity<>(systemConfigService.searchWithPaging(request), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveSysConfig(@RequestBody SystemConfigSaveRequest request) {
        return new ResponseEntity<>(systemConfigService.saveSysConfig(request), HttpStatus.OK);
    }
}
