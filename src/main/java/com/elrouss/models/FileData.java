package com.elrouss.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class FileData {
	@Id
	@GeneratedValue
	@Column(name="FILE_ID")
	private long id;
	
	@Column(name="FILE_SIZE")
	private long size;
	
	@Column(name="File_NAME")
	@NotEmpty
	private String name;
	
	@Column(name="File_Location")
	private String location;
	


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long l) {
		this.size = l;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	
}
