package dev.teamuts.shared.data;

import static dev.teamuts.shared.utils.AppTimeUtils.GLOBAL_ZONED_DATETIME_FORMAT;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

public class AppObjectMapperConfig {
  /**
   * - {@code .disable(DateTimeFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)}<br>
   * keeps the original timezone in json string when serializing ZonedDateTime.<br>
   * otherwise, it will be converted to the system default timezone.
   */
  public static ObjectMapper baseObjectMapper() {
    return JsonMapper.builder()
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .withConfigOverride(
            ZonedDateTime.class,
            config ->
                config.setFormat(
                    JsonFormat.Value.forPattern(GLOBAL_ZONED_DATETIME_FORMAT)
                        .withShape(JsonFormat.Shape.STRING)))
        .disable(DateTimeFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
        .build();
  }
}
