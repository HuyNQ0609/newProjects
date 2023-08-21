package com.vsafe.admin.server.business.services.system.implement;

import com.vsafe.admin.server.business.entities.operation.CustomerEntity;
import com.vsafe.admin.server.business.entities.system.EmployeeEntity;
import com.vsafe.admin.server.business.entities.system.EmployeeRoleEntity;
import com.vsafe.admin.server.business.entities.system.RoleEntity;
import com.vsafe.admin.server.business.repositories.cms.system.IEmployeeRepositoryCustom;
import com.vsafe.admin.server.business.repositories.cms.system.IEmployeeRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.IEmployeeRoleRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.IRoleRepositoryJpa;
import com.vsafe.admin.server.business.request.system.CreateUserRequest;
import com.vsafe.admin.server.business.request.system.EmployeeDetailRequest;
import com.vsafe.admin.server.business.request.system.SearchEmployeeRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.response.UploadFileResponse;
import com.vsafe.admin.server.business.response.system.UserDetailResponse;
import com.vsafe.admin.server.business.services.common.FileService;
import com.vsafe.admin.server.business.services.common.UserDetailService;
import com.vsafe.admin.server.business.services.system.AuthService;
import com.vsafe.admin.server.business.services.system.EmployeeService;
import com.vsafe.admin.server.helpers.constants.BusinessConstant;
import com.vsafe.admin.server.helpers.constants.Constants;
import com.vsafe.admin.server.helpers.enums.UserType;
import com.vsafe.admin.server.helpers.enums.responseStatus.ResponseStatus;
import com.vsafe.admin.server.helpers.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.vsafe.admin.server.helpers.constants.BusinessConstant.Status.ACTIVE;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    @Value("${resource.upload.public.cdn}")
    private String pathDir;
    private final FileService fileService;

    @Value("${system.password-employee}")
    private String passwordDefault;

    private static final String PREFIX_EMPLOYEE = "employee/employee_id_";

    private final UserDetailService userDetailService;
    private final IEmployeeRepositoryCustom iEmployeeRepositoryCustom;
    private final IEmployeeRepositoryJpa iEmployeeRepositoryJpa;
    private final IEmployeeRoleRepositoryJpa iEmployeeRoleRepositoryJpa;
    private final IRoleRepositoryJpa iRoleRepositoryJpa;
    private final AuthService authService;

    public EmployeeServiceImpl(FileService fileService, UserDetailService userDetailService, IEmployeeRepositoryCustom iEmployeeRepositoryCustom, IEmployeeRepositoryJpa iEmployeeRepositoryJpa, IEmployeeRoleRepositoryJpa iEmployeeRoleRepositoryJpa, IRoleRepositoryJpa iRoleRepositoryJpa, AuthService authService) {
        this.fileService = fileService;
        this.userDetailService = userDetailService;
        this.iEmployeeRepositoryCustom = iEmployeeRepositoryCustom;
        this.iEmployeeRepositoryJpa = iEmployeeRepositoryJpa;
        this.iEmployeeRoleRepositoryJpa = iEmployeeRoleRepositoryJpa;
        this.iRoleRepositoryJpa = iRoleRepositoryJpa;
        this.authService = authService;
    }

    @Override
    public BaseResponse searchWithPaging(SearchEmployeeRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        List<EmployeeEntity> employeeEntitiesList = iEmployeeRepositoryCustom.searchWithPaging(request, clientId);
        if (employeeEntitiesList != null && employeeEntitiesList.size() > 0) {
            for (EmployeeEntity employeeEntity : employeeEntitiesList) {
                List<EmployeeRoleEntity> employeeRoleEntities = iEmployeeRoleRepositoryJpa.findByEmployeeId(employeeEntity.getId());
                if (employeeRoleEntities != null && employeeRoleEntities.size() > 0) {
                    List<RoleEntity> roleEntityList = new ArrayList<>();
                    List<String> roleIds = new ArrayList<>();
                    RoleEntity roleEntityItem;
                    for (EmployeeRoleEntity employeeRoleEntity : employeeRoleEntities) {
                        roleEntityItem = iRoleRepositoryJpa.findFirstByIdAndClientId(employeeRoleEntity.getRoleId(), employeeEntity.getClientId());
                        if (roleEntityItem != null) {
                            roleEntityList.add(roleEntityItem);
                            roleIds.add(roleEntityItem.getId());
                        }
                    }
                    employeeEntity.setRoles(roleIds);
                    employeeEntity.setRoleModels(roleEntityList);
                }
            }
        }
        long total = iEmployeeRepositoryCustom.countSearch(request, clientId);
        Map<String, Long> dataTotal = new HashMap<>();
        dataTotal.put("total", total);
        return BaseResponse.success("Lấy dữ liệu thành công", employeeEntitiesList, dataTotal);
    }

    @Override
    public BaseResponse updateInfo(EmployeeDetailRequest request) {
        String id = request.getId();
        EmployeeEntity employeeEntity = iEmployeeRepositoryJpa.findById(id).orElse(null);
        if (Objects.isNull(employeeEntity)) {
            return BaseResponse.error("Thông tin nhân viên không hợp lệ");
        }
        String folder = PREFIX_EMPLOYEE + id;
        String _avt = "avatar";
        MultipartFile fileAvt = request.getAvatar();
        if (fileAvt != null) {
            if (!Utils.validFileType(Collections.singletonList(fileAvt), Arrays.asList(Constants.CONTENT_TYPE_FILE.JPEG,
                    Constants.CONTENT_TYPE_FILE.PNG, Constants.CONTENT_TYPE_FILE.JPG))) {
                return BaseResponse.error("Định dạng file không hợp lệ");
            }
            UploadFileResponse uploadAvt =
                    fileService.storeFile(fileAvt, pathDir, folder + "/" + _avt,
                            fileAvt.getName() + "." +
                                    FilenameUtils.getExtension(fileAvt.getOriginalFilename()), false);
            if (Objects.nonNull(uploadAvt)) {
                employeeEntity.setAvatar(uploadAvt.getFileDownloadUri());
            }
        }

        if (!request.getEmail().equals(employeeEntity.getEmail())) {
            List<EmployeeEntity> customerEntitiesExist = iEmployeeRepositoryJpa.findByEmailAndClientId(request.getEmail(), employeeEntity.getClientId());
            if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
                return BaseResponse.error("Thông tin Email đã được sử dụng");
        }
        employeeEntity.setFullName(request.getFullName());
        employeeEntity.setEmail(request.getEmail());
        employeeEntity.setProvinceCode(request.getProvinceCode());
        employeeEntity.setDistrictCode(request.getDistrictCode());
        employeeEntity.setWardCode(request.getWardCode());
        employeeEntity.setAddress(request.getAddress());
        employeeEntity.setIdentityNumber(request.getIdentityNumber());
        employeeEntity.setGender(request.getGender());
        employeeEntity.setBirthday(request.getBirthday());
        iEmployeeRepositoryJpa.save(employeeEntity);

        List<EmployeeRoleEntity> employeeRoleEntities = iEmployeeRoleRepositoryJpa.findByEmployeeId(employeeEntity.getId());
        iEmployeeRoleRepositoryJpa.deleteAll(employeeRoleEntities);
        employeeRoleEntities = new ArrayList<>();
        EmployeeRoleEntity employeeRoleEntityNew;
        List<RoleEntity> roleEntityList = new ArrayList<>();
        RoleEntity roleEntityItem;
        if (request.getRoles() != null && request.getRoles().length > 0) {
            for (int i = 0; i < request.getRoles().length; i++) {
                employeeRoleEntityNew = new EmployeeRoleEntity();
                roleEntityItem = iRoleRepositoryJpa.findFirstByIdAndClientId(request.getRoles()[i], employeeEntity.getClientId());
                employeeRoleEntityNew.setEmployeeId(employeeEntity.getId());
                employeeRoleEntityNew.setRoleId(request.getRoles()[i]);
                employeeRoleEntityNew.setClientId(employeeEntity.getClientId());
                employeeRoleEntities.add(employeeRoleEntityNew);
                roleEntityList.add(roleEntityItem);
            }
            iEmployeeRoleRepositoryJpa.saveAll(employeeRoleEntities);
            employeeEntity.setRoles(Arrays.asList(request.getRoles()));
            employeeEntity.setRoleModels(roleEntityList);
        }

        return BaseResponse.success("Chỉnh sửa thông tin thành công", employeeEntity, null);
    }

    @Override
    public BaseResponse insertInfo(EmployeeDetailRequest request) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();

        List<EmployeeEntity> customerEntitiesExist = iEmployeeRepositoryJpa.findByPhoneNumberAndClientId(request.getPhoneNumber(), clientId);
        if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
            return BaseResponse.error("Thông tin Số điện thoại đã được sử dụng");

        customerEntitiesExist = iEmployeeRepositoryJpa.findByEmailAndClientId(request.getEmail(), clientId);
        if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
            return BaseResponse.error("Thông tin Email đã được sử dụng");

        if (StringUtils.isNotEmpty(request.getIdentityNumber())) {
            customerEntitiesExist = iEmployeeRepositoryJpa.findByIdentityNumberAndClientId(request.getIdentityNumber(), clientId);
            if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
                return BaseResponse.error("Thông tin Giấy tờ định danh đã được sử dụng");
        }

        employeeEntity.setFullName(request.getFullName());
        employeeEntity.setEmail(request.getEmail());
        employeeEntity.setPhoneNumber(request.getPhoneNumber());
        employeeEntity.setUserName(request.getPhoneNumber());
        employeeEntity.setClientId(clientId);
        employeeEntity.setProvinceCode(request.getProvinceCode());
        employeeEntity.setDistrictCode(request.getDistrictCode());
        employeeEntity.setWardCode(request.getWardCode());
        employeeEntity.setAddress(request.getAddress());
        employeeEntity.setIdentityNumber(request.getIdentityNumber());
        employeeEntity.setGender(request.getGender());
        employeeEntity.setBirthday(request.getBirthday());
        employeeEntity.setStatus(ACTIVE);

        //Khởi tạo thông tin tài khoản
        BaseResponse createUserResponse = createUserName(employeeEntity);

        if (!ResponseStatus.SUCCESS.name().equals(createUserResponse.getMessageCode()))
            return createUserResponse;

        employeeEntity = iEmployeeRepositoryJpa.save(employeeEntity);

        String folder = PREFIX_EMPLOYEE + employeeEntity.getId();
        String _avt = "avatar";
        MultipartFile fileAvt = request.getAvatar();
        if (fileAvt != null) {
            if (!Utils.validFileType(Collections.singletonList(fileAvt), Arrays.asList(Constants.CONTENT_TYPE_FILE.JPEG,
                    Constants.CONTENT_TYPE_FILE.PNG, Constants.CONTENT_TYPE_FILE.JPG))) {
                return BaseResponse.error("Định dạng file không hợp lệ");
            }
            UploadFileResponse uploadAvt =
                    fileService.storeFile(fileAvt, pathDir, folder + "/" + _avt,
                            fileAvt.getName() + "." +
                                    FilenameUtils.getExtension(fileAvt.getOriginalFilename()), false);
            if (Objects.nonNull(uploadAvt)) {
                employeeEntity.setAvatar(uploadAvt.getFileDownloadUri());
            }
        }

        employeeEntity = iEmployeeRepositoryJpa.save(employeeEntity);

        List<EmployeeRoleEntity> employeeRoleEntities = new ArrayList<>();
        EmployeeRoleEntity employeeRoleEntityNew;
        List<RoleEntity> roleEntityList = new ArrayList<>();
        RoleEntity roleEntityItem;
        if (request.getRoles() != null && request.getRoles().length > 0) {
            for (int i = 0; i < request.getRoles().length; i++) {
                employeeRoleEntityNew = new EmployeeRoleEntity();
                roleEntityItem = iRoleRepositoryJpa.findFirstByIdAndClientId(request.getRoles()[i], employeeEntity.getClientId());
                employeeRoleEntityNew.setEmployeeId(employeeEntity.getId());
                employeeRoleEntityNew.setRoleId(request.getRoles()[i]);
                employeeRoleEntityNew.setClientId(employeeEntity.getClientId());
                employeeRoleEntities.add(employeeRoleEntityNew);
                roleEntityList.add(roleEntityItem);
            }
            iEmployeeRoleRepositoryJpa.saveAll(employeeRoleEntities);
            employeeEntity.setRoles(Arrays.asList(request.getRoles()));
            employeeEntity.setRoleModels(roleEntityList);
        }

        return BaseResponse.success("Thêm mới thông tin thành công", employeeEntity, null);
    }

    @Override
    public BaseResponse saveInfoStatus(EmployeeDetailRequest request) {
        String id = request.getId();
        EmployeeEntity employeeEntity = iEmployeeRepositoryJpa.findById(id).orElse(null);
        if (Objects.isNull(employeeEntity)) {
            return BaseResponse.error("Thông tin không hợp lệ");
        }
        employeeEntity.setStatus(request.getStatus());
        iEmployeeRepositoryJpa.save(employeeEntity);
        return BaseResponse.success("Chỉnh sửa thông tin thành công", employeeEntity, request);
    }

    private BaseResponse createUserName(EmployeeEntity employeeEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        String tokenValue = auth2AuthenticationDetails.getTokenValue();
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUserType(UserType.INTERNAL.getValue());
        createUserRequest.setPassword(passwordDefault);
        createUserRequest.setUserName(employeeEntity.getPhoneNumber());
        createUserRequest.setStatus(ACTIVE);
        createUserRequest.setToken(tokenValue);
        return this.authService.addUser(createUserRequest);
    }
}
