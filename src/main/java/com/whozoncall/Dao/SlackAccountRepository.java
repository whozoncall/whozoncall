package com.whozoncall.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.whozoncall.Entities.Account;
import com.whozoncall.Entities.PDAccount;
import com.whozoncall.Entities.SlackChannelAccount;

public interface SlackAccountRepository extends CrudRepository<SlackChannelAccount, Long> {

	@SuppressWarnings("unchecked")
	SlackChannelAccount save(SlackChannelAccount slackChannelAccount);

	@Query("from SlackChannelAccount c where c.account.id=?1")
	List<SlackChannelAccount> findAllSlackChannelByAccountId(Long accountId);

	
	Optional<SlackChannelAccount> findById(Long id);

	@Query("from SlackChannelAccount c where c.slackAuthEntity.incoming_webhook.channel_id = ?1 ")
	SlackChannelAccount findByChannelId(String channelId);

	
}