package com.example.dao;

import com.example.entity.OdApply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OdApplyRespository extends MongoRepository<OdApply, String> {

    OdApply findByMentor(String mentor);
}
