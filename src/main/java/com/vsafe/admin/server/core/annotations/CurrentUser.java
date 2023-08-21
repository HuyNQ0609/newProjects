package com.vsafe.admin.server.core.annotations;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * *   public String show(@CurrentUser CustomUser customUser) {
 * *       // do something with CustomUser
 * *       return "view";
 * *   }
 * Lấy thông tin người dùng đăng nhập hiện tại
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal
public @interface CurrentUser {

}
