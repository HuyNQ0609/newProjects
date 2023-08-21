package com.vsafe.admin.server.business.controllers.system;

import com.vsafe.admin.server.business.request.system.AdministrativeUnitFilter;
import com.vsafe.admin.server.business.request.system.AdministrativeUnitRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.services.system.AdministrativeUnitService;
import com.vsafe.admin.server.core.annotations.HideResponseLog;
import com.vsafe.admin.server.core.annotations.TrackExecutionTime;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import com.vsafe.admin.server.helpers.enums.responseStatus.ResponseStatus;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vsafe.admin.server.core.exceptions.SystemException;;
import javax.validation.Valid;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/administrative-unit")
public class AdministrativeUnitController {
    private final AtomicBoolean importDataProcessing = new AtomicBoolean(false);

    @PostMapping(value = "import-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @HideResponseLog
    public ResponseEntity<?> importData(@RequestPart("file") MultipartFile file) {
        if (!importDataProcessing.get()) {
            importDataProcessing.set(true);
            try {
                administrativeUnitService.insertData(file);
                importDataProcessing.set(false);
            } catch (Exception e) {
                importDataProcessing.set(false);
                return new ResponseEntity<>(BaseResponse.error(e.getMessage()), HttpStatus.OK);
            }
            return new ResponseEntity<>(BaseResponse.success("Import dữ liệu thành công"), HttpStatus.OK);
        }
        return new ResponseEntity<>(BaseResponse.error(ResponseStatus.PROCESSING), HttpStatus.OK);
    }

    @PostMapping("list")
    @TrackExecutionTime
    public ResponseEntity<?> list(@RequestBody @Valid AdministrativeUnitFilter filter) {
        return new ResponseEntity<>(BaseResponse.success(administrativeUnitService.list(filter)), HttpStatus.OK);
    }

    @PostMapping("flat-list")
    @TrackExecutionTime
    public ResponseEntity<?> listAll(@RequestBody @Valid AdministrativeUnitFilter filter) {
        return new ResponseEntity<>(BaseResponse.success(administrativeUnitService.listAll(filter)), HttpStatus.OK);
    }

    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody AdministrativeUnitRequest request) throws SystemException {
        return new ResponseEntity<>(BaseResponse.success(administrativeUnitService.save(request)), HttpStatus.OK);
    }

    @GetMapping("detail/{code}")
    public ResponseEntity<?> detail(@PathVariable("code") String code) {
        return new ResponseEntity<>(BaseResponse.success(administrativeUnitService.detail(code)), HttpStatus.OK);
    }

    @GetMapping("list/by-type-parent")
    public ResponseEntity<?> getListByTypeAndParent(@Valid @RequestParam Integer type, @RequestParam(required = false) String parentCode) {
        return new ResponseEntity<>(administrativeUnitService.getListByTypeAndParent(type, parentCode), HttpStatus.OK);
    }

    private final AdministrativeUnitService administrativeUnitService;
    public AdministrativeUnitController(AdministrativeUnitService administrativeUnitService) {
        AssertUtils.defaultNotNull(administrativeUnitService);
        this.administrativeUnitService = administrativeUnitService;
    }
}
