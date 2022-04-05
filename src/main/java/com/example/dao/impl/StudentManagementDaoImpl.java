package com.example.dao.impl;

import com.example.dao.StudentManagementDao;
import com.example.entity.Staff;
import com.example.entity.Student;
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
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.enums.UserStatus.DEACTIVE;
@Service
public class StudentManagementDaoImpl implements StudentManagementDao {

   @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Student studentAccess(String username){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.findOne(query, Student.class);
    }

    @Override
    public List<Student> listStudent() throws MongoDbException {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("status").is("ACTIVE"));
            query.fields().exclude("password");
            return mongoTemplate.find(query,Student.class);
        } catch (MongoException mongoException) {
            throw new MongoDbException("Error while fetching data", mongoException);
        }
    }

    @Override
    public List<Student> findStudentByName(String name) throws MongoDbException{
        try{
            Query query = new Query();
            query.addCriteria(Criteria.where("name").is(name));
            query.addCriteria(Criteria.where("status").is("ACTIVE"));
            query.fields().exclude("password");
            return mongoTemplate.find(query, Student.class);
        }catch(MongoException mongoException){
            throw new MongoDbException("Error while fetching data", mongoException);
        }


    }

    @Override
    public Student findStudentById(String id) throws NotFoundException {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
            query.fields().exclude("password");
            return mongoTemplate.findOne(query, Student.class);
        } catch (MongoException mongoException) {
            throw new NotFoundException("Student not found", mongoException);
        }
    }

    @Override
    public Student findStudentByUserName(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is("ACTIVE"));
        query.addCriteria(Criteria.where("username").is(username));
        query.fields().exclude("password");
        return mongoTemplate.findOne(query, Student.class);
    }



    @Override
    public void addStudent(Student student)  {
        mongoTemplate.insert(student);
        return;
    }

    @Override
    public void modifyStudent(Student student, String id){


        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        Document doc = new Document("$set", student);
        mongoTemplate.getConverter().write(student, doc);
        Update update = Update.fromDocument(doc);
        mongoTemplate.findAndModify(query, update, Student.class);
        return;

    }

    @Override
    public void deleteStudent(String id) throws NotFoundException {
        try{
            Query query1 = new Query(Criteria.where("_id").is(new ObjectId(id)));
            Update update1 = new Update();
            update1.set("status", DEACTIVE);
            mongoTemplate.updateFirst(query1, update1, Student.class);
        }
        catch (MongoException mongoException) {
            throw  new NotFoundException("Student with this id doesn't exists",mongoException);
        }
    }
}