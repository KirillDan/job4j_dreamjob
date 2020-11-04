package ru.job4j.dream.model;

import java.util.Objects;

public class Candidate {
	private int id;
	private String firstname;
	private String lastname;
	private String position;
	private Integer photoId;

	public Candidate(int id, String firstname, String lastname, String position) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.position = position;
		this.photoId = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Candidate candidate = (Candidate) o;
		return id == candidate.id && Objects.equals(firstname, candidate.firstname)
				&& Objects.equals(lastname, candidate.lastname) && Objects.equals(position, candidate.position);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstname, lastname, position);
	}

	@Override
	public String toString() {
		return "Candidate [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", position=" + position
				+ "]";
	}

}