package raf.si.racunovodstvo.user.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
@Profile(value = "prod")
public class RedisCacheConfig {

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.host}")
    private String host;

    @Bean
    @Primary
    public RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                                      .entryTtl(Duration.ofMinutes(60))
                                      .disableCachingNullValues()
                                      .serializeKeysWith(SerializationPair.fromSerializer(RedisSerializer.string()))
                                      .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json()));
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }
}
