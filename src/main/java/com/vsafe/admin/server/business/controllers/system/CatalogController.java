package com.vsafe.admin.server.business.controllers.system;

import com.vsafe.admin.server.business.request.system.AdministrativeUnitFilter;
import com.vsafe.admin.server.business.request.system.CatalogFilter;
import com.vsafe.admin.server.business.request.system.CatalogRequest;
import com.vsafe.admin.server.business.request.system.CatalogTypeRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.services.system.CatalogService;
import com.vsafe.admin.server.core.annotations.HideResponseLog;
import com.vsafe.admin.server.core.annotations.TrackExecutionTime;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vsafe.admin.server.core.exceptions.SystemException;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/catalogs")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        AssertUtils.defaultNotNull(catalogService);
        this.catalogService = catalogService;
    }

    @GetMapping("/type/list")
    @TrackExecutionTime
    public ResponseEntity<?> listCatalogType() {
        return new ResponseEntity<>(BaseResponse.success(catalogService.getCatalogTypeALl()), HttpStatus.OK);
    }

    @PostMapping("/type/save")
    public ResponseEntity<?> saveCatalogType(@RequestBody @Valid CatalogTypeRequest request) throws SystemException {
        return new ResponseEntity<>(BaseResponse.success(catalogService.save(request)), HttpStatus.OK);
    }

    @PostMapping("/type/{id}/change-state")
    public ResponseEntity<?> changeCatalogTypeState(@PathVariable("id") String id) throws SystemException {
        return new ResponseEntity<>(BaseResponse.success(catalogService.changeCatalogTypeState(id)), HttpStatus.OK);
    }

    @PostMapping("/list")
    @TrackExecutionTime
    public ResponseEntity<?> listCatalog(@RequestBody @Valid CatalogFilter filter) {
        return new ResponseEntity<>(BaseResponse.success(catalogService.getCatalogList(filter)), HttpStatus.OK);
    }

    @GetMapping("/type/{id}")
    @TrackExecutionTime
    @HideResponseLog
    public ResponseEntity<?> getCatalogByType(@PathVariable(name = "id") String catalogTypeId, @RequestParam(required = false) String name, @RequestParam(required = false) String parentId) {
        CatalogFilter filter = new CatalogFilter(name, catalogTypeId, null, parentId);
        return new ResponseEntity<>(BaseResponse.success(catalogService.getCatalogList(filter)), HttpStatus.OK);
    }

    @PostMapping("/{id}/change-state")
    public ResponseEntity<?> changeCatalogState(@PathVariable("id") String id) throws SystemException {
        return new ResponseEntity<>(BaseResponse.success(catalogService.changeCatalogState(id)), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCatalog(@RequestBody @Valid CatalogRequest request) throws SystemException {
        return new ResponseEntity<>(BaseResponse.success(catalogService.save(request)), HttpStatus.OK);
    }
}
