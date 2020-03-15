package cn.shirman.learn.spring.redisrepositoriessample.controller;

import cn.shirman.learn.spring.redisrepositoriessample.entity.Gender;
import cn.shirman.learn.spring.redisrepositoriessample.entity.Person;
import cn.shirman.learn.spring.redisrepositoriessample.service.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 描述：
 * 作者：shirman
 * 日期：2020-03-14 11:14 下午
 */
@RestController
public class PersonCtrl {
    @Autowired
    private PersonRepository personRepository;

    Person eddard = new Person("军军", "谢", Gender.MALE);
    Person robb = new Person("robb", "stark", Gender.MALE);
    Person sansa = new Person("sansa", "stark", Gender.FEMALE);
    Person arya = new Person("arya", "stark", Gender.FEMALE);
    Person bran = new Person("bran", "stark", Gender.MALE);
    Person rickon = new Person("rickon", "stark", Gender.MALE);
    Person jon = new Person("jon", "snow", Gender.MALE);

    @GetMapping("findAllPerson")
    public Object findAllPerson(){
        Person eddard = new Person("军军", "谢", Gender.MALE);
        Person robb = new Person("robb", "stark", Gender.MALE);
        Person sansa = new Person("sansa", "stark", Gender.FEMALE);
        Person arya = new Person("arya", "stark", Gender.FEMALE);
        Person bran = new Person("bran", "stark", Gender.MALE);
        Person rickon = new Person("rickon", "stark", Gender.MALE);
        Person jon = new Person("jon", "snow", Gender.MALE);
        personRepository.deleteAll();
        personRepository.saveAll(Arrays.asList(eddard, robb, sansa, arya, bran, rickon, jon));
        return personRepository.findAll();
    }

    @GetMapping("deleteAll")
    public Object deleteAll(){
        personRepository.deleteAll();
        return true;
    }

    @GetMapping("saveAll")
    public Object saveAll(){
        personRepository.saveAll(Arrays.asList(eddard, robb, sansa, arya, bran, rickon, jon));
        return true;
    }

    @GetMapping("leaveOne")
    public Object leaveOne(){
        this.deleteAll();
        personRepository.saveAll(Arrays.asList(eddard));
        return true;
    }
}
