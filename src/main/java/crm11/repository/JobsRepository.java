package crm11.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import crm11.config.MysqlConfig;
import crm11.entity.JobsEntity;
import crm11.entity.RolesEntity;
import crm11.entity.StatusEntity;
import crm11.entity.TasksEntity;
import crm11.entity.UserEntity;

public class JobsRepository {
	public List<JobsEntity> findAllJobs (){
		List<JobsEntity> listJobsEntity = new ArrayList<JobsEntity>();
		
		String query = "SELECT * FROM jobs";
		
		Connection connection = MysqlConfig.getConnection();
		try {
			PreparedStatement statments = connection.prepareStatement(query);
			ResultSet resultSet = statments.executeQuery();
			
			while(resultSet.next()){
				JobsEntity jobsEntity = new JobsEntity();
				
				jobsEntity.setId(resultSet.getInt("id"));
				jobsEntity.setName(resultSet.getString("name"));
				
			    Date startDate = resultSet.getDate("start_date");
			    Date endDate   = resultSet.getDate("end_date");

			    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			    jobsEntity.setStart_date(sdf.format(startDate));
			    jobsEntity.setEnd_date(sdf.format(endDate));
				
				listJobsEntity.add(jobsEntity);
				
			}
			
			
		} catch(Exception e) {
			System.out.println("Error findAllJobs : " + e.getMessage());
		}
		
		return listJobsEntity;
	}
	
	public int insertJobs (JobsEntity jobsEntity) {
		int count = 0;
		String query = "INSERT INTO jobs(name,start_date,end_date)"
				+ "VALUES (?,?,?)";
		Connection connection = MysqlConfig.getConnection();
		try {
			PreparedStatement statments = connection.prepareStatement(query);
			statments.setString(1, jobsEntity.getName());
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        java.util.Date start = sdf.parse(jobsEntity.getStart_date());
	        java.util.Date end   = sdf.parse(jobsEntity.getEnd_date());
	        
			statments.setDate(2, new java.sql.Date(start.getTime()));
			statments.setDate(3, new java.sql.Date(end.getTime()));
			
			count = statments.executeUpdate();
			
		}catch (Exception e) {
			System.err.println("Error InserJobs : " + e.getMessage());
		}
		
		return count;
	}
	
	public boolean deleteByIdJobs(int id) {
	    String query = "DELETE FROM jobs WHERE id = ?";

	    Connection connection = MysqlConfig.getConnection();
	    try {
	    		PreparedStatement statments = connection.prepareStatement(query);
	        statments.setInt(1, id);
	        return statments.executeUpdate() > 0;

	    } catch (Exception e) {
	        System.out.println("Delete deleteByIdJobs: " + e.getMessage());
	    }
	    return false;
	}
	
	public List<TasksEntity> findByIdTaskFromJobs(int jobId) {

	    List<TasksEntity> list = new ArrayList<>();

	    String query =
	    	    "SELECT t.id        AS task_id, " +
	    	    "       t.name      AS task_name, " +
	    	    "       t.status_id AS status_id, " +
	    	    "       s.name      AS status_name, " +
	    	    "       u.id        AS user_id, " +
	    	    "       u.fullname  AS fullname, " +
	    	    "       j.id        AS job_id, " +
	    	    "       j.name      AS job_name " +
	    	    "FROM tasks t " +
	    	    "JOIN users u  ON t.user_id = u.id " +
	    	    "JOIN status s ON t.status_id = s.id " +
	    	    "JOIN jobs j   ON t.job_id = j.id " +
	    	    "WHERE t.job_id = ? " +
	    	    "ORDER BY u.id, t.status_id";


	    Connection conn = MysqlConfig.getConnection();

	    try {
	        PreparedStatement ps = conn.prepareStatement(query);
	        ps.setInt(1, jobId);

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {

	            TasksEntity task = new TasksEntity();
	            task.setId(rs.getInt("task_id"));
	            task.setName(rs.getString("task_name"));

	            UserEntity user = new UserEntity();
	            user.setId(rs.getInt("user_id"));
	            user.setFullname(rs.getString("fullname"));
	            task.setUsers(user);

	            StatusEntity status = new StatusEntity();
	            status.setId(rs.getInt("status_id"));
	            status.setName(rs.getString("status_name"));
	            task.setStatus(status);

	            JobsEntity job = new JobsEntity();
	            job.setId(rs.getInt("job_id"));
	            job.setName(rs.getString("job_name"));
	            task.setJobs(job);

	            list.add(task);
	        }

	    } catch (Exception e) {
	        System.out.println("findTaskByJobId error: " + e.getMessage());
	    }

	    return list;
	}
	
	public List<TasksEntity> findAllIdStastus () {
		List<TasksEntity> listTask = new ArrayList<TasksEntity>();
		String query = "SELECT t.status_id, s.id,s.name "
				+ "FROM tasks t "
				+ "JOIN status s ON t.status_id = s.id";
		Connection connection = MysqlConfig.getConnection();
		try {
			PreparedStatement statment = connection.prepareStatement(query);
			ResultSet resultSet = statment.executeQuery();
			
			while(resultSet.next()) {
				TasksEntity taskEntity = new TasksEntity();
				taskEntity.setId(resultSet.getInt("status_id"));
				
				StatusEntity statusEntity = new StatusEntity();
				
				statusEntity.setId(resultSet.getInt("id"));
				statusEntity.setName(resultSet.getString("name"));
				taskEntity.setStatus(statusEntity);
				
				listTask.add(taskEntity);
			}
			
			
			
		}catch (Exception e) {
			System.out.println("Error findAllIdStastus : " + e.getMessage());
		}
		
		return listTask;
	}

	public boolean updateByJobId(JobsEntity entityJob) {

	    String sql = "UPDATE jobs SET name=?, start_date=?, end_date=? WHERE id=?";

	    Connection connection = MysqlConfig.getConnection();

	    try {
	        PreparedStatement statement = connection.prepareStatement(sql);

	        statement.setString(1, entityJob.getName());

	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	        java.util.Date start = sdf.parse(entityJob.getStart_date());
	        java.util.Date end   = sdf.parse(entityJob.getEnd_date());

	        statement.setDate(2, new java.sql.Date(start.getTime()));
	        statement.setDate(3, new java.sql.Date(end.getTime()));
	        statement.setInt(4, entityJob.getId());

	        return statement.executeUpdate() > 0;

	    } catch (Exception e) {
	        System.out.println("Error updateByJobId: " + e.getMessage());
	    }
	    return false;
	}

	
	public JobsEntity findByIdJobs(int id) {

	    String sql = "SELECT id, name, start_date, end_date FROM jobs WHERE id=?";
	    Connection conn = MysqlConfig.getConnection();
	    
	    try {
	        	PreparedStatement statment = conn.prepareStatement(sql); 
	    		statment.setInt(1, id);

	        ResultSet resultSet = statment.executeQuery();

	        if (resultSet.next()) {
	            JobsEntity jobEntity = new JobsEntity();
	            jobEntity.setId(resultSet.getInt("id"));
	            jobEntity.setName(resultSet.getString("name"));
	            
			    Date startDate = resultSet.getDate("start_date");
			    Date endDate   = resultSet.getDate("end_date");

			    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			    jobEntity.setStart_date(sdf.format(startDate));
			    jobEntity.setEnd_date(sdf.format(endDate));
			    
	            return jobEntity;
	        }

	    } catch (Exception e) {
	        System.out.println("Error findByIdJobs : " + e.getMessage());
	    }

	    return null;
	}

	
}
