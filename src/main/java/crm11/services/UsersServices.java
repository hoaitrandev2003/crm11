package crm11.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import crm11.entity.RolesEntity;
import crm11.entity.TasksEntity;
import crm11.entity.UserEntity;
import crm11.repository.RolesRepository;
import crm11.repository.TasksRepository;
import crm11.repository.UserRepository;

public class UsersServices {
	private UserRepository userRepository = new UserRepository();
	private RolesRepository roleRepository = new RolesRepository();
	private TasksRepository tasksRepository = new TasksRepository();
	
	public List<UserEntity> showUser () {
		return userRepository.findAllUser();
	}
	
    public String insertUser(UserEntity userEntity) {
    		String message = "Insert fail!";
    		
    		int count = userRepository.save(userEntity);
    		if(count > 0) {
    			message = "Insert Success";
    		}
    	
        return message;
    }
    
	public List<RolesEntity> getAllRoles () {
		return roleRepository.findAllRoles();
	}
	
	public List<TasksEntity> getAllTasks () {
		return tasksRepository.findAllTasks();
	}
	
	public List<TasksEntity> getAllByTaskIdNotYetComplete (int id) {
		List<TasksEntity> listTask = userRepository.findByUserId(id);
	    List<TasksEntity> result = new ArrayList<TasksEntity>();

	    for (TasksEntity item : listTask) {
	        if ("Chưa thực hiện".equals(item.getStatus().getName())) {
	            result.add(item);
	        }
	    }
		
		return result;
	}
	
	public List<TasksEntity> getAllByTaskIdCurrentlyInProgress (int id) {
		List<TasksEntity> listTask = userRepository.findByUserId(id);
	    List<TasksEntity> result = new ArrayList<TasksEntity>();

	    for (TasksEntity item : listTask) {
	        if (	"Đang thực hiện".equals(item.getStatus().getName())) {
	            result.add(item);
	        }
	    }
		
		return result;
	}
	
	public List<TasksEntity> getAllByTaskIdComplete (int id) {
		List<TasksEntity> listTask = userRepository.findByUserId(id);
	    List<TasksEntity> result = new ArrayList<TasksEntity>();

	    for (TasksEntity item : listTask) {
	        if ("Đã hoàn thành".equals(item.getStatus().getName())) {
	            result.add(item);
	        }
	    }
		
		return result;
	}
	
	public UserEntity getByUserId(int id) {
		return userRepository.findByIdUser(id);
	}
	
	public String deleteByIdUser(int id) {

	    boolean result = userRepository.deleteByIdUser(id);

	    if(result){
	        return "Xóa thành công";
	    } else {
	        return "Xóa thất bại";
	    }
	}
	
	public Map<String, Integer> getPercentStatusByUser(int userId) {
	    List<TasksEntity> listTask = userRepository.findTaskByUserId(userId);
	    JobsServices jobsS = new JobsServices();
	    return jobsS.getPercentStatusFromList(listTask);
	}
	
	public String editByIdUser(int id, String fullname,String email, String password, String phone, int roleId) {
		String messages = "Edit fail!";

	    if(fullname == null || fullname.trim().isEmpty())
	        return "Fullname không được để trống!";

	    if(email == null || email.trim().isEmpty())
	        return "Email không được để trống!";

	    boolean isSuccess = userRepository.updateByIdUser(
	            id, fullname, email, password, phone, roleId
	    );

	    if(isSuccess){
	        messages = "Edit success!";
	    }
		
		
		return messages;
	}

	
}