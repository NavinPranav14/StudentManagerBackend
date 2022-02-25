package com.example.dao.impl;

import com.example.dao.StaffManagementDao;
import com.example.entity.Staff;
import com.example.exception.MongoDbException;
import com.example.exception.NotFoundException;
import com.mongodb.MongoException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.enums.UserStatus.DEACTIVE;
@Repository
public class StaffManagementDaoImpl implements StaffManagementDao{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Staff> listStaff() throws MongoDbException {
        try {
            return mongoTemplate.findAll(Staff.class);
        } catch (MongoException mongoException) {
            throw new MongoDbException("Error while fetching data", mongoException);
        }
    }


    @Override
    public Staff staffAccess(String username){

        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.findOne(query, Staff.class);
    }

    @Override
    public Staff findStaffById(String id) throws NotFoundException {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
            return mongoTemplate.findOne(query, Staff.class);
        } catch (MongoException mongoException) {
            throw new NotFoundException("Staff not found", mongoException);
        }
    }

    @Override
    public Staff findStaffByUserName(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.findOne(query, Staff.class);
    }

    @Override
    public void addStaff(Staff staff) {

        mongoTemplate.insert(staff);
        return;
    }

    @Override
    public void modifyStaff(Staff staff, String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        Document doc = new Document("$set", staff);
        mongoTemplate.getConverter().write(staff, doc);
        Update update = Update.fromDocument(doc);
        mongoTemplate.findAndModify(query, update, Staff.class);
        return;

    }

    @Override
    public void deleteStaff(String id) throws NotFoundException {
            try{
                Query query1 = new Query(Criteria.where("_id").is(new ObjectId(id)));
                Update update1 = new Update();
                update1.set("status", DEACTIVE);
                mongoTemplate.updateFirst(query1, update1, Staff.class);
            }
         catch (MongoException mongoException) {
            throw  new NotFoundException("Staff with this id doesn't exists",mongoException);
        }
    }
}