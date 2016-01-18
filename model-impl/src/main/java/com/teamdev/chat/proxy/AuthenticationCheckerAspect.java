package com.teamdev.chat.proxy;

import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.service.UserAuthenticationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import javax.inject.Inject;


@Aspect
@Service
public class AuthenticationCheckerAspect {

    @Inject
    UserAuthenticationService userAuthenticationService;

    @Pointcut(value = "execution(* com.teamdev.chat.service.*.*(com.teamdev.chat.dto.UserId, .., com.teamdev.chat.dto.TokenDTO))" +
            "&& !execution(* com.teamdev.chat.service.UserAuthenticationService.validateToken(..))" +
            "&& args(userId, .., token)) ", argNames = "userId,token")
    public void authenticationPointcut(UserId userId, TokenDTO token){

    }

    @Around(value = "authenticationPointcut(userId, token)", argNames = "joinPoint,userId,token")
    public Object checkIsTokenValid(ProceedingJoinPoint joinPoint, UserId userId, TokenDTO token) throws Throwable {
        userAuthenticationService.validateToken(userId, token);
        return joinPoint.proceed();
    }

}
