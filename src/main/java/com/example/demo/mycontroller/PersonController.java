package com.example.demo.mycontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.Customer.Person;
import com.example.demo.dao.PersonDao;
import com.example.demo.student.Student;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@EnableSwagger2

// I have used swagger for implementation ignore the html pages the same is the implementation for pages.
public class PersonController {
     
	@Autowired
	PersonDao dao;
	
	@GetMapping("/persons")
	public List<Person> getAll()
	{
		return dao.findAll();
	}
	
   // for registration 
	@PostMapping(path="/person",consumes= {"application/json","application/xml"})
	public String insertPerson(@RequestBody Person p)
	{
		if(dao.existsById(p.getId()))
		{
			return "Person with such id already exists try with a different name";
		}
		else {
		System.out.println(p);
		dao.save(p);
		return "Person "+p.getName()+"is Registered successfully";
		}
	}
	
	
	//After login  welcone
	@GetMapping(path="/person/{id}/{password}",produces= {"application/json","application/xml"})
	public Optional<Person> getPerson(@PathVariable("id") int id,@PathVariable("password") String password )
	{
		if(dao.existsById(id))
		{
			Optional<Person> p1 = dao.findById(id);
			Person p=  p1.orElse(null);
			
			if(password.equals(p.getPassword()))
			{
				
				System.out.println("Wecome to Your Page");
				return p1;
			}
			
			System.out.println("Either password or id is wrong check again or register first");
			return null;
		}
		System.out.println("Either password or id is wrong check again or register first");
		return null;
	}
	
	
	// fund transfer from one id to id2
	@PutMapping("/person/{id}/{password}/{fund}//{id2}")
	public String updatePerson(@PathVariable("id") int id,@PathVariable("password") String password,@PathVariable("fund")int fund,@PathVariable("id2") int id2)
	{
		if(dao.existsById(id))
		{
			Optional<Person> p1 = dao.findById(id);
			Person p=  p1.orElse(null);
			
			if(password.equals(p.getPassword()))
			{
				long bal=p.getBalance()-fund;
				p.setBalance(bal);
				String s2="debited with with amount= "+fund+"New balance is : "+bal;
				p.setStatement(s2);
				dao.save(p);
				Optional<Person> p2 = dao.findById(id2);
				Person p12=  p2.orElse(null);
				
				if(p12==null)
					return "No person with id2 is present in db";
				else
				{
					long bal2=p12.getBalance()+fund;
					p12.setBalance(bal2);
					String s1="Credited with with amount= "+fund+"New balance is : "+bal2;
					p12.setStatement(s1);
					dao.save(p12);
					
				}
				
				return "balance updated successfully, Current balance is:"+bal;
			}
			
			return "Either password or id is wrong check again or register first" ;
		}
		
		
		return "Either password or id is wrong check again or register first" ;
		
		
	}
	
	
	//to update password
	@PutMapping("/person1/{id}/{password}/{newpass}")
	public String updatePerson(@PathVariable("id") int id,@PathVariable("password") String password,@PathVariable("newpass")String newpass)
	{
		if(dao.existsById(id))
		{
			Optional<Person> p1 = dao.findById(id);
			Person p=  p1.orElse(null);
			
			if(password.equals(p.getPassword()))
			{
				String pass=newpass;
				p.setPassword(newpass);
				dao.save(p);
				return "Password is reset";
			}
			
			return "Either password or id is wrong check again or register first" ;
		}
		
		
		return "Either password or id is wrong check again or register first" ;
		
		
	}
	
	
}
