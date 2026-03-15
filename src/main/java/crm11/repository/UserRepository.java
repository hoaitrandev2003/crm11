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
//Nơi quản lý tất cả câu truy vấn liên quan tới bảng user 
public class UserRepository {
	//Cách đặt tên cho hàm 
	// select <=> find
	// where <=> by không có where thì findAll
	public List<UserEntity> findByEmailAndPassword (String email, String password){
		List<UserEntity> listUser = new ArrayList<UserEntity>();

		String query = "SELECT * FROM users WHERE email = ? AND password = ?";
		
		Connection connection = MysqlConfig.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, email);
			statement.setString(2, password);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				UserEntity userEntity = new UserEntity();
				userEntity.setId(resultSet.getInt("id"));
				userEntity.setEmail(resultSet.getString("email"));
				
				RolesEntity rolesEntity = new RolesEntity();
				rolesEntity.setId(resultSet.getInt("role_id"));
				
				userEntity.setRole(rolesEntity);
				
				listUser.add(userEntity);
				
			}
		} catch (Exception e) {
			System.out.println("Error query" + e.getMessage());
		}
		
		return listUser;	
	}
	
	public List<UserEntity> findAllUser () {
		//Tao mang rong
		List<UserEntity> listUser = new ArrayList<UserEntity>();
		
		//Viet cau truy van
		String query = "SELECT u.id,	u.fullname, u.email ,u.role_id, r.name"
					 + "	FROM users u "
					 + "JOIN roles r ON u.role_id = r.id";
		
		//Ket noi database
		Connection connection = MysqlConfig.getConnection();
		
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				UserEntity userEntity = new UserEntity();
				userEntity.setId(resultSet.getInt("id"));
				userEntity.setFullname(resultSet.getString("fullname"));
				userEntity.setEmail(resultSet.getString("email"));
				
				RolesEntity rolesEntity = new RolesEntity();
				rolesEntity.setName(resultSet.getString("name"));
				userEntity.setRole(rolesEntity);
				
				listUser.add(userEntity);
			}
		} catch (Exception e){
			System.out.println("Error findAllUser :" + e.getMessage());
		}
		
		return listUser;
	}
	
	public int save(UserEntity userEntity) {
		int count = 0;
	    String query = "INSERT INTO users (email, password, fullname, role_id, phone_no) "
	                 + "VALUES (?, ?, ?, ?, ?)";
	    
	    Connection connection = MysqlConfig.getConnection();
	    try (
	         PreparedStatement statement = connection.prepareStatement(query)) {

	        statement.setString(1, userEntity.getEmail());
	        statement.setString(2, userEntity.getPassword());
	        statement.setString(3, userEntity.getFullname());
	        statement.setInt(4, userEntity.getRole().getId());
	        statement.setString(5, userEntity.getPhone_no());

	        count = statement.executeUpdate();

	    } catch (Exception e) {
	        System.out.println("Error save: " + e.getMessage());
	    }
	    return count;
	}
	
	public List<TasksEntity> findByUserId (int id) {
		List<TasksEntity> listTask = new ArrayList<TasksEntity>();
		
		String query = "SELECT t.id,t.start_date,t.end_date,u.fullname AS user,u.email,t.user_id ,s.name AS status,j.name AS job  from tasks t "
				+ "JOIN users u ON t.user_id = u.id "
				+ "JOIN status s ON t.status_id = s.id "
				+ "JOIN jobs j ON t.job_id = j.id "
				+ "WHERE t.user_id = ? ";
		
		Connection connection = MysqlConfig.getConnection();
		try {
			PreparedStatement statments = connection.prepareStatement(query);
			statments.setInt(1, id);
			
			ResultSet resultSet = statments.executeQuery();
			
			while (resultSet.next()) {
				TasksEntity tasksEntity = new TasksEntity();
				tasksEntity.setId(resultSet.getInt("id"));
				
				Date startDate = resultSet.getDate("start_date");
			    Date endDate   = resultSet.getDate("end_date");

			    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			    tasksEntity.setStart_date(sdf.format(startDate));
			    tasksEntity.setEnd_date(sdf.format(endDate));

				StatusEntity statusEntity = new StatusEntity();
				statusEntity.setId(resultSet.getInt("id"));
				statusEntity.setName(resultSet.getString("status"));
				tasksEntity.setStatus(statusEntity);
				
				UserEntity userEntity = new UserEntity();
				userEntity.setId(resultSet.getInt("user_id"));
				userEntity.setFullname(resultSet.getString("user"));
				userEntity.setEmail(resultSet.getString("email"));
				tasksEntity.setUsers(userEntity);
				
				JobsEntity jobsEntity = new JobsEntity();
				jobsEntity.setName(resultSet.getString("job"));
				tasksEntity.setJobs(jobsEntity);
				
				listTask.add(tasksEntity);
			}
			
			
		}catch (Exception e) {
			System.out.println("Error findByUserId : " + e.getMessage());
		}
		
		return listTask;
	}
	
	public UserEntity findByIdUser (int id) {
		UserEntity userEntity = new UserEntity();
		String query = "SELECT u.id, u.fullname,u.password,u.email,u.phone_no,u.role_id FROM users u WHERE u.id = ?";
		Connection connection = MysqlConfig.getConnection();
		try {
			PreparedStatement statments = connection.prepareStatement(query);
			statments.setInt(1, id);
			ResultSet resultSet = statments.executeQuery();
			while(resultSet.next()) {
				userEntity.setId(resultSet.getInt("id"));
				userEntity.setFullname(resultSet.getString("fullname"));
				userEntity.setEmail(resultSet.getString("email"));
	            userEntity.setPassword(resultSet.getString("password"));
	            userEntity.setPhone_no(resultSet.getString("phone_no"));
	            
	            RolesEntity roleEntity = new RolesEntity();
	            roleEntity.setId(resultSet.getInt("role_id"));
	            userEntity.setRole(roleEntity);
			}
			
		}catch (Exception e) {
			System.out.println("Error findByIdUser : " + e.getMessage());
		}
		
		return userEntity;
	}
	
	public boolean deleteByIdUser(int id) {

	    String query = "DELETE FROM users WHERE id = ?";

	    try (Connection conn = MysqlConfig.getConnection();
	         PreparedStatement statments = conn.prepareStatement(query)) {

	    		statments.setInt(1, id);
	        return statments.executeUpdate() > 0;

	    } catch (Exception e) {
	        System.out.println("Delete deleteByIdUser: " + e.getMessage());
	    }
	    return false;
	}

	public List<TasksEntity> findTaskByUserId(int userId) {

	    List<TasksEntity> listTask = new ArrayList<>();

	    String query =
	        "SELECT t.status_id, s.name " +
	        "FROM tasks t JOIN status s ON t.status_id = s.id " +
	        "WHERE t.user_id = ?";

	    try (Connection conn = MysqlConfig.getConnection();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setInt(1, userId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            TasksEntity task = new TasksEntity();
	            StatusEntity status = new StatusEntity();

	            status.setId(rs.getInt("status_id"));
	            status.setName(rs.getString("name"));

	            task.setStatus(status);
	            listTask.add(task);
	        }

	    } catch (Exception e) {
	        System.out.println("findTaskByUserId error: " + e.getMessage());
	    }

	    return listTask;
	}

	public boolean updateByIdUser(int id, String fullname,String email, String password, String phone, int roleId) {
	    String sql = " UPDATE users SET fullname=?, email=?, password=?, phone_no=?, role_id=? WHERE id=? ";
	    Connection connection = MysqlConfig.getConnection();
	    
	    try {
	    	 	PreparedStatement statments = connection.prepareStatement(sql);
	    	 	statments.setString(1, fullname);
	    	 	statments.setString(2, email);
	    	 	statments.setString(3, password);
	    	 	statments.setString(4, phone);
	    	 	statments.setInt(5, roleId);
	    	 	statments.setInt(6, id);

	        return statments.executeUpdate() > 0;
	        
	    } catch (Exception e) {
	        System.out.println("Error updateByIdUser : " + e.getMessage());
	    }
	    return false;
	}

	
}
