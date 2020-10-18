package ru.job4j.dto;

import java.util.ArrayList;
import java.util.List;

public class Candidate {
	private String firstname;
	private String lastname;
	private String patronymic;
	private String post;
	private Float salary;
	private List<PersonnelOfficer> personnelOfficers;
	/**
	 * 
	 */
	public Candidate() {
	}
	/**
	 * 
	 * @return firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * 
	 * @param firstname
	 */
	public void setFirstname(final String firstname) {
		this.firstname = firstname;
	}
	/**
	 * 
	 * @return lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * 
	 * @param lastname
	 */
	public void setLastname(final String lastname) {
		this.lastname = lastname;
	}
	/**
	 * 
	 * @return patronymic
	 */
	public String getPatronymic() {
		return patronymic;
	}
	/**
	 * 
	 * @param patronymic
	 */
	public void setPatronymic(final String patronymic) {
		this.patronymic = patronymic;
	}
	/**
	 * 
	 * @return post
	 */
	public String getPost() {
		return post;
	}
	/**
	 * 
	 * @param post
	 */
	public void setPost(final String post) {
		this.post = post;
	}
	/**
	 * 
	 * @return salary
	 */
	public Float getSalary() {
		return salary;
	}
	/**
	 * 
	 * @param salary
	 */
	public void setSalary(final Float salary) {
		this.salary = salary;
	}
	/**
	 * 
	 * @return list of personnelOfficers
	 */
	public List<PersonnelOfficer> getPersonnelOfficers() {
		return personnelOfficers;
	}
	/**
	 * 
	 * @param personnelOfficer
	 */
	public void addPersonnelOfficers(final PersonnelOfficer personnelOfficer) {
		if (this.personnelOfficers == null) {
			this.personnelOfficers = new ArrayList<PersonnelOfficer>();
		}
		this.personnelOfficers.add(personnelOfficer);
	}
	@Override
	public String toString() {
		return "Candidate [firstname=" + firstname + ", lastname=" + lastname + ", patronymic="
				+ patronymic + ", post=" + post + ", salary=" + salary
				+ ", personnelOfficers=" + personnelOfficers + "]";
	}
	
}
