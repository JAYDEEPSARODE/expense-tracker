package com.expensetracker.expenseService.controller;

import com.expensetracker.expenseService.dto.RoleDTO;
import com.expensetracker.expenseService.global.ResponseDTO;
import com.expensetracker.expenseService.service.RoleService;
import com.expensetracker.expenseService.Utility.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createRole(HttpServletRequest request, @RequestBody RoleDTO roleDTO) {
        String endPoint = "/roles";
        RoleDTO created = roleService.createRole(roleDTO);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(created, endPoint), HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<ResponseDTO> updateRole(HttpServletRequest request, 
                                                   @PathVariable Long roleId,
                                                   @RequestBody RoleDTO roleDTO) {
        String endPoint = "/roles/" + roleId;
        RoleDTO updated = roleService.updateRole(roleId, roleDTO);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(updated, endPoint), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ResponseDTO> deleteRole(HttpServletRequest request, @PathVariable Long roleId) {
        String endPoint = "/roles/" + roleId;
        roleService.deleteRole(roleId);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("Role deleted successfully", endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<ResponseDTO> getRoleById(HttpServletRequest request, @PathVariable Long roleId) {
        String endPoint = "/roles/" + roleId;
        RoleDTO role = roleService.getRoleById(roleId);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(role, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAllRoles(HttpServletRequest request) {
        String endPoint = "/roles";
        List<RoleDTO> roles = roleService.getAllRoles();
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(roles, endPoint), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseDTO> getRoleByName(HttpServletRequest request, @PathVariable String name) {
        String endPoint = "/roles/name/" + name;
        RoleDTO role = roleService.getRoleByName(name);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(role, endPoint), HttpStatusCode.valueOf(200));
    }
}
