package com.osh4.accounting.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.osh4.accounting.utils.Constants;

@Configuration
public class MainConfig
{
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Constants.ru_RU);
		return slr;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setAlwaysUseMessageFormat(true);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setBasenames("classpath:messages");

		return messageSource;
	}
}
