package com.elrouss.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.elrouss.models.FileData;

public interface FileDataRepository extends CrudRepository<FileData, Long> {
	//This method will be used to query the database and retrieve all the email send in the last hour
	@Query(value="select f from FileData f where f.timestamp + 3600  < CURRENT_TIMESTAMP")
	public Iterable<FileData> queryLastUploadedFiles();
}


