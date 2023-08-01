package ru.otus.spring.homework.oke.service;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
@StepScope
public class ExecutionContextKeyValueCacheService implements KeyValueCacheService {
    private final ExecutionContext executionContext;

    public ExecutionContextKeyValueCacheService(@Value("#{stepExecution.jobExecution.executionContext}")
                                                        ExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    @Override
    public void putString(String key, String value) {
        this.executionContext.putString(key, value);
    }

    @Override
    public String getString(String key) {
        return this.executionContext.getString(key);
    }
}
