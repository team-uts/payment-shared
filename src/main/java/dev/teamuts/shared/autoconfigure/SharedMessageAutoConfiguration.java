package dev.teamuts.shared.autoconfigure;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

@AutoConfiguration(before = MessageSourceAutoConfiguration.class)
@EnableConfigurationProperties(MessageSourceProperties.class)
public class SharedMessageAutoConfiguration {

  @Bean(name = "messageSource")
  @ConditionalOnMissingBean(name = "messageSource", value = MessageSource.class)
  public MessageSource messageSource(MessageSourceProperties properties) {
    ResourceBundleMessageSource source = new ResourceBundleMessageSource();

    List<String> basenames = new ArrayList<>(properties.getBasename());
    basenames.add("shared-messages");
    source.setBasenames(basenames.toArray(new String[0]));

    source.setDefaultEncoding(properties.getEncoding().name());
    source.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
    source.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());

    Duration cacheDuration = properties.getCacheDuration();
    if (cacheDuration != null) {
      source.setCacheMillis(cacheDuration.toMillis());
    }

    source.setCommonMessages(loadCommonMessages(properties.getCommonMessages()));

    return source;
  }

  private Properties loadCommonMessages(List<Resource> resources) {
    Properties messages = new Properties();

    if (resources == null) {
      return messages;
    }

    for (Resource resource : resources) {
      try {
        PropertiesLoaderUtils.fillProperties(messages, resource);
      } catch (IOException e) {
        throw new UncheckedIOException("Failed to load messages from " + resource, e);
      }
    }

    return messages;
  }
}
