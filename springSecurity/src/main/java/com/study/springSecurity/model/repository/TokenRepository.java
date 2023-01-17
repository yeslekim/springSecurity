package com.study.springSecurity.model.repository;

import org.springframework.data.repository.CrudRepository;

import com.study.springSecurity.model.entity.redis.Token;

public interface TokenRepository extends CrudRepository<Token, Long>{

}
