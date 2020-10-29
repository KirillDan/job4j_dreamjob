package ru.job4j.dream.model;

import java.util.Objects;

public class Post {
    private Integer id;
    private String name;
    private String description;
    private String created;

    public Post(Integer id, String name, String description, String created) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.created = created;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", name=" + name + ", description=" + description + ", created=" + created + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Post post = (Post) o;
		return id == post.id &&
				Objects.equals(name, post.name) &&
				Objects.equals(description, post.description) &&
				Objects.equals(created, post.created);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description, created);
	}
}
