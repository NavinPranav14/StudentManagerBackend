package com.example.dao.impl;

import com.example.dao.AdminAccessDao;
import com.example.entity.Admin;
import com.example.exception.MongoDbException;
import com.example.exception.NotFoundException;
import com.mongodb.MongoException;
import org.hibernate.validator.constraints.Mod10Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class AdminAccessDaoImpl implements AdminAccessDao{
    @Autowired
    private MongoTemplate mongoTemplate;

    public Admin adminAccess(String username) throws NotFoundException{
        try{
            Query query = new Query();
            query.addCriteria(Criteria.where("username").is(username));
            return mongoTemplate.findOne(query, Admin.class);
        }catch(MongoException mongoException){
            throw new NotFoundException("Staff not found", mongoException);

        }

    }
}
