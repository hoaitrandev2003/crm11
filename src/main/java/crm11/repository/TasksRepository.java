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
import crm11.entity.StatusEntity;
import crm11.entity.TasksEntity;
import crm11.entity.UserEntity;

public class TasksRepository {
	public List<TasksEntity> findAllTasks(){
		List<TasksEntity> listTasks = new ArrayList<TasksEntity>();
		String query = "SELECT t.id, t.name, t.start_date, "
					+ "t.end_date,u.fullname,j.name AS job_name, s.name AS status_name "
					+ "FROM tasks t "
					+ "JOIN users u ON t.user_id = u.id "
					+ "JOIN jobs j ON t.job_id = j.id "
					+ "JOIN status s ON t.status_id = s.id";
		
		Connection connection = MysqlConfig.getConnection();
		
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				TasksEntity tasksEntity = new TasksEntity();
				
				tasksEntity.setId(resultSet.getInt("id"));
				tasksEntity.setName(resultSet.getString("name"));
				
			    Date startDate = resultSet.getDate("start_date");
			    Date endDate   = resultSet.getDate("end_date");

			    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			    tasksEntity.setStart_date(sdf.format(startDate));
			    tasksEntity.setEnd_date(sdf.format(endDate));
				
				UserEntity userEntity = new UserEntity();
				userEntity.setFullname(resultSet.getString("fullname"));
				tasksEntity.setUsers(userEntity);
				
				JobsEntity jobsEntity = new JobsEntity();
				jobsEntity.setName(resultSet.getString("job_name"));
				tasksEntity.setJobs(jobsEntity);
				
				StatusEntity statusEntity = new StatusEntity();
				statusEntity.setName(resultSet.getString("status_name"));
				tasksEntity.setStatus(statusEntity);

				listTasks.add(tasksEntity);
			}
			
		} catch (Exception e) {
			System.out.println("Error findAllTasks : " + e.getMessage());
		}
		
		return listTasks;
	}
	
	public int saveTask (TasksEntity tasksEntity) {
		int count = 0;
		
		String query = "INSERT INTO tasks(name,start_date,end_date,user_id,job_id,status_id) "
				+ "VALUES (?,?,?,?,?,?)";
		
		Connection connection = MysqlConfig.getConnection();
		
		try {
			PreparedStatement statments = connection.prepareStatement(query);
			statments.setString(1, tasksEntity.getName());
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        java.util.Date start = sdf.parse(tasksEntity.getStart_date());
	        java.util.Date end   = sdf.parse(tasksEntity.getEnd_date());
	        
	        statments.setDate(2, new java.sql.Date(start.getTime()));
			statments.setDate(3, new java.sql.Date(end.getTime()));
			
			statments.setInt(4, tasksEntity.getUsers().getId());
			statments.setInt(5, tasksEntity.getJobs().getId());
			statments.setInt(6, 1);
			
			count = statments.executeUpdate();
			
		}catch (Exception e) {
			System.out.println("Error saveTask : " + e.getMessage());
		}
		
		return count;
	}
	
	public boolean deleteByIdTask(int id) {
	    String query = "DELETE FROM tasks WHERE id = ?";

	    Connection connection = MysqlConfig.getConnection();
	    try {
	    		PreparedStatement statments = connection.prepareStatement(query);
	        statments.setInt(1, id);
	        return statments.executeUpdate() > 0;

	    } catch (Exception e) {
	        System.out.println("Delete deleteByIdTask: " + e.getMessage());
	    }
	    return false;
	}
	
	public TasksEntity findById(int id) {

	    String sql = "SELECT t.id, t.name, t.start_date, t.end_date, " +
                "u.id AS user_id, u.fullname, " +
                "j.id AS job_id, j.name AS job_name, " +
                "s.id AS status_id, s.name AS status_name " +
                "FROM tasks t " +
                "JOIN users u ON t.user_id = u.id " +
                "JOIN jobs j ON t.job_id = j.id " +
                "JOIN status s ON t.status_id = s.id " +
                "WHERE t.id = ?";
	    Connection conn = MysqlConfig.getConnection();
	    try 
	          {
	    		PreparedStatement statment = conn.prepareStatement(sql);
	    		statment.setInt(1, id);

	        ResultSet resultSet = statment.executeQuery();

	        if (resultSet.next()) {
	        		TasksEntity tasksEntity = new TasksEntity();
				
				tasksEntity.setId(resultSet.getInt("id"));
				tasksEntity.setName(resultSet.getString("name"));
				
			    Date startDate = resultSet.getDate("start_date");
			    Date endDate   = resultSet.getDate("end_date");

			    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			    tasksEntity.setStart_date(sdf.format(startDate));
			    tasksEntity.setEnd_date(sdf.format(endDate));
				
				UserEntity userEntity = new UserEntity();
				userEntity.setId(resultSet.getInt("user_id"));
				userEntity.setFullname(resultSet.getString("fullname"));
				tasksEntity.setUsers(userEntity);
				
				JobsEntity jobsEntity = new JobsEntity();
				jobsEntity.setId(resultSet.getInt("job_id"));
				jobsEntity.setName(resultSet.getString("job_name"));
				tasksEntity.setJobs(jobsEntity);

				return tasksEntity;
	        }

	    } catch (Exception e) {
	        System.out.println("findById Task error: " + e.getMessage());
	    }

	    return null;
	}
	
	public boolean updateTask(TasksEntity task) {

	    String sql = "UPDATE tasks SET name = ?, start_date = ?, end_date = ?, " +
	                 "user_id = ?, job_id = ? WHERE id = ?";

	    Connection conn = MysqlConfig.getConnection();

	    try {
	        PreparedStatement statments = conn.prepareStatement(sql);

	        statments.setString(1, task.getName());

	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        java.util.Date start = sdf.parse(task.getStart_date());
	        java.util.Date end   = sdf.parse(task.getEnd_date());

	        statments.setDate(2, new java.sql.Date(start.getTime()));
	        statments.setDate(3, new java.sql.Date(end.getTime()));

	        statments.setInt(4, task.getUsers().getId());
	        statments.setInt(5, task.getJobs().getId());
	        statments.setInt(6, task.getId());

	        return statments.executeUpdate() > 0;

	    } catch (Exception e) {
	        System.out.println("Error updateTask: " + e.getMessage());
	    }

	    return false;
	}

}
