package com.app.internal.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.listener.MessageListener;
import org.springframework.data.redis.listener.MessageListenerContainer;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import com.app.internal.handler.EducationActivityHandler;

@Component
public class EducationActivityQueue {

    private final StringRedisTemplate redisTemplate;
    private final EducationActivityHandler educationActivityHandler;

    @Autowired
    public EducationActivityQueue(StringRedisTemplate redisTemplate, EducationActivityHandler educationActivityHandler) {
        this.redisTemplate = redisTemplate;
        this.educationActivityHandler = educationActivityHandler;
    }

    @Bean
    public MessageListenerContainer messageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        container.addMessageListener(new MessageListenerAdapter(this, "processEducationActivityMessage"), new ChannelTopic("educationActivityChannel"));
        return container;
    }

    public void processEducationActivityMessage(String message) {
        // Handle message related to education activity (university review, major review)
        educationActivityHandler.handleEducationActivity(message);
    }
}