package com.itlike.service;

import com.itlike.domain.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getPermissions();

    List<Permission> getPermissionByRid(Long rid);
}
