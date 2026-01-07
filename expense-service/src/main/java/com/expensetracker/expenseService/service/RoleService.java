package com.expensetracker.expenseService.service;

import com.expensetracker.expenseService.dto.RoleDTO;
import com.expensetracker.expenseService.entity.Role;
import com.expensetracker.expenseService.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public RoleDTO createRole(RoleDTO roleDTO) {
        if (roleRepository.findByName(roleDTO.getName()).isPresent()) {
            throw new BadRequestException("Role with name " + roleDTO.getName() + " already exists");
        }
        
        Role role = new Role();
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        
        Role saved = roleRepository.save(role);
        return convertToDTO(saved);
    }

    public RoleDTO updateRole(Long roleId, RoleDTO roleDTO) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BadRequestException("Role not found"));
        
        if (roleDTO.getName() != null && !roleDTO.getName().equals(role.getName())) {
            if (roleRepository.findByName(roleDTO.getName()).isPresent()) {
                throw new BadRequestException("Role with name " + roleDTO.getName() + " already exists");
            }
            role.setName(roleDTO.getName());
        }
        
        if (roleDTO.getDescription() != null) {
            role.setDescription(roleDTO.getDescription());
        }
        
        Role updated = roleRepository.save(role);
        return convertToDTO(updated);
    }

    public void deleteRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new BadRequestException("Role not found");
        }
        roleRepository.deleteById(roleId);
    }

    public RoleDTO getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BadRequestException("Role not found"));
        return convertToDTO(role);
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO getRoleByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new BadRequestException("Role not found"));
        return convertToDTO(role);
    }

    private RoleDTO convertToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setRoleId(role.getRoleId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        return dto;
    }
}
