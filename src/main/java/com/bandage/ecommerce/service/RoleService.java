package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.RoleResponse;
import com.bandage.ecommerce.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(
            RoleRepository roleRepository
    ) {
        this.roleRepository = roleRepository;
    }

    public List<RoleResponse> getRoles() {

        return roleRepository
                .findAll()
                .stream()
                .map(role ->
                        RoleResponse.builder()
                                .id(role.getId())
                                .name(role.getName())
                                .build()
                )
                .toList();
    }
}