package crm11.services;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crm11.entity.JobsEntity;
import crm11.entity.TasksEntity;
import crm11.repository.JobsRepository;

public class JobsServices {
	JobsRepository jobRepository = new JobsRepository();
	
	public List<JobsEntity> getAllJob (){
		List<JobsEntity> listJobs = jobRepository.findAllJobs();
		
		return listJobs;
	}
	
	public String addJobs(JobsEntity jobsEntity) {
		String messages = "Insert Fail!";
		
		int count = jobRepository.insertJobs(jobsEntity);
		
		if(count > 0) {
			messages = "Insert success!";
		}
	
		return messages;
	}
	
	public List<TasksEntity> getByIdTaskFromJobs (int id) {
		return jobRepository.findByIdTaskFromJobs(id);
	}
	
	public String deleteByIdJobs (int id) {
	    boolean result = jobRepository.deleteByIdJobs(id);

	    if(result){
	        return "Xóa thành công";
	    } else {
	        return "Xóa thất bại";
	    }
	}
	
	public Map<String, Integer> getPercentStatusFromList(List<TasksEntity> listTask) {

	    Map<String, Integer> countMap = new HashMap<>();

	    for (TasksEntity task : listTask) {
	        String statusName = task.getStatus().getName();

	        countMap.put(
	            statusName,
	            countMap.getOrDefault(statusName, 0) + 1
	        );
	    }

	    int totalTask = listTask.size();
	    if (totalTask == 0) return new HashMap<>();

	    Map<String, Integer> percentMap = new HashMap<>();

	    for (String key : countMap.keySet()) {
	        int percent = (countMap.get(key) * 100) / totalTask;
	        percentMap.put(key, percent);
	    }

	    return percentMap;
	}

	public String editJob(int id, String name, String startDate, String endDate) {

	    JobsEntity job = new JobsEntity();
	    job.setId(id);
	    job.setName(name);
	    job.setStart_date(startDate);
	    job.setEnd_date(endDate);

	    boolean success = jobRepository.updateByJobId(job);

	    return success ? "Update success" : "Update fail";
	}

	public JobsEntity getByIdJobs(int id) {
		return jobRepository.findByIdJobs(id);
	}
}
