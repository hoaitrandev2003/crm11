package crm11.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm11.entity.JobsEntity;
import crm11.entity.TasksEntity;
import crm11.entity.UserEntity;
import crm11.services.TasksServices;

@WebServlet(name="taskController", urlPatterns = {"/task","/task-add","/task-delete","/task-edit"})
public class TaskController extends HttpServlet{
	private TasksServices tasksServices = new TasksServices();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		
		switch (url) {
		case "/task":
			uiTask(req, resp);
			break;
		case "/task-add":
			uiTaskAdd(req, resp);
			break;
		case "/task-delete":
			deleteByIdTask(req,resp);
			uiTask(req, resp);
			break;
		case "/task-edit":
			uiEditTaskById(req,resp);
			break;
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		
		switch (url) {
		case "/task":
			break;
		case "/task-add":
			addTaskAdd(req, resp);
			break;
		case "/task-edit":
			editTaskById(req, resp);
			uiEditTaskById(req, resp);
			break;
		}
	}
	
	private void editTaskById(HttpServletRequest req, HttpServletResponse resp) {
	    int id = Integer.parseInt(req.getParameter("id"));
	    String name = req.getParameter("name");
	    String start = req.getParameter("start_date");
	    String end = req.getParameter("end_date");
	    int jobId = Integer.parseInt(req.getParameter("job_id"));
	    int userId = Integer.parseInt(req.getParameter("user_id"));

	    tasksServices.updateTask(id, name, start, end, userId, jobId);
	}
	
	private void uiEditTaskById (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    int id = Integer.parseInt(req.getParameter("id"));
	    
		List<JobsEntity> listJobs = tasksServices.getAllJobs();
		List<UserEntity> listUser = tasksServices.getAllUsers();
		
		req.setAttribute("listJobs", listJobs);
		req.setAttribute("listUser", listUser);
		
	    TasksEntity task = tasksServices.getTaskById(id);

	    req.setAttribute("task", task);

		req.getRequestDispatcher("task-add.jsp").forward(req, resp);
	}
	
	private void deleteByIdTask(HttpServletRequest req, HttpServletResponse resp) {
		int id = Integer.parseInt(req.getParameter("id"));
		String messages = tasksServices.deleteByIdTask(id);
		req.setAttribute("messages", messages);
	}
	
	private void addTaskAdd (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TasksEntity tasksEntity = new TasksEntity();
		tasksEntity.setName(req.getParameter("name"));
		tasksEntity.setStart_date(req.getParameter("start_date"));
		tasksEntity.setEnd_date(req.getParameter("end_date"));

		JobsEntity jobsEntity = new JobsEntity();
		jobsEntity.setId(Integer.parseInt(req.getParameter("job_id")));
		tasksEntity.setJobs(jobsEntity);
		
		UserEntity userEntity = new UserEntity();
		userEntity.setId(Integer.parseInt(req.getParameter("user_id")));
		tasksEntity.setUsers(userEntity);
		
		String messages = tasksServices.addTasks(tasksEntity);
		
		req.setAttribute("messages", messages);
		
		List<JobsEntity> listJobs = tasksServices.getAllJobs();
		List<UserEntity> listUser = tasksServices.getAllUsers();
		
		req.setAttribute("listJobs", listJobs);
		req.setAttribute("listUser", listUser);
		
		req.getRequestDispatcher("task-add.jsp").forward(req, resp);
	}
	
	private void uiTask (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<TasksEntity> listTask= tasksServices.getAllTasks();
		
		req.setAttribute("listTask", listTask);
		req.getRequestDispatcher("task.jsp").forward(req, resp);
	}
	
	private void uiTaskAdd (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<JobsEntity> listJobs = tasksServices.getAllJobs();
		List<UserEntity> listUser = tasksServices.getAllUsers();
		
		req.setAttribute("listJobs", listJobs);
		req.setAttribute("listUser", listUser);
		
		req.getRequestDispatcher("task-add.jsp").forward(req, resp);
	}
	
}
