package crm11.services;

import java.util.List;

import crm11.entity.RolesEntity;
import crm11.repository.RolesRepository;

public class RolesServices {
	private RolesRepository rolesRepository = new RolesRepository();
	
	public List<RolesEntity> getAllRoles () {
		return rolesRepository.findAllRoles();
	}
	
	public String addRoles(RolesEntity roleEntity) {
		String messages = "Insert fail!";
		
		int count = rolesRepository.addRoles(roleEntity);
		if(count > 0) {
			messages = "Insert success!";
		}
		
		return messages;
	}
	
	public String deleteByIdRole (int id) {
	    boolean result = rolesRepository.deleteByIdRole(id);

	    if(result){
	        return "Xóa thành công";
	    } else {
	        return "Xóa thất bại";
	    }
	}
	
	public RolesEntity getByIdRoles(int id) {
		return rolesRepository.findById(id);
	}
	
	public String editByIdRole(int id, String name, String description) {

	    boolean success = rolesRepository.updateRoleById(id, name, description);

	    return success ? "Update role success!" : "Update role failed!";
	}

	
}
