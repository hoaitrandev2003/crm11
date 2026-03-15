package crm11;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm11.config.MysqlConfig;
import crm11.entity.UserEntity;

@WebServlet(name="loginSevlet",urlPatterns = {"/login"})
public class LoginSevlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.getRequestDispatcher("login.jsp").forward(req, resp);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Bước 1: Lấy tham số username , password 
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		//Bước 2: Chuẩn bị câu truy vấn.
		String query = "SELECT * FROM users WHERE email = ? AND password = ?";
		
		//Bước 3: Mở kết nối cơ sở dữ liệu.
		Connection connection = MysqlConfig.getConnection();
		// Onion design pattern, Strategy design pattern 
		
		//Controller: là nơi định nghĩa đường dẫn và tham số. [valition tham số]
		
		//Services: Xử lý logic code cho controller 
		
		//Repository: Đảm nhận vai trò và chỉ truy vấn dữ liệu và trả ra kết quả câu truy vấn 
		try {
			//Bước 4: Truyền câu query vào liên kêt mới mở.
			PreparedStatement statement = connection.prepareStatement(query);
			
			//Bước 5: Truyền tham số vào câu query nếu có ( Không có thì bỏ qua bước 5)
			statement.setString(1, email);
			statement.setString(2, password);
			
			//Bước 6: Thực hiện câu truy vấn 
			//executeUpdate : Không phải là câu select thì sử dụng
			//executeQuery : Nếu câu truy vấn là câu Select thì sử dụng
			ResultSet resultSet = statement.executeQuery();
			
			//Tạo ra danh sách rỗng
			List<UserEntity> listUser = new ArrayList<UserEntity>();
			
			//Duyệt qua từng phần tử của resultset để rán giá trị của câu truy vấn thành mảng để đi xử lí
			while(resultSet.next()) {
				UserEntity userEntity = new UserEntity();
				userEntity.setId(resultSet.getInt("id"));
				userEntity.setEmail(resultSet.getString("email"));
				
				listUser.add(userEntity);
				
			}
			
			if(listUser.size() > 0) {
				System.out.println("Đăng nhập thành công");
			} else {
				System.out.println("Đăng nhập thất bại!");
			}
			
		} catch (Exception e) {
			System.out.println("Error query" + e.getMessage());
		}
		
		req.getRequestDispatcher("login.jsp").forward(req, resp);
	}
}
