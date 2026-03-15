package crm11.entity;

public class TasksEntity {
	private int id;
	private String name;
	private String start_date;
	private String end_date;
	private UserEntity users;
	private JobsEntity jobs;
	private StatusEntity status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public UserEntity getUsers() {
		return users;
	}
	public void setUsers(UserEntity users) {
		this.users = users;
	}
	public JobsEntity getJobs() {
		return jobs;
	}
	public void setJobs(JobsEntity jobs) {
		this.jobs = jobs;
	}
	public StatusEntity getStatus() {
		return status;
	}
	public void setStatus(StatusEntity status) {
		this.status = status;
	}
	
}
