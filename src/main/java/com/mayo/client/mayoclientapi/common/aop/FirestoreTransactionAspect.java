package com.mayo.client.mayoclientapi.common.aop;

import com.google.cloud.firestore.Firestore;
import com.mayo.client.mayoclientapi.common.exception.ApplicationException;
import com.mayo.client.mayoclientapi.common.exception.payload.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Aspect
@Component
@RequiredArgsConstructor
public class FirestoreTransactionAspect {

    private final Firestore firestore;

    @Around("@within(com.mayo.client.mayoclientapi.common.annotation.FirestoreTransactional)")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            return firestore.runTransaction(transaction -> {
                try {
                    return joinPoint.proceed();
                } catch (ApplicationException e) {
                    throw e;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {

            Throwable cause = e.getCause();

            if (cause instanceof ApplicationException applicationException) {
                throw applicationException;
            }

            String errorMessage = "Firebase transaction 오류: " + (cause != null ? cause.getMessage() : e.getMessage());

            throw new ApplicationException(ErrorStatus.toErrorStatus(errorMessage, 500, LocalDateTime.now()));
        }
    }
}
