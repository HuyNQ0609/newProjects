package com.vsafe.admin.server.business.controllers.operation;

import com.vsafe.admin.server.business.request.operation.CustomerDetailRequest;
import com.vsafe.admin.server.business.request.operation.SearchCustomerRequest;
import com.vsafe.admin.server.business.services.opearation.CustomerService;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import com.vsafe.admin.server.helpers.utils.file.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/area")
public class AreaController {

}
