package com.vsafe.admin.server.business.controllers.base;

import com.vsafe.admin.server.business.request.base.AddFireFighterRequest;
import com.vsafe.admin.server.business.request.base.SearchFireFighterRequest;
import com.vsafe.admin.server.business.request.base.UpdateFireFighterRequest;
import com.vsafe.admin.server.business.services.base.FireFighterService;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/fire-fighter")
public class FireFighterController {

    private final FireFighterService fireFighterService;

    public FireFighterController(FireFighterService fireFighterService) {
        this.fireFighterService = fireFighterService;
    }

    @PostMapping("/search")
    public ResponseEntity search(@RequestBody SearchFireFighterRequest request) {
        return new ResponseEntity<>(fireFighterService.searchWithPaging(request), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody AddFireFighterRequest request) {
        return new ResponseEntity<>(fireFighterService.addFireFighter(request), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody UpdateFireFighterRequest request) {
        return new ResponseEntity<>(fireFighterService.updateFireFighter(request), HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity search(@RequestParam String id) {
        return new ResponseEntity<>(fireFighterService.getById(id), HttpStatus.OK);
    }
}
