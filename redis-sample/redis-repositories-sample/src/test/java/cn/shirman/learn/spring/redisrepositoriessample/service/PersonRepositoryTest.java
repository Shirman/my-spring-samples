package cn.shirman.learn.spring.redisrepositoriessample.service;


import static org.assertj.core.api.Assertions.*;
import cn.shirman.learn.spring.redisrepositoriessample.RedisRepositoriesSampleApplicationTests;
import cn.shirman.learn.spring.redisrepositoriessample.entity.Address;
import cn.shirman.learn.spring.redisrepositoriessample.entity.Gender;
import cn.shirman.learn.spring.redisrepositoriessample.entity.Person;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 描述：
 * 作者：shirman
 * 日期：2020-03-14 10:08 下午
 */
public class PersonRepositoryTest extends RedisRepositoriesSampleApplicationTests{
    @Autowired
    private PersonRepository personRepository;

    /**
     * 测试数据集合
     */
    Person eddard = new Person("军军", "谢", Gender.MALE);
    Person robb = new Person("robb", "stark", Gender.MALE);
    Person sansa = new Person("sansa", "stark", Gender.FEMALE);
    Person arya = new Person("arya", "stark", Gender.FEMALE);
    Person bran = new Person("bran", "stark", Gender.MALE);
    Person rickon = new Person("rickon", "stark", Gender.MALE);
    Person jon = new Person("jon", "snow", Gender.MALE);

    @Test
    public void addAll() {
        cleanAll();
        personRepository.saveAll(Arrays.asList(eddard, robb, sansa, arya, bran, rickon, jon));
    }

    @Test
    public void deleteAll() {
        cleanAll();
    }

    @Test
    public void saveOne(){
        personRepository.save(robb);
        Assert.assertNotNull(personRepository.findById(robb.getId()));
    }

    @Test
    public void findBySingleProperty(){
        flushTestUsers();
        List<Person> starks = personRepository.findByLastname("stark");
        assertThat(starks).contains(robb, sansa, arya, bran, rickon).doesNotContain(eddard, jon);
    }

    @Test
    public void findByQueryByExample(){
        flushTestUsers();
        Example<Person> ex = Example.of(new Person(null, "stark", null));
        Iterable<Person> all = personRepository.findAll(ex);
        assertThat(all).contains(robb, sansa).doesNotContain(eddard);
    }

    @Test
    public void findByReturningPage(){
        flushTestUsers();
        Page<Person> page1 = personRepository.findPeopleByLastname(robb.getLastname(), PageRequest.of(0, 4));
        assertThat(page1.getNumberOfElements()).isEqualTo(4);
        assertThat(page1.getTotalElements()).isEqualTo(5);
    }

    @Test
    public void findByEmbeddedProperty() {

        Address winterfell = new Address();
        winterfell.setCountry("the north");
        winterfell.setCity("winterfell");

        eddard.setAddress(winterfell);

        flushTestUsers();

        List<Person> eddardStark = personRepository.findByAddress_City(winterfell.getCity());

        assertThat(eddardStark).containsOnly(eddard);
    }

    @Test
    public void findByGeoLocationProperty() {

        Address winterfell = new Address();
        winterfell.setCountry("the north");
        winterfell.setCity("winterfell");
        winterfell.setLocation(new Point(52.9541053, -1.2401016));

        eddard.setAddress(winterfell);

        Address casterlystein = new Address();
        casterlystein.setCountry("Westerland");
        casterlystein.setCity("Casterlystein");
        casterlystein.setLocation(new Point(51.5287352, -0.3817819));

        robb.setAddress(casterlystein);

        flushTestUsers();

        Circle innerCircle = new Circle(new Point(51.8911912, -0.4979756), new Distance(50, Metrics.KILOMETERS));
        List<Person> eddardStark = personRepository.findByAddress_LocationWithin(innerCircle);

        assertThat(eddardStark).containsOnly(robb);

        Circle biggerCircle = new Circle(new Point(51.8911912, -0.4979756), new Distance(200, Metrics.KILOMETERS));
        List<Person> eddardAndRobbStark = personRepository.findByAddress_LocationWithin(biggerCircle);

        assertThat(eddardAndRobbStark).hasSize(2).contains(robb, eddard);
    }

    @Test
    public void useReferencesToStoreDataToOtherObjects() {

        flushTestUsers();

        eddard.setChildren(Arrays.asList(robb));

        personRepository.save(eddard);
    }

    private void cleanAll(){
        personRepository.deleteAll();
    }

    private void flushTestUsers() {
        cleanAll();
        personRepository.saveAll(Arrays.asList(eddard, robb, sansa, arya, bran, rickon, jon));
    }
}