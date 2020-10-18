package ru.job4j.dto;

import java.util.ArrayList;
import java.util.List;

public class PersonnelOfficer {
	private String firstname;
	private String lastname;
	private String patronymic;
	private String companyName;
	private Float salary;
	private List<Candidate> candidates;
	/**
	 * 
	 */
	public PersonnelOfficer() {
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
	 * @return companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * 
	 * @param companyName
	 */
	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
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
	 * @return candidate
	 */
	public List<Candidate> getCandidate() {
		return candidates;
	}
	/**
	 * 
	 * @param candidate
	 */
	public void addCandidate(final Candidate candidate) {
		if (this.candidates == null) {
			this.candidates = new ArrayList<Candidate>();
		}
		this.candidates.add(candidate);
	}
	@Override
	public String toString() {
		return "PersonnelOfficer [firstname=" + firstname + ", lastname=" + lastname 
				+ ", patronymic=" + patronymic + ", companyName=" + companyName
				+ ", salary=" + salary + ", candidates=" + candidates + "]";
	} 
	
}
