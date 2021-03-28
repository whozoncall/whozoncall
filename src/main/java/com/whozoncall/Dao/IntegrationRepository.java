package com.whozoncall.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.whozoncall.Constants.IntegrationTypes;
import com.whozoncall.Entities.Integration;
import com.whozoncall.Entities.PDAccount;

public interface IntegrationRepository extends CrudRepository<Integration, Long> {

	@Query("from Integration i where i.toType='SLACK' and i.account.onTrial = false and i.account.onPaidSub=true")
	List<Integration> findAllPaidIntegrationsToSlack(Integer nextInvocation);
	
	@Query("from Integration i where i.toType='SLACK' and i.account.id=?1 ")
	List<Integration> findAllIntegrationsByAccountId(Long accountId);

}