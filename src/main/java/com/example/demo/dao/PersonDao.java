package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Customer.Person;

public interface PersonDao extends JpaRepository<Person,Integer>{

}
