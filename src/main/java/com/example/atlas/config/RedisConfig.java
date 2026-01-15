package com.example.atlas.config;

import com.example.atlas.comments.dto.CommentsResponse;
import com.example.atlas.task.dto.TaskResponse;
import com.example.atlas.users.dto.UserResponse;
import com.example.atlas.workspace.dto.WorkSpaceResponse;
import com.example.atlas.workspacemembers.dto.WorkspaceMemberResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        // Typed serializers for each DTO
        Jackson2JsonRedisSerializer<UserResponse> userSerializer =
                new Jackson2JsonRedisSerializer<>(mapper, UserResponse.class);
        Jackson2JsonRedisSerializer<WorkSpaceResponse> workspaceSerializer =
                new Jackson2JsonRedisSerializer<>(mapper, WorkSpaceResponse.class);
        Jackson2JsonRedisSerializer<WorkspaceMemberResponse> memberSerializer =
                new Jackson2JsonRedisSerializer<>(mapper, WorkspaceMemberResponse.class);
        Jackson2JsonRedisSerializer<TaskResponse> taskSerializer =
                new Jackson2JsonRedisSerializer<>(mapper, TaskResponse.class);
        Jackson2JsonRedisSerializer<CommentsResponse> commentSerializer =
                new Jackson2JsonRedisSerializer<>(mapper, CommentsResponse.class);

        // Default config (if needed, fallback)
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig();

        // Map each cache name to its config
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put("user", defaultConfig.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(userSerializer)));
        cacheConfigs.put("workspace", defaultConfig.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(workspaceSerializer)));
        cacheConfigs.put("member", defaultConfig.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(memberSerializer)));
        cacheConfigs.put("task", defaultConfig.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(taskSerializer)));
        cacheConfigs.put("comment", defaultConfig.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(commentSerializer)));

        return RedisCacheManager.builder(factory)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}
