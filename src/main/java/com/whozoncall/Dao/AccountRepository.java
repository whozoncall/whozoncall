package com.whozoncall.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.whozoncall.Entities.Account;
import com.whozoncall.Entities.Integration;
import com.whozoncall.Entities.PDAccount;

public interface AccountRepository extends CrudRepository<Account, Long> {

	@SuppressWarnings("unchecked")
	Account save(Account account);

	Account findByAccountName(String accountName);

	Account findByGuid(String guid);

}