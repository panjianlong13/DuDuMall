package com.peterpan.dudumall.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.peterpan.dudumall.auth.entity.UserEntity;
import com.peterpan.dudumall.auth.repository.UserRepository;
import com.peterpan.dudumall.auth.util.ResultCode;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private UserRepository respository;

	public UserEntity addUser(UserEntity userEntity) {
		UserEntity result = respository.save(userEntity);
		return result;
	}

	public UserEntity findByUserId(String userid) {
		Query query = new Query(Criteria.where("userid").is(userid));
		UserEntity user = mongoTemplate.findOne(query, UserEntity.class);
		return user;
	}

	public UserEntity findByUserName(String username) {
		Query query = new Query(Criteria.where("username").is(username));
		UserEntity userEntity = mongoTemplate.findOne(query, UserEntity.class);
		if (userEntity == null) {
            return null;
		}
        return userEntity;
	}
	
	public UserEntity updateByUserId(String userid, UserEntity userEntity) {
		Query query = new Query(Criteria.where("userid").is(userid));
		Update update = new Update();
		update.set("username", userEntity.getUsername());
		update.set("mobile", userEntity.getMobile());
		update.set("country", userEntity.getCountry());
		update.set("province", userEntity.getProvince());
		update.set("city", userEntity.getCity());
		// update.set("imageUrl", userEntity.getImageUrl());
		mongoTemplate.updateFirst(query, update, UserEntity.class);
		return findByUserId(userid);
	}

	public boolean deleteByUserId(String userid) {
		UserEntity searchUserEntity = findByUserId(userid);
		if (searchUserEntity == null) {
			log.info("获取用户信息失败");
			return false;
		}
		log.info("获取用户信息成功, username = " + searchUserEntity.getUsername());
		Criteria criteria = Criteria.where("userid").is(userid);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, UserEntity.class);
		return true;
	}
}
