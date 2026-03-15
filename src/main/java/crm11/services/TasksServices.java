package crm11.services;

import java.util.List;

import crm11.entity.JobsEntity;
import crm11.entity.TasksEntity;
import crm11.entity.UserEntity;
import crm11.repository.JobsRepository;
import crm11.repository.TasksRepository;
import crm11.repository.UserRepository;

public class TasksServices {
	private TasksRepository tasksRepository = new TasksRepository();
	private JobsRepository jobsRepository = new JobsRepository();
	private UserRepository userRepository = new UserRepository();
	
	public List<TasksEntity> getAllTasks (){
		List<TasksEntity> listTasks = tasksRepository.findAllTasks();
		return listTasks;
	}
	
	public List<JobsEntity> getAllJobs () {
		List<JobsEntity> listJobs = jobsRepository.findAllJobs();
		return listJobs;
	}
	
	public List<UserEntity> getAllUsers () {
		List<UserEntity> listUser = userRepository.findAllUser();
		return listUser;
	}
	
	public String addTasks (TasksEntity taskEntity) {
		String messages = "Insert Fail!";
		
		int count = tasksRepository.saveTask(taskEntity);
		
		if(count > 0) {
			messages = "Insert success!";
		}
		
		return messages;
	}
	
	public String deleteByIdTask(int id) {
		boolean result = tasksRepository.deleteByIdTask(id);

	    if(result){
	        return "Xóa thành công";
	    } else {
	        return "Xóa thất bại";
	    }
	}
	
	public TasksEntity getTaskById(int id) {
	    return tasksRepository.findById(id);
	}
	
	public String updateTask(int id, String name, String start,String end, int userId, int jobId) {

		TasksEntity task = new TasksEntity();
		task.setId(id);
		task.setName(name);
		task.setStart_date(start);
		task.setEnd_date(end);
		
		UserEntity user = new UserEntity();
		user.setId(userId);
		task.setUsers(user);
		
		JobsEntity job = new JobsEntity();
		job.setId(jobId);
		task.setJobs(job);
		
		
		boolean result = tasksRepository.updateTask(task);
		
		if (result) {
			return "Cập nhật công việc thành công!";
		} else { 
			return "Cập nhật công việc thất bại!";
		}
	}

}
