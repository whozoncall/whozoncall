package com.whozoncall.Dao;


import org.springframework.data.repository.CrudRepository;

import com.whozoncall.Entities.SlackAuthEntity;

public interface SlackAuthRepository extends CrudRepository<SlackAuthEntity, Long> {

	@SuppressWarnings("unchecked")
	SlackAuthEntity save(SlackAuthEntity SlackAuthEntity);

}