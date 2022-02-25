package com.example.service.impl;

public class GenerateId {

    public String generateId(String user){
        Integer rand = (int)(Math.random()*(1000 - 100) + 100);
        return user + rand;
    }
}
