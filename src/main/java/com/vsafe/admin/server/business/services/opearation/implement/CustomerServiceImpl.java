package com.vsafe.admin.server.business.services.opearation.implement;

import com.vsafe.admin.server.business.entities.operation.CustomerEntity;
import com.vsafe.admin.server.business.repositories.cms.operation.ICustomerRepositoryCustom;
import com.vsafe.admin.server.business.repositories.cms.operation.ICustomerRepositoryJpa;
import com.vsafe.admin.server.business.repositories.cms.system.IAdministrativeUnitRepositoryJpa;
import com.vsafe.admin.server.business.request.operation.CustomerDetailRequest;
import com.vsafe.admin.server.business.request.operation.SearchCustomerRequest;
import com.vsafe.admin.server.business.request.system.CreateUserRequest;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.business.response.UploadFileResponse;
import com.vsafe.admin.server.business.response.operation.CustomerResponse;
import com.vsafe.admin.server.business.response.system.UserDetailResponse;
import com.vsafe.admin.server.business.services.common.ExcelService;
import com.vsafe.admin.server.business.services.common.FileService;
import com.vsafe.admin.server.business.services.common.UserDetailService;
import com.vsafe.admin.server.business.services.opearation.CustomerService;
import com.vsafe.admin.server.business.services.system.AuthService;
import com.vsafe.admin.server.helpers.constants.BusinessConstant;
import com.vsafe.admin.server.helpers.constants.Constants;
import com.vsafe.admin.server.helpers.enums.UserType;
import com.vsafe.admin.server.helpers.enums.responseStatus.ResponseStatus;
import com.vsafe.admin.server.helpers.utils.Utils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.vsafe.admin.server.helpers.constants.SystemConstants.ROLE_CUSTOMER;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final UserDetailService userDetailService;
    @Value("${resource.upload.public.cdn}")
    private String pathDir;

    @Value("${system.password-customer}")
    private String passwordDefault;

    private static final String PREFIX_CUSTOMER = "customer/customer_id_";

    private final ICustomerRepositoryCustom iCustomerRepositoryCustom;
    private final ICustomerRepositoryJpa iCustomerRepositoryJpa;
    private final ExcelService excelService;
    private final FileService fileService;
    private final IAdministrativeUnitRepositoryJpa administrativeUnitRepo;
    private final AuthService authService;

    public CustomerServiceImpl(UserDetailService userDetailService, ICustomerRepositoryCustom iCustomerRepositoryCustom, ICustomerRepositoryJpa iCustomerRepositoryJpa, ExcelService excelService, FileService fileService, IAdministrativeUnitRepositoryJpa administrativeUnitRepo, AuthService authService) {
        this.userDetailService = userDetailService;
        this.iCustomerRepositoryCustom = iCustomerRepositoryCustom;
        this.iCustomerRepositoryJpa = iCustomerRepositoryJpa;
        this.excelService = excelService;
        this.fileService = fileService;
        this.administrativeUnitRepo = administrativeUnitRepo;
        this.authService = authService;
    }

    @Override
    public BaseResponse searchWithPaging(SearchCustomerRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        List<CustomerEntity> customerEntityList = iCustomerRepositoryCustom.searchWithPaging(request, clientId);
        long total = iCustomerRepositoryCustom.countSearch(request, clientId);
        Map<String, Long> dataTotal = new HashMap<>();
        dataTotal.put("total", total);
        return BaseResponse.success("Lấy dữ liệu thành công", customerEntityList, dataTotal);
    }

    @Override
    public BaseResponse searchWithoutPaging(SearchCustomerRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        return BaseResponse.success(iCustomerRepositoryCustom.searchWithoutPaging(request, clientId));
    }

    @Override
    public BaseResponse getDetail(String id) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        CustomerEntity customerEntity = iCustomerRepositoryJpa.findFirstByIdAndClientId(id, clientId);
        if (customerEntity != null) {
            customerEntity.setProvince(administrativeUnitRepo.findFirstByCode(customerEntity.getProvinceCode()));
            customerEntity.setDistrict(administrativeUnitRepo.findFirstByCode(customerEntity.getDistrictCode()));
            customerEntity.setWard(administrativeUnitRepo.findFirstByCode(customerEntity.getWardCode()));
        }
        return BaseResponse.success(customerEntity);
    }

    @Override
    public byte[] export(SearchCustomerRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        List<CustomerEntity> customerEntities = iCustomerRepositoryCustom.searchWithoutPaging(request, clientId);
        List<CustomerResponse> customerResponses = CustomerResponse.of(customerEntities);
        return excelService.writeExcel(customerResponses, new ExcelService.ExcelExportInfo("templates/customer.xlsx", "customer", 1, 1));
    }

    @Override
    public BaseResponse updateInfo(CustomerDetailRequest request) {
        String id = request.getId();
        CustomerEntity customerEntity = iCustomerRepositoryJpa.findById(id).orElse(null);
        if (Objects.isNull(customerEntity)) {
            return BaseResponse.error("Thông tin khách hàng không hợp lệ");
        }
        String folder = PREFIX_CUSTOMER + id;
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
                customerEntity.setAvatar(uploadAvt.getFileDownloadUri());
            }
        }
        if (!request.getPhoneNumber().equals(customerEntity.getPhoneNumber())) {
            List<CustomerEntity> customerEntitiesExist = iCustomerRepositoryCustom.searchByPhoneNumber(request.getPhoneNumber(), customerEntity.getClientId());
            if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
                return BaseResponse.error("Thông tin Số điện thoại đã được sử dụng");
            else {
                //Xóa acc sđt cũ, tạo mới acc bên SSO
                BaseResponse updateUserNameResponse = updateUserName(customerEntity, request.getPhoneNumber());
                if (!ResponseStatus.SUCCESS.name().equals(updateUserNameResponse.getMessageCode()))
                    return updateUserNameResponse;
            }
        }
        if (!request.getEmail().equals(customerEntity.getEmail())) {
            List<CustomerEntity> customerEntitiesExist = iCustomerRepositoryCustom.searchByEmail(request.getEmail(), customerEntity.getClientId());
            if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
                return BaseResponse.error("Thông tin Email đã được sử dụng");
        }
        customerEntity.setName(request.getName());
        customerEntity.setPhoneNumber(request.getPhoneNumber());
        customerEntity.setEmail(request.getEmail());
        customerEntity.setProvinceCode(request.getProvinceCode());
        customerEntity.setDistrictCode(request.getDistrictCode());
        customerEntity.setWardCode(request.getWardCode());
        customerEntity.setAddress(request.getAddress());
        customerEntity.setUserName(request.getPhoneNumber());
        customerEntity.setIdentityNumber(request.getIdentityNumber());
        customerEntity.setGender(request.getGender());
        customerEntity.setBirthday(request.getBirthday());
        customerEntity.setCustomerType(request.getCustomerType());
        iCustomerRepositoryJpa.save(customerEntity);
        return BaseResponse.success("Chỉnh sửa thông tin thành công", customerEntity, null);
    }

    @Override
    public BaseResponse updateMemberInfo(CustomerDetailRequest request) {
        String id = request.getId();
        CustomerEntity customerEntity = iCustomerRepositoryJpa.findById(id).orElse(null);
        if (Objects.isNull(customerEntity)) {
            return BaseResponse.error("Thông tin khách hàng không hợp lệ");
        }

        if (!request.getPhoneNumber().equals(customerEntity.getPhoneNumber())) {
            List<CustomerEntity> customerEntitiesExist = iCustomerRepositoryCustom.searchByPhoneNumber(request.getPhoneNumber(), customerEntity.getClientId());
            if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
                return BaseResponse.error("Thông tin Số điện thoại đã được sử dụng");
            else {
                //Xóa acc sđt cũ, tạo mới acc bên SSO
                BaseResponse updateUserNameResponse = updateUserName(customerEntity, request.getPhoneNumber());
                if (!ResponseStatus.SUCCESS.name().equals(updateUserNameResponse.getMessageCode()))
                    return updateUserNameResponse;
            }
        }
        if (!request.getEmail().equals(customerEntity.getEmail())) {
            List<CustomerEntity> customerEntitiesExist = iCustomerRepositoryCustom.searchByEmail(request.getEmail(), customerEntity.getClientId());
            if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
                return BaseResponse.error("Thông tin Email đã được sử dụng");
        }
        customerEntity.setName(request.getName());
        customerEntity.setPhoneNumber(request.getPhoneNumber());
        customerEntity.setEmail(request.getEmail());
        customerEntity.setProvinceCode(request.getProvinceCode());
        customerEntity.setDistrictCode(request.getDistrictCode());
        customerEntity.setWardCode(request.getWardCode());
        customerEntity.setAddress(request.getAddress());
        customerEntity.setUserName(request.getPhoneNumber());
        customerEntity.setIdentityNumber(request.getIdentityNumber());
        customerEntity.setGender(request.getGender());
        customerEntity.setBirthday(request.getBirthday());
        if (customerEntity.getIsAccMain() != 1)
            customerEntity.setRole(request.getRole());
        iCustomerRepositoryJpa.save(customerEntity);
        return BaseResponse.success("Chỉnh sửa thông tin thành công", customerEntity, null);
    }

    @Override
    public BaseResponse addMemberInfo(CustomerDetailRequest request) {
        CustomerEntity customerEntity = new CustomerEntity();

        CustomerEntity customerEntityParent = iCustomerRepositoryJpa.findById(request.getParentId()).orElse(null);
        if (Objects.isNull(customerEntityParent)) {
            return BaseResponse.error("Thông tin đơn vị quản lý không hợp lệ");
        }

        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();

        //Kiểm tra thông tin SĐT/Email/CCCDd đã được sử dụng hay chưa?
        List<CustomerEntity> customerEntitiesExist = iCustomerRepositoryCustom.searchByPhoneNumber(request.getPhoneNumber(), clientId);
        if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
            return BaseResponse.error("Thông tin Số điện thoại đã được sử dụng");

        customerEntitiesExist = iCustomerRepositoryCustom.searchByEmail(request.getEmail(), clientId);
        if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
            return BaseResponse.error("Thông tin Email đã được sử dụng");

        if (StringUtils.isNotEmpty(request.getIdentityNumber())) {
            customerEntitiesExist = iCustomerRepositoryCustom.searchByIdentityNumber(request.getIdentityNumber(), clientId);
            if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
                return BaseResponse.error("Thông tin Giấy tờ định danh đã được sử dụng");
        }

        customerEntity.setName(request.getName());
        customerEntity.setPhoneNumber(request.getPhoneNumber());
        customerEntity.setEmail(request.getEmail());
        customerEntity.setProvinceCode(request.getProvinceCode());
        customerEntity.setDistrictCode(request.getDistrictCode());
        customerEntity.setWardCode(request.getWardCode());
        customerEntity.setAddress(request.getAddress());
        customerEntity.setUserName(request.getPhoneNumber());
        customerEntity.setIdentityNumber(request.getIdentityNumber());
        customerEntity.setGender(request.getGender());
        customerEntity.setBirthday(request.getBirthday());
        customerEntity.setGender(request.getGender());
        customerEntity.setClientId(clientId);
        customerEntity.setCustomerType(request.getCustomerType());
        customerEntity.setStatus(request.getStatus());
        customerEntity.setIsAccMain(BusinessConstant.CUSTOMER_ACCOUNT.EXTRA_ACC);
        customerEntity.setVerificationType(BusinessConstant.VERIFICATION_TYPE.SMS);
        customerEntity.setParentId(customerEntityParent.getId());
        customerEntity.setRole(request.getRole());

        //Khởi tạo thông tin tài khoản
        BaseResponse createUserResponse = createUserName(customerEntity);

        if (!ResponseStatus.SUCCESS.name().equals(createUserResponse.getMessageCode()))
            return createUserResponse;

        iCustomerRepositoryJpa.save(customerEntity);
        return BaseResponse.success("Thêm thành viên thành công", customerEntity, null);
    }

    @Override
    public BaseResponse saveInfoStatus(CustomerDetailRequest request) {
        String id = request.getId();
        CustomerEntity customerEntity = iCustomerRepositoryJpa.findById(id).orElse(null);
        if (Objects.isNull(customerEntity)) {
            return BaseResponse.error("Thông tin khách hàng không hợp lệ");
        }
        customerEntity.setStatus(request.getStatus());
        iCustomerRepositoryJpa.save(customerEntity);
        return BaseResponse.success("Chỉnh sửa thông tin thành công", customerEntity, request);
    }

    @Override
    public BaseResponse saveTwoFactor(CustomerDetailRequest request) {
        String id = request.getId();

        CustomerEntity customerEntity = iCustomerRepositoryJpa.findById(id).orElse(null);
        if (Objects.isNull(customerEntity)) {
            return BaseResponse.error("Thông tin khách hàng không hợp lệ");
        }
        customerEntity.setVerificationType(request.getVerificationType());
        iCustomerRepositoryJpa.save(customerEntity);
        return BaseResponse.success("Cài đặt xác thực 2 lớp thành công", customerEntity, request);
    }

    @Override
    public BaseResponse addCustomer(CustomerDetailRequest request) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();

        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setName(request.getName());
        customerEntity.setPhoneNumber(request.getPhoneNumber());
        customerEntity.setEmail(request.getEmail());
        customerEntity.setProvinceCode(request.getProvinceCode());
        customerEntity.setDistrictCode(request.getDistrictCode());
        customerEntity.setWardCode(request.getWardCode());
        customerEntity.setAddress(request.getAddress());
        customerEntity.setIdentityNumber(request.getIdentityNumber());
        customerEntity.setGender(request.getGender());
        customerEntity.setBirthday(request.getBirthday());
        customerEntity.setClientId(clientId);
        customerEntity.setCustomerType(request.getCustomerType());
        customerEntity.setStatus(request.getStatus());
        customerEntity.setUserName(request.getPhoneNumber());
        customerEntity.setIsAccMain(BusinessConstant.CUSTOMER_ACCOUNT.MAIN_ACC);
        customerEntity.setRole(ROLE_CUSTOMER.ADMIN);
        customerEntity.setVerificationType(BusinessConstant.VERIFICATION_TYPE.SMS);

        //Kiểm tra thông tin SĐT/Email/CCCDd đã được sử dụng hay chưa?
        List<CustomerEntity> customerEntitiesExist = iCustomerRepositoryCustom.searchByPhoneNumber(request.getPhoneNumber(), clientId);
        if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
            return BaseResponse.error("Thông tin Số điện thoại đã được sử dụng");

        customerEntitiesExist = iCustomerRepositoryCustom.searchByEmail(request.getEmail(), clientId);
        if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
            return BaseResponse.error("Thông tin Email đã được sử dụng");

        if (StringUtils.isNotEmpty(request.getIdentityNumber())) {
            customerEntitiesExist = iCustomerRepositoryCustom.searchByIdentityNumber(request.getIdentityNumber(), clientId);
            if (customerEntitiesExist != null && customerEntitiesExist.size() > 0)
                return BaseResponse.error("Thông tin Giấy tờ định danh đã được sử dụng");
        }

        //Khởi tạo thông tin tài khoản
        BaseResponse createUserResponse = createUserName(customerEntity);

        if (!ResponseStatus.SUCCESS.name().equals(createUserResponse.getMessageCode()))
            return createUserResponse;

        customerEntity = iCustomerRepositoryJpa.save(customerEntity);
        customerEntity.setParentId(customerEntity.getId());

        //Kiểm tra file ảnh đại diện
        String folder = PREFIX_CUSTOMER + customerEntity.getId();
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
                customerEntity.setAvatar(uploadAvt.getFileDownloadUri());
            }
        }

        return BaseResponse.success(iCustomerRepositoryJpa.save(customerEntity));
    }

    private BaseResponse createUserName(CustomerEntity customerEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        String tokenValue = auth2AuthenticationDetails.getTokenValue();
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUserType(UserType.CUSTOMER.getValue());
        createUserRequest.setPassword(passwordDefault);
        createUserRequest.setUserName(customerEntity.getPhoneNumber());
        createUserRequest.setStatus(BusinessConstant.Status.ACTIVE);
        createUserRequest.setToken(tokenValue);
        return this.authService.addUser(createUserRequest);
    }

    private BaseResponse updateUserName(CustomerEntity customerEntity, String userNameNew) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        String tokenValue = auth2AuthenticationDetails.getTokenValue();
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUserType(UserType.CUSTOMER.getValue());
        createUserRequest.setPassword(passwordDefault);
        createUserRequest.setUserName(userNameNew);
        createUserRequest.setUserNameOld(customerEntity.getPhoneNumber());
        createUserRequest.setStatus(BusinessConstant.Status.ACTIVE);
        createUserRequest.setToken(tokenValue);
        return this.authService.updateUserName(createUserRequest);
    }

    @Override
    public BaseResponse listByParent(String parentId) {
        UserDetailResponse userDetailResponse = userDetailService.getCurrentUser();
        String clientId = Objects.equals(userDetailResponse.getUserType(), UserType.INTERNAL.getValue()) ? userDetailResponse.getEmployeeInfo().getClientId() : userDetailResponse.getCustomerInfo().getClientId();
        List<CustomerEntity> customerEntities = iCustomerRepositoryCustom.findByParentIdAndClientId(parentId, clientId);
        return BaseResponse.success("Thành công", customerEntities, customerEntities.size());
    }
}
