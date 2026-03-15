package crm11.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm11.entity.RolesEntity;
import crm11.services.LoginServices;
import crm11.utils.SHA256Util;

@WebServlet(name="loginController",urlPatterns = {"/p-login"})
public class LoginController extends HttpServlet {

	private LoginServices loginServices = new LoginServices();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Cookie [] cookies = req.getCookies();
		String email = "";
		String password = "";
		if(cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				String nameCookie = cookie.getName();
				String valueCookie = cookie.getValue();
				
				if(nameCookie.equals("email")) {
					email = valueCookie;
				}
				
				if(nameCookie.equals("password")) {
					password = valueCookie;
				}
				
			}
		}
		
		req.setAttribute("email", email);
		req.setAttribute("password", password);
		
		req.getRequestDispatcher("login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
//		String password = req.getParameter("password");
		String password = SHA256Util.sha256(req.getParameter("password"));
		String remember = req.getParameter("remember-me");
		
		RolesEntity rolesEntity = loginServices.checkLogin(email, password);
		
		if(rolesEntity != null) {
			Cookie cRole = new Cookie("role" ,rolesEntity.getName());
			cRole.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
			resp.addCookie(cRole);

			if(remember != null) {
				Cookie cEmail = new Cookie("email",email);
				Cookie cPassword = new Cookie("password",password);
				
				resp.addCookie(cEmail);
				resp.addCookie(cPassword);
			}
			
			resp.sendRedirect(req.getContextPath());
		} else {
			req.setAttribute("result", "Đăng nhập thất bại !");
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		}
	}
	
}
