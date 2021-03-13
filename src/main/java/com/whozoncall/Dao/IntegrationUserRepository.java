package com.whozoncall.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.whozoncall.Entities.Account;
import com.whozoncall.Entities.IntegrationUser;
import com.whozoncall.Entities.PDAccount;
import com.whozoncall.Entities.SlackChannelAccount;

public interface IntegrationUserRepository extends CrudRepository<IntegrationUser, Long> {

	
	Optional<IntegrationUser> findByEmail(String email);
	
}