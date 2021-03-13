package com.whozoncall.Dao;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.whozoncall.Entities.SlackAuthEntity;
import com.whozoncall.Entities.SlackChannelMember;

public interface SlackMemberRepository extends CrudRepository<SlackChannelMember, String> {


}