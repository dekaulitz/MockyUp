package com.github.dekaulitz.mockyup.server.service.common.impl;

import org.springframework.retry.ExhaustedRetryException;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryState;

public interface RetryOperations {

  <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E;

  <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback,
      RecoveryCallback<T> recoveryCallback)
      throws E;

  <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RetryState retryState)
      throws E, ExhaustedRetryException;

  <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback,
      RecoveryCallback<T> recoveryCallback,
      RetryState retryState) throws E;
}
