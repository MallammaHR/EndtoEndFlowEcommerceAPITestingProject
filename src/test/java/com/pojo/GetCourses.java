package com.pojo;

public class GetCourses 
{
	//Decalre  variables
	private String url;
	private String Services;
	private String expertise;
	private Courses Courses;
	private String linkedIn;
	private String instructor;

	
	public String getUrl() 
	{
		return url;
	}
	public void setUrl(String url) 
	{
		this.url = url;
	}
	public String getServices() 
	{
		return Services;
	}
	public void setServices(String services)
	{
		Services = services;
	}
	public String getExpertise()
	{
		return expertise;
	}
	public void setExpertise(String expertise) 
	{
		this.expertise = expertise;
	}
	public com.pojo.Courses getCourses()
	{
		return Courses;
	}
	public void setCourses(com.pojo.Courses courses)
	{
		Courses = courses;
	}
	public String getLinkedIn()
	{
		return linkedIn;
	}
	public void setLinkedIn(String linkedIn)
	{
		this.linkedIn = linkedIn;
	}
	public String getInstructor()
	
	{
		return instructor;
	}
	public void setInstructor(String instructor) 
	{
		this.instructor = instructor;
	}

}
