package com.watermelon.service;

import java.util.Optional;

import com.watermelon.model.entity.Role;

public interface RoleService {
	Optional<Role> findByNameRole(String nameRole);
}
