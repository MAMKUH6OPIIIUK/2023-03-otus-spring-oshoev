package ru.otus.spring.homework.oke.configuration;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.Locale;

@Component(WebHttpHandlerBuilder.LOCALE_CONTEXT_RESOLVER_BEAN_NAME)
public class LocalizationConfig implements LocaleContextResolver {

    @Override
    public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        boolean overrideLocaleCookie = false;
        String lang = exchange.getRequest().getQueryParams().getFirst("lang");
        Locale parameterLocale = this.parseLocale(lang);
        HttpCookie locale = exchange.getRequest().getCookies().getFirst("locale");
        Locale cookieLocale = this.parseLocale(locale);
        Locale targetLocale;
        if (parameterLocale == null && cookieLocale == null) {
            targetLocale = new Locale("ru");
            overrideLocaleCookie = true;
        } else if (parameterLocale != null) {
            targetLocale = parameterLocale;
            overrideLocaleCookie = true;
        } else {
            targetLocale = cookieLocale;
        }
        if (overrideLocaleCookie) {
            ResponseCookie newLocaleCookie = ResponseCookie.from("locale", targetLocale.toString()).build();
            exchange.getResponse().addCookie(newLocaleCookie);
        }
        return new SimpleLocaleContext(targetLocale);
    }

    @Override
    public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
        throw new UnsupportedOperationException();
    }

    private Locale parseLocale(String localeName) {
        if (localeName != null && !localeName.isEmpty()) {
            return new Locale(localeName);
        }
        return null;
    }

    private Locale parseLocale(HttpCookie localeNameCookie) {
        if (localeNameCookie != null) {
            String localeName = localeNameCookie.getValue();
            return parseLocale(localeName);
        }
        return null;
    }
}
