package com.vsafe.admin.server.business.controllers.operation;

import com.vsafe.admin.server.business.request.operation.Notifications;
import com.vsafe.admin.server.business.response.BaseResponse;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import com.vsafe.admin.server.helpers.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/web-socket")
public class SocketController {

    @Value("${vsafe.secret-key}")
    private String secretKey;
    private final SimpMessagingTemplate template;

    public SocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @PostMapping("/notify")
    public BaseResponse pushNotify(@Valid @RequestBody Notifications request) {
        log.info("pushNotify | request: " + request.toString());
        try {
            String sign = Utils.genSecureCodeGeneral(secretKey, request.getRequestId(),request.getRequestDate(), request.getMarkerId(), request.getStatus().toString());
            if (!sign.equals(request.getSecureCode())) {
                log.info("pushNotify | secureCode wrong | requestId: " + request.getRequestId());
                return BaseResponse.error("secureCode không khớp");
            }
            template.convertAndSend("/topic/notification", request);
            log.info("pushNotify | Done ");
            return BaseResponse.success("Đẩy dữ liệu thông báo đến client thành công", null, null);
        } catch (Exception ex) {
            log.info("pushNotify | Ex: " + ex);
            return BaseResponse.error("Lỗi hệ thống");
        }
    }

}
