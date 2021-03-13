package com.whozoncall.Dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.whozoncall.Entities.PDAccount;

public interface PDAccountRepository extends CrudRepository<PDAccount, Long> {

	@SuppressWarnings("unchecked")
	PDAccount save(PDAccount pdAccount);
	
	@Query
	Iterable<PDAccount> findByAuthDoneFalse();

	@Query
	Iterable<PDAccount> findByOnCallsFetchDoneFalse();
	
	@Query
	Iterable<PDAccount> findByUsersFetchDoneFalse();

	@Query
	Iterable<PDAccount> findByAuthDoneTrueAndOnCallsFetchDoneFalse();
	
	@Query
	Iterable<PDAccount> findByAuthDoneTrueAndUsersFetchDoneFalse();

}