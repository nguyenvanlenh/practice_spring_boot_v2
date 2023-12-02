package com.watermelon.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.watermelon.model.entity.Role;
import com.watermelon.repository.RoleRepository;
import com.watermelon.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Optional<Role> findByNameRole(String nameRole) {
		// TODO Auto-generated method stub
		return roleRepository.findByNameRole(nameRole);
	}

}
