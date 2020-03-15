package cn.shirman.learn.spring.redisrepositoriessample.service;

import cn.shirman.learn.spring.redisrepositoriessample.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Circle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：
 * 作者：shirman
 * 日期：2020-03-14 10:00 下午
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, String>, QueryByExampleExecutor<Person> {
    List<Person> findByLastname(String lastname);

    Page<Person> findPeopleByLastname(String lastname, Pageable page);

    List<Person> findByFirstnameAndLastname(String firstname, String lastname);

    List<Person> findByFirstnameOrLastname(String firstname, String lastname);

    List<Person> findByAddress_City(String city);

    List<Person> findByAddress_LocationWithin(Circle circle);
}
