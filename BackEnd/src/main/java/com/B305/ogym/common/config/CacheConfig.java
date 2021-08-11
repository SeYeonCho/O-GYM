package com.B305.ogym.common.config;


import com.B305.ogym.common.properties.CacheProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@EnableCaching
@Configuration
public class CacheConfig {

    private final CacheProperties cacheProperties;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean(name = "redisCacheConnectionFactory")
    public RedisConnectionFactory redisCacheConnectionFactory() {
        return new LettuceConnectionFactory(redisHost,
            redisPort);
    }


    private ObjectMapper objectMapper() {

        // 2.9 이하 버전까지 적용 가능
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        mapper.registerModule(new JavaTimeModule());
//        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // jackson 2.10이상 3.0버전까지 적용 가능
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
            .allowIfSubType(Object.class)
            .build();

        return JsonMapper.builder()
            .polymorphicTypeValidator(ptv)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .addModule(new JavaTimeModule())
            .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL)
            .build();
    }

    // redisCacheManager에 여러 옵션을 부여할 수 있는 오브젝트
    // 캐시의 key/value를 직렬화 - 역직렬화하는 Pair
    private RedisCacheConfiguration redisCacheDefaultConfiguration() {
        return RedisCacheConfiguration
            .defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));
    }

    private Map<String, RedisCacheConfiguration> redisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        for (Entry<String, Long> cacheNameAndTimeout : cacheProperties.getTtl().entrySet()) {
            cacheConfigurations
                .put(cacheNameAndTimeout.getKey(), redisCacheDefaultConfiguration().entryTtl(
                    Duration.ofSeconds(cacheNameAndTimeout.getValue())));
        }
        return cacheConfigurations;
    }

    /**
     * 캐시 매니저를 등록한다.  스프링에서 기본적으로 지원하는 캐시 저장소는 JDK의 ConcuurentHashMap이며 그 외 캐시 저장소를 사용하기 위해서는 캐시
     * 매니저를 Bean으로 등록해서 사용해야 한다. withInitialCacheConfigurations에 캐시의 종류별로 만료기간을 설정한
     * redisCacheConfigurationMap을
     */
    @Bean
    public CacheManager redisCacheManager(
        @Qualifier("redisCacheConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(redisCacheDefaultConfiguration())
            .withInitialCacheConfigurations(redisCacheConfigurationMap()).build();
    }


}
