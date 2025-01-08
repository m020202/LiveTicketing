package redisX.ticket.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import redisX.ticket.config.RedissonLock;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

}
