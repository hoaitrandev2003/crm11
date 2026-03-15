package crm11.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm11.entity.RolesEntity;
import crm11.entity.TasksEntity;
import crm11.entity.UserEntity;
import crm11.services.LoginServices;
import crm11.services.UsersServices;
import crm11.utils.SHA256Util;

//Chuẩn mã hóa MD5 -> SHA1 -> SHA256 -> SHA512 -> Bcrypt -> HMAC -> RSA -> Hybrid

@WebServlet(name="userTableController", urlPatterns = {"/user-table","/user-add","/user-details","/user-delete","/user-edit"})
public class UserTableController extends HttpServlet{
	private UsersServices usersServices = new UsersServices();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		
		switch (url) {
			case "/user-table":
				uiUser(req,resp);
				break;
			case "/user-add":
				uiUserAdd(req,resp);
				break;
			case "/user-details":
				uiUserDetails(req,resp);
				break;
	        case "/user-delete":
	            deleteByIdUser(req, resp);
	            uiUser(req,resp);
	            break;
	        case "/user-edit":
	        		uieditByIdUser(req,resp);
	        		break;
		} 
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		
		switch (url) {
			case "/user-table":
				break;
			case "/user-add":
				userAddPost(req,resp);
				break;
			case "/user-edit":
				editByIdUser(req,resp);
				uieditByIdUser(req, resp);
				break;
				
		}
	}
	
	private void editByIdUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
	    int id = Integer.parseInt(req.getParameter("id"));
	    String fullname = req.getParameter("fullname");
	    String email = req.getParameter("email");
	    String password = req.getParameter("password");
	    String phone = req.getParameter("phone_no");
	    int role = Integer.parseInt(req.getParameter("role"));

	    usersServices.editByIdUser(id, fullname, email, password, phone, role);

	}
	
	private void uieditByIdUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	    int id = Integer.parseInt(req.getParameter("id"));

	    UserEntity user = usersServices.getByUserId(id);
	    List<RolesEntity> listAllRoles = usersServices.getAllRoles();

	    req.setAttribute("user", user);
	    req.setAttribute("listAllRoles", listAllRoles);

	    req.getRequestDispatcher("user-add.jsp").forward(req, resp);

	}
	
	private void deleteByIdUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		String message = usersServices.deleteByIdUser(id);

		req.setAttribute("message", message);
	}
	
	private void userAddPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    
	    UserEntity userEntity = new UserEntity();
	    userEntity.setFullname(req.getParameter("fullname"));
	    userEntity.setPassword(SHA256Util.sha256(req.getParameter("password")));
	    userEntity.setEmail(req.getParameter("email"));
	    userEntity.setPhone_no(req.getParameter("phone_no"));
	    
	    RolesEntity roleEntity = new RolesEntity();
	    roleEntity.setId(Integer.parseInt(req.getParameter("role")));
	    
	    userEntity.setRole(roleEntity);
	    
	    String message = usersServices.insertUser(userEntity);
	    
	    req.setAttribute("message", message);
	    
		req.getRequestDispatcher("user-add.jsp").forward(req, resp);
	}
	
	private void uiUserDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		//chưa hoàn thành
		List<TasksEntity> listTaskNotyetComplete = usersServices.getAllByTaskIdNotYetComplete(id);
		//Đang thực hiện
		List<TasksEntity> listTaskIdCurrentlyInProgress = usersServices.getAllByTaskIdCurrentlyInProgress(id);
		//Đã hoàn thành
		List<TasksEntity> listTaskComplete = usersServices.getAllByTaskIdComplete(id);
		UserEntity user = usersServices.getByUserId(id);
		
		req.setAttribute("user", user);
		req.setAttribute("listTaskIdCurrentlyInProgress", listTaskIdCurrentlyInProgress);
		req.setAttribute("listTaskNotyetComplete", listTaskNotyetComplete);
		req.setAttribute("listTaskComplete", listTaskComplete);
		

		Map<String, Integer> percentMap = usersServices.getPercentStatusByUser(id);

		req.setAttribute("percentMap", percentMap);
		
		req.getRequestDispatcher("user-details.jsp").forward(req, resp);
	}
	
	private void uiUserAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<RolesEntity> listAllRoles = usersServices.getAllRoles();
		
		req.setAttribute("listAllRoles", listAllRoles);
		
		req.getRequestDispatcher("user-add.jsp").forward(req, resp);
	}
	
	
	private void uiUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		List<UserEntity> listUserEntity = usersServices.showUser();
		
		req.setAttribute("listUser", listUserEntity);
		req.getRequestDispatcher("user-table.jsp").forward(req, resp);
	}

}
