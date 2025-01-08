package redisX.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SampleSpinLock {
    private final RedisTemplate redisTemplate;

    public boolean spinLock(String key) throws InterruptedException {
        Boolean getKey = Boolean.FALSE;
        while (true) {
            getKey = redisTemplate.opsForValue().setIfAbsent(key, "lock", 300L, TimeUnit.MILLISECONDS);

            if (getKey)
                break;
            else {
                Thread.sleep(50);
            }
        }

        if (!getKey) return false;
        return true;
    }
}
