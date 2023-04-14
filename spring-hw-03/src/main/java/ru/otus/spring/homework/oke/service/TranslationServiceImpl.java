package ru.otus.spring.homework.oke.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.config.TestingLocalizationProperties;

import java.util.Locale;

@Service
public class TranslationServiceImpl implements TranslationService {
    private final MessageSource messageSource;

    private final Locale locale;

    public TranslationServiceImpl(MessageSource messageSource,
                                  TestingLocalizationProperties testingLocalizationProperties) {
        this.messageSource = messageSource;
        this.locale = testingLocalizationProperties.getLocale();
    }

    @Override
    public String getTranslatedString(String code) {
        return this.messageSource.getMessage(code, null, code, this.locale);
    }

    @Override
    public String getTranslatedString(String code, Object[] args) {
        return this.messageSource.getMessage(code, args, code, this.locale);
    }
}
