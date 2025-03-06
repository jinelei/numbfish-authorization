package com.jinelei.iotgenius.auth.provider.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jinelei.iotgenius.auth.api.permission.PermissionApi;
import com.jinelei.iotgenius.auth.dto.permission.PermissionRequest;
import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/permission")
public class PermissionController implements PermissionApi{

    @Override
    @PostMapping("create")
    public PermissionResponse create(@RequestBody PermissionRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    @PostMapping("delete")
    public void delete(Long permissionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    @PostMapping("update")
    public PermissionResponse update(Long permissionId, PermissionRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    @PostMapping("get")
    public PermissionResponse get(Long permissionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    @PostMapping("list")
    public List<PermissionResponse> list() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'list'");
    }
    

}
