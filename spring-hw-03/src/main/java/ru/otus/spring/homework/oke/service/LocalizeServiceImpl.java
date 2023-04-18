package ru.otus.spring.homework.oke.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.config.LocaleProvider;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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

    /**
     * Метод для поиска ресурса по его базовому имени и расширению файла, относящегося к сконфигурированной для
     * приложения локали.
     * Локализованные ресурсы ищутся от конкретных локалей к более общим
     *
     * @param baseName базовое имя ресурса
     * @param suffix   расширение файла ресурса
     * @return URL найденного ресурса для установленной локали, либо null
     */
    @Override
    public URL getResourceUrl(String baseName, String suffix) {
        ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_DEFAULT);
        List<Locale> localeCandidates = control.getCandidateLocales(baseName, this.locale);
        ClassLoader classLoader = getClass().getClassLoader();
        for (Locale specificLocale : localeCandidates) {
            String bundleName = control.toBundleName(baseName, specificLocale);
            String resourceName = control.toResourceName(bundleName, suffix);
            URL url = classLoader.getResource(resourceName);
            if (url != null) {
                return url;
            }
        }
        return null;
    }
}
