package crm11.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm11.entity.JobsEntity;
import crm11.entity.TasksEntity;
import crm11.entity.UserEntity;
import crm11.services.JobsServices;

@WebServlet(name="workGroupController",urlPatterns = {"/groupwork","/groupwork-add","/groupwork-details","/groupwork-delete","/groupwork-edit"})
public class WorkGroupController extends HttpServlet{
	private JobsServices jobsServices = new JobsServices();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		
		switch (url) {
		case "/groupwork":
			uiGroupwork(req,resp);
			break;

		case "/groupwork-add":
			uiGroupworkAdd(req,resp);
			break;
		case "/groupwork-details":
			groupworkDetails(req,resp);
			break;
		case "/groupwork-delete":
			deleteGroupwork(req,resp);
			uiGroupwork(req,resp);
			break;
		case "/groupwork-edit" :
			uiEditGroupWork(req, resp);
			break;
		}
		
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		
		switch (url) {
		case "/groupwork":
			break;

		case "/groupwork-add":
			groupworkAdd(req,resp);
			break;
		case "/groupwork-edit":
			editGroupWork(req,resp);
			uiEditGroupWork(req,resp);
			break;
		
		}
	}
	
	private void editGroupWork(HttpServletRequest req, HttpServletResponse resp) {
	    int id = Integer.parseInt(req.getParameter("id"));
	    String name = req.getParameter("name");

	    String start = req.getParameter("start_date");
	    String end   = req.getParameter("end_date");

	    jobsServices.editJob(id, name, start, end);
		
	}
	
	private void uiEditGroupWork(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		
		JobsEntity jobs = jobsServices.getByIdJobs(id);
		
		req.setAttribute("jobs", jobs);
		
		req.getRequestDispatcher("groupwork-add.jsp").forward(req, resp);
	}
	
	private void deleteGroupwork(HttpServletRequest req, HttpServletResponse resp) {
		int id = Integer.parseInt(req.getParameter("id"));
		String message = jobsServices.deleteByIdJobs(id);
		req.setAttribute("message", message);
		
	}
	
	private void groupworkAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JobsEntity jobsEntity = new JobsEntity();
		jobsEntity.setName(req.getParameter("name"));
		jobsEntity.setStart_date(req.getParameter("start_date"));
		jobsEntity.setEnd_date(req.getParameter("end_date"));
		
		String messages = jobsServices.addJobs(jobsEntity);
		
		req.setAttribute("messages", messages);
		
		req.getRequestDispatcher("groupwork-add.jsp").forward(req, resp);
	}
	
	private void groupworkDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));

		List<TasksEntity> listTask = jobsServices.getByIdTaskFromJobs(id);

		Map<String, Integer> percentMap = jobsServices.getPercentStatusFromList(listTask);

		req.setAttribute("percentMap", percentMap);
		req.setAttribute("listTask", listTask);

		req.getRequestDispatcher("groupwork-details.jsp").forward(req, resp);

	}
	
	private void uiGroupwork (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<JobsEntity> listJobs = jobsServices.getAllJob();
		
		req.setAttribute("listJobs", listJobs);
		
		req.getRequestDispatcher("groupwork.jsp").forward(req, resp);
	}
	
	private void uiGroupworkAdd (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.getRequestDispatcher("groupwork-add.jsp").forward(req, resp);
	}
	
}
