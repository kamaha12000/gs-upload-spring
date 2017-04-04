package com.elrouss.controllers;


import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.elrouss.exception.ResourceNotFoundException;
import com.elrouss.models.FileData;
import com.elrouss.repository.FileDataRepository;
import com.elrouss.services.StorageService;

@RestController
public class FileHandleController {
	@Inject 
	private FileDataRepository fileDataRepository;
	
	@Inject 
	StorageService storageService;
	
	//Get list of meta data of all the files currently saved on the server
	@RequestMapping(value="/files", method=RequestMethod.GET)
	public ResponseEntity<Iterable<FileData>> getAllFiles(){
		Iterable<FileData> allFiles = fileDataRepository.findAll();
		return new ResponseEntity<>(allFiles, HttpStatus.OK);
	}
	
	//Upload a new file with few meta data
	@RequestMapping(value="/files", method=RequestMethod.POST)
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file ){
		FileData fileData = new FileData();
		
		fileData.setName(file.getName());
		fileData.setSize(file.getSize());
		fileData.setLocation(file.getOriginalFilename());
		
		fileData = fileDataRepository.save(fileData);
		
		storageService.store(file);
		
		return new ResponseEntity<>(null, HttpStatus.CREATED );
	}
	
	//get meta data of a specific file on the server
	@RequestMapping(value="/files/{fileId}", method=RequestMethod.GET)
	public ResponseEntity<?> getFile(@PathVariable Long fileId){
		
		verifyPoll(fileId);
		FileData fileData = fileDataRepository.findOne(fileId);
		
		return new ResponseEntity<>(fileData, HttpStatus.OK);
	}
	
	//Update meta data of a specific file on the server
	@RequestMapping(value="/files/{fileId}", method=RequestMethod.PUT)
	public ResponseEntity<?> updateFileData(@PathVariable Long fileId, @Valid @RequestBody FileData fileData){
		
		verifyPoll(fileId);
		
		fileData.setId(fileId);
		
		fileDataRepository.save(fileData);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//Remove a file from the server
	@RequestMapping(value="/files/{fileId}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteFile(@PathVariable Long fileId){
		
		verifyPoll(fileId);
		
		
		fileDataRepository.delete(fileId);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//Download content stream
	@RequestMapping(value="/files/{fileId}", method=RequestMethod.GET)
	public ResponseEntity<Resource> serveFile(@PathVariable Long fileId){
		String fileName = fileDataRepository.findOne(fileId).getName();
		
		Resource file = storageService.loadAsResource(fileName);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Dispositon", "attachement; filename=\"" + file.getFilename()+ "\"");
		
		return new ResponseEntity<>(file, headers, HttpStatus.OK);
	}
	
	
	
	//Method use to verify that a file with a given id actually exist on the server
	protected void verifyPoll(Long fileId) throws ResourceNotFoundException{
		FileData fileData = fileDataRepository.findOne(fileId);
		if(fileData == null){
			throw new ResourceNotFoundException("File with id " + fileId + " not found");
		}
	}
}
