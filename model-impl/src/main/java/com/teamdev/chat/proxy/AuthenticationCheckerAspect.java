package com.teamdev.chat.proxy;

import com.teamdev.chat.dto.TokenDTO;
import com.teamdev.chat.dto.UserId;
import com.teamdev.chat.service.UserAuthenticationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import javax.inject.Inject;

@Aspect
public class AuthenticationCheckerAspect {

    @Inject
    UserAuthenticationService userAuthenticationService;

    //@Before("execution(* com.mkyong.customer.bo.CustomerBo.addCustomer(..))")
    @Around("execution(* com.teamdev.chat.service.*.*(com.teamdev.chat.dto.UserId, .., com.teamdev.chat.dto.TokenDTO)) " +
            "&& !execution(* com.teamdev.chat.service.UserAuthenticationService.validateToken(..))")
    public Object logBefore(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("just called " + joinPoint.getSignature().getName());

        final Object[] args = joinPoint.getArgs();
        final UserId userId = (UserId)args[0];
        final TokenDTO token = (TokenDTO)args[args.length - 1];
        userAuthenticationService.validateToken(userId, token);

        return joinPoint.proceed();
    }

}
