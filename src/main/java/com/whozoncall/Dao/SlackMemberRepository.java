package com.whozoncall.Dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.whozoncall.Entities.SlackAuthEntity;
import com.whozoncall.Entities.SlackChannelMember;

public interface SlackMemberRepository extends CrudRepository<SlackChannelMember, String> {

	Optional<SlackChannelMember> findByEmail(String string);

	Optional<SlackChannelMember> findByUserId(String slackUserId);


}