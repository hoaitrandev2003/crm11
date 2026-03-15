package crm11.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm11.entity.RolesEntity;
import crm11.services.RolesServices;
import crm11.services.UsersServices;

@WebServlet(name="rolesTableController",urlPatterns = {"/role-table","/role-add","/role-delete","/role-edit"})
public class RolesTableController extends HttpServlet{
	private RolesServices roleServices = new RolesServices();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		
		switch (url) {
			case "/role-table":
				uiRolesTable(req,resp);
				break;
			case "/role-add":
				uiRolesAdd(req,resp);
				break;
			case "/role-delete": 
				deleteByIdRole(req,resp);
				uiRolesTable(req,resp);
				break;
			case "/role-edit":
				uiEditByIdRoles(req,resp);
				break;
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		
		switch (url) {
			case "/role-table":
				break;
			case "/role-add":
				addRoles(req,resp);
				break;
			case "/role-edit":
				editByIdRoles(req,resp);
				uiEditByIdRoles(req, resp);
				break;
		}
		
	}
	
	private void editByIdRoles(HttpServletRequest req, HttpServletResponse resp) {
	    int id = Integer.parseInt(req.getParameter("id"));
	    String name = req.getParameter("name");
	    String description = req.getParameter("description");

	    roleServices.editByIdRole(id, name, description);
	    
	}
	
	private void uiEditByIdRoles(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		
		RolesEntity roles = roleServices.getByIdRoles(id);
		
		req.setAttribute("roles", roles);
		req.getRequestDispatcher("role-add.jsp").forward(req, resp);
	}
	
	private void deleteByIdRole (HttpServletRequest req, HttpServletResponse resp) {
		int id = Integer.parseInt(req.getParameter("id"));
		String messages = roleServices.deleteByIdRole(id);
		req.setAttribute("messages", messages);
	}
	
	private void addRoles(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RolesEntity rolesEntity = new RolesEntity();
		rolesEntity.setName(req.getParameter("name"));
		rolesEntity.setDescription(req.getParameter("description"));

		String messages = roleServices.addRoles(rolesEntity);
		
		req.setAttribute("messages", messages);
		
		req.getRequestDispatcher("role-add.jsp").forward(req, resp);
	}
	

	private void uiRolesTable(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<RolesEntity> listRole = roleServices.getAllRoles();
		
		req.setAttribute("listRole", listRole);
		
		req.getRequestDispatcher("role-table.jsp").forward(req, resp);
	}

	private void uiRolesAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {

		req.getRequestDispatcher("role-add.jsp").forward(req, resp);
	}
}
