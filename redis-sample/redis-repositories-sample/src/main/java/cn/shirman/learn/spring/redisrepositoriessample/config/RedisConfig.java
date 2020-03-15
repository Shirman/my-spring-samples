package cn.shirman.learn.spring.redisrepositoriessample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.keyvalue.core.mapping.KeySpaceResolver;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * 描述：
 * 作者：shirman
 * 日期：2020-03-14 10:45 下午
 */
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Value("${spring.application.name:public}")
    private String redisKeyPrefix;

    public static final String CONCAT_STR = ":";

    @Bean
    RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisMappingContext keyValueMappingContext() {
        MyRedisMappingContext redisMappingContext = new MyRedisMappingContext(
                new MappingConfiguration(new IndexConfiguration(), new KeyspaceConfiguration()));
        redisMappingContext.setFallbackKeySpaceResolver(new MyRedisMappingContext
                .MyConfigAwareKeySpaceResolver(
                        redisMappingContext.getMappingConfiguration().getKeyspaceConfiguration(),
                        redisKeyPrefix)
                );
        return redisMappingContext;
    }

    public static class MyRedisMappingContext extends RedisMappingContext {

        public MyRedisMappingContext(MappingConfiguration mappingConfiguration) {
            super(mappingConfiguration);
        }

        static class MyConfigAwareKeySpaceResolver implements KeySpaceResolver {

            private final KeyspaceConfiguration keyspaceConfig;
            private String prefix;

            public MyConfigAwareKeySpaceResolver(KeyspaceConfiguration keyspaceConfig, String prefix) {
                this.keyspaceConfig = keyspaceConfig;
                this.prefix = prefix;
            }

            @Override
            public String resolveKeySpace(Class<?> type) {

                Assert.notNull(type, "Type must not be null!");
                if (keyspaceConfig.hasSettingsFor(type)) {

                    String value = keyspaceConfig.getKeyspaceSettings(type).getKeyspace();
                    if (StringUtils.hasText(value)) {
                        return concatPrefix(value);
                    }
                }
                return concatPrefix(ClassSimpleNameKeySpaceResolver.INSTANCE.resolveKeySpace(type));
            }

            private String concatPrefix(String value){
                return prefix + CONCAT_STR + value;
            }
        }

        enum ClassSimpleNameKeySpaceResolver implements KeySpaceResolver {

            INSTANCE;

            @Override
            public String resolveKeySpace(Class<?> type) {

                Assert.notNull(type, "Type must not be null!");
                return ClassUtils.getUserClass(type).getSimpleName().toLowerCase();
            }
        }
    }
}
