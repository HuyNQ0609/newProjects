package com.vsafe.admin.server.business.controllers.operation;

import com.vsafe.admin.server.business.request.operation.SearchMarkerRequest;
import com.vsafe.admin.server.business.services.opearation.GgMapService;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/gg-map")
public class GgMapController {
    private final GgMapService ggMapService;

    public GgMapController(GgMapService ggMapService) {
        this.ggMapService = ggMapService;
    }

    @GetMapping("/list-group-area")
    public ResponseEntity<?> getGroupArea() {
        return new ResponseEntity<>(ggMapService.getGroupArea(), HttpStatus.OK);
    }

    @PostMapping("/list-marker")
    public ResponseEntity<?> searchListMarker(@RequestBody @Valid SearchMarkerRequest request) {
        return new ResponseEntity<>(ggMapService.searchWithoutPaging(request), HttpStatus.OK);
    }

    @GetMapping("/list-location")
    public ResponseEntity<?> getListLocation(@RequestParam String areaId) {
        return new ResponseEntity<>(ggMapService.getListLocation(areaId), HttpStatus.OK);
    }

    @GetMapping("/list-device")
    public ResponseEntity<?> searchListDevice(@RequestParam String markerId) {
        return new ResponseEntity<>(ggMapService.getListDevice(markerId), HttpStatus.OK);
    }

    @GetMapping("/list-device-status/top-rank")
    public ResponseEntity<?> getListDeviceStatusTopRank() {
        return new ResponseEntity<>(ggMapService.getListDeviceStatusTopRank(), HttpStatus.OK);
    }
}
