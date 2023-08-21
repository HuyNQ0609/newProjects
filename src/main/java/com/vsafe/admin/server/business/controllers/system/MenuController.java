package com.vsafe.admin.server.business.controllers.system;

import com.vsafe.admin.server.business.entities.system.MenuEntity;
import com.vsafe.admin.server.business.request.system.PermissionRoleRequest;
import com.vsafe.admin.server.business.request.system.SearchMenuRequest;
import com.vsafe.admin.server.business.services.system.MenuService;
import com.vsafe.admin.server.core.configurations.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE + "/menu")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/list-all")
    @PreAuthorize("hasPermission('MENU_MANAGEMENT','VIEW')")
    public ResponseEntity getListAll() {
        return new ResponseEntity<>(menuService.searchWithoutPaging(new SearchMenuRequest()), HttpStatus.OK);
    }

    @GetMapping("/search/{value}")
    @PreAuthorize("hasPermission('MENU_MANAGEMENT','VIEW')")
    public ResponseEntity searchMenu(@PathVariable String value) {
        if (StringUtils.isEmpty(value))
            return new ResponseEntity<>(menuService.searchWithoutPaging(new SearchMenuRequest()), HttpStatus.OK);
        return new ResponseEntity<>(menuService.searchWithoutPaging(value), HttpStatus.OK);
    }

    @PostMapping("/permission-to-role")
    @PreAuthorize("hasPermission('MENU_MANAGEMENT','UPDATE')")
    public ResponseEntity setPermissionToRole(@Valid @RequestBody PermissionRoleRequest request) {
        return new ResponseEntity<>(menuService.setPermissionToRole(request), HttpStatus.OK);
    }


    @PostMapping("/update")
    @PreAuthorize("hasPermission('MENU_MANAGEMENT','UPDATE')")
    public ResponseEntity updateMenu(@RequestBody MenuEntity request) {
        return new ResponseEntity<>(menuService.updateMenu(request), HttpStatus.OK);
    }

    @PostMapping("/state")
    @PreAuthorize("hasPermission('MENU_MANAGEMENT','UPDATE')")
    public ResponseEntity updateStateMenu(@RequestBody MenuEntity request) {
        return new ResponseEntity<>(menuService.updateStateMenu(request), HttpStatus.OK);
    }


    @GetMapping("/role-by-menu/{menuId}")
    public ResponseEntity getRoleOfMenu(@PathVariable String menuId) {
        return new ResponseEntity<>(menuService.getRoleOfMenu(menuId), HttpStatus.OK);
    }

    @GetMapping("/menu-by-user")
    public ResponseEntity getMenuByUserLogin() {
        return new ResponseEntity<>(menuService.getMenuByUserLogin(), HttpStatus.OK);
    }
}