package crm11.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import crm11.config.MysqlConfig;
import crm11.entity.RolesEntity;

public class RolesRepository {
	public RolesEntity findById(int id) {
		
		RolesEntity rolesEntity = null;
		
		String query = "SELECT * FROM roles WHERE id = ?";
		
		Connection connection = MysqlConfig.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				rolesEntity = new RolesEntity();
				rolesEntity.setId(resultSet.getInt("id"));
				rolesEntity.setName(resultSet.getString("name"));
				rolesEntity.setDescription(resultSet.getString("description"));
			}
			
		} catch (Exception e) {
			System.out.println("Error findById : " + e.getMessage());
		}
		
		return rolesEntity;
	}
	
	public List<RolesEntity> findAllRoles() {
		List<RolesEntity> listRolesEntity = new ArrayList<RolesEntity>();
		
		String query = "SELECT * FROM roles ";
		
		Connection connection = MysqlConfig.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				RolesEntity rolesEntity = new RolesEntity();
				rolesEntity = new RolesEntity();
				rolesEntity.setId(resultSet.getInt("id"));
				rolesEntity.setName(resultSet.getString("name"));
				rolesEntity.setDescription(resultSet.getString("description"));
				
				listRolesEntity.add(rolesEntity);
			}
			
		} catch (Exception e) {
			System.out.println("Error findById : " + e.getMessage());
		}
		
		return listRolesEntity;
		
	}
	
	public int addRoles(RolesEntity rolesEntity ) {
		int count = 0;
		
		String query = "INSERT INTO roles(name,description) "
				+ "VALUES(?,?)";
		
		Connection connection = MysqlConfig.getConnection();
		
		try {
			PreparedStatement statment = connection.prepareStatement(query);
			statment.setString(1, rolesEntity.getName());
			statment.setString(2, rolesEntity.getDescription());
			
			count = statment.executeUpdate();
			
		} catch (Exception e){
			System.out.println("Error saveRole : " + e.getMessage());
		}
		
		return count;
	}
	
	public boolean deleteByIdRole(int id) {
	    String query = "DELETE FROM roles WHERE id = ?";

	    Connection connection = MysqlConfig.getConnection();
	    try {
	    		PreparedStatement statments = connection.prepareStatement(query);
	        statments.setInt(1, id);
	        return statments.executeUpdate() > 0;

	    } catch (Exception e) {
	        System.out.println("Delete deleteByIdRole: " + e.getMessage());
	    }
	    return false;
	}

	
	public boolean updateRoleById(int id, String name, String description) {

	    String sql = "UPDATE roles SET name=?, description=? WHERE id=?";

	    Connection conn = MysqlConfig.getConnection();
	    try {
	    		PreparedStatement statments = conn.prepareStatement(sql);
	    		statments.setString(1, name);
	    		statments.setString(2, description);
	    		statments.setInt(3, id);

	        return statments.executeUpdate() > 0;

	    } catch (Exception e) {
	        System.out.println("Error updateRoleById : " + e.getMessage());
	    }
	    return false;
	}

}
