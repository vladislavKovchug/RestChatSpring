package com.teamdev.chat.proxy;

import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.service.UserAuthenticationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import javax.inject.Inject;

@Aspect
public class AuthenticationCheckerAspect {

    @Inject
    UserAuthenticationService userAuthenticationService;

    @Pointcut("execution(* com.teamdev.chat.service.*.*(com.teamdev.chat.dto.UserId, .., com.teamdev.chat.dto.TokenDTO))" +
            "&& !execution(* com.teamdev.chat.service.UserAuthenticationService.validateToken(..))" +
            "&& args(userId, .., token)) ")
    public void authenticationPointcut(UserId userId, TokenDTO token){

    }

    @Around("authenticationPointcut(userId, token)")
    public Object logBefore(ProceedingJoinPoint joinPoint, UserId userId, TokenDTO token) throws Throwable {
        userAuthenticationService.validateToken(userId, token);
        return joinPoint.proceed();
    }

}
