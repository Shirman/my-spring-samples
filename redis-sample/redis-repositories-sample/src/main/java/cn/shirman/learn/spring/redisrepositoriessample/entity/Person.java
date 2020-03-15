package cn.shirman.learn.spring.redisrepositoriessample.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

/**
 * 描述：
 * 作者：shirman
 * 日期：2020-03-14 9:54 下午
 */
@Data
@EqualsAndHashCode(exclude = {"children"})
@NoArgsConstructor
@RedisHash
public class Person {
    @Id
    private String id;
    @Indexed
    private String firstname;
    @Indexed
    private String lastname;
    private Gender gender;
    private Address address;
    @Reference
    private List<Person> children;

    public Person(String firstname, String lastname, Gender gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
    }
}
