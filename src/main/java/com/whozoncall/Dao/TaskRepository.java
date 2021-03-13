package com.whozoncall.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.whozoncall.Entities.PDAccount;
import com.whozoncall.Entities.TaskResult;

public interface TaskRepository extends CrudRepository<TaskResult, Long> {

	@SuppressWarnings("unchecked")
	TaskResult save(TaskResult task);
	
	
}