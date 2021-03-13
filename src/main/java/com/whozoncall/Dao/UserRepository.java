package com.whozoncall.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.whozoncall.Entities.Account;
import com.whozoncall.Entities.PDAccount;
import com.whozoncall.Entities.User;

public interface UserRepository extends CrudRepository<PDAccount, Long> {
	
	
	User save(User user);
	
	@Query(nativeQuery = true, value ="select pdMap.user_channel_id,slackMap.user_channel_id  from user_map pdMap inner join user_map slackMap"
+ " on pdMap.map_id=slackMap.map_id where slackMap.type = \"SLACK\" and pdMap.type = \"PAGERDUTY\" group by pdMap.map_id;")
	List<User> getPdSlackUserMap();
	
	
	@Query("from User u WHERE u.account.id = ?1 and u.userName =?2 and u.password= ?3 ")
	User findAccountByUserNameandPassword(Long accountId, String userName, String passowrd);

}