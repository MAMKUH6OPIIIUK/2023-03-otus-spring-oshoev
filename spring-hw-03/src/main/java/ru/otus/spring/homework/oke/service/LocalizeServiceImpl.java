package ru.otus.spring.homework.oke.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.config.LocaleProvider;
import java.util.Locale;

@Service
public class LocalizeServiceImpl implements LocalizeService {
    private final MessageSource messageSource;

    private final Locale locale;

    public LocalizeServiceImpl(MessageSource messageSource,
                               LocaleProvider localeProvider) {
        this.messageSource = messageSource;
        this.locale = localeProvider.getLocale();
    }

    @Override
    public String getMessage(String code) {
        return this.messageSource.getMessage(code, null, code, this.locale);
    }

    @Override
    public String getMessage(String code, Object[] args) {
        return this.messageSource.getMessage(code, args, code, this.locale);
    }
}
