package cn.shirman.learn.spring.redisrepositoriessample.entity;

import lombok.Data;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.index.GeoIndexed;
import org.springframework.data.redis.core.index.Indexed;

/**
 * 描述：
 * 作者：shirman
 * 日期：2020-03-14 9:49 下午
 */
@Data
public class Address {
    @Indexed
    private String city;
    private String country;
    @GeoIndexed
    private Point location;
}
