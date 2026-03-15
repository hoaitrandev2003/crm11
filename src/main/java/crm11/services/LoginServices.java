package crm11.services;

import java.util.List;

import crm11.entity.RolesEntity;
import crm11.entity.UserEntity;
import crm11.repository.RolesRepository;
import crm11.repository.UserRepository;

public class LoginServices {
	
	private UserRepository userRepository = new UserRepository();
	private RolesRepository rolesRepository = new RolesRepository();
	
	public RolesEntity checkLogin (String email, String password) {
		List<UserEntity> listUser = userRepository.findByEmailAndPassword(email, password);
		
		RolesEntity entity = null;
		
		for(UserEntity item : listUser) {
			entity = rolesRepository.findById(item.getRole().getId());
		}
		
		return  entity;
	}
	
}
