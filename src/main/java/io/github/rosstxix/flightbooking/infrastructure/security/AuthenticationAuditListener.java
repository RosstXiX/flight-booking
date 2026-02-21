package io.github.rosstxix.flightbooking.infrastructure.security;

import io.github.rosstxix.flightbooking.infrastructure.logging.LogMessageFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationAuditListener {

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        if (!(event.getAuthentication() instanceof UsernamePasswordAuthenticationToken)) {
            return;
        }
        String username = event.getAuthentication().getName();
        log.info(LogMessageFormatter.successLogin(username));
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        if (!(event.getAuthentication() instanceof UsernamePasswordAuthenticationToken)) {
            return;
        }
        String username = event.getAuthentication().getName();
        String reason = event.getException().getClass().getSimpleName();
        log.warn(LogMessageFormatter.failedLogin(username, reason));
    }

}
