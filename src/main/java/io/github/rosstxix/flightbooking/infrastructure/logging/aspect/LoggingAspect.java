package io.github.rosstxix.flightbooking.infrastructure.logging.aspect;

import io.github.rosstxix.flightbooking.infrastructure.logging.LogMessageFormatter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
@Aspect
public class LoggingAspect {

    @Around("@annotation(io.github.rosstxix.flightbooking.infrastructure.logging.aspect.Loggable) || @within(io.github.rosstxix.flightbooking.infrastructure.logging.aspect.Loggable)")
    public Object logExecution(ProceedingJoinPoint pjp) throws Throwable {
        String className = pjp.getSignature().getDeclaringTypeName();
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
        String methodName = pjp.getSignature().getName();
        String argsFormatted = formatArgs(pjp.getArgs());

        log.debug(LogMessageFormatter.enterMethod(simpleClassName, methodName, argsFormatted));

        long startTime = System.currentTimeMillis();

        try {
            Object result = pjp.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            log.debug(LogMessageFormatter.exitMethod(simpleClassName, methodName, executionTime));

            return result;
        } catch (Exception ex) {
            long executionTime = System.currentTimeMillis() - startTime;

            log.debug(LogMessageFormatter.failedMethod(simpleClassName, methodName, executionTime, ex));

            throw ex;
        }
    }

    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }

        return Arrays.stream(args)
                .map(arg -> arg != null ? arg.toString() : "null")
                .collect(Collectors.joining(", "));
    }
}
