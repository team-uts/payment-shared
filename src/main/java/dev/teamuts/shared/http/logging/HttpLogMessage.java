package dev.teamuts.shared.http.logging;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.teamuts.shared.config.AppProfile;
import dev.teamuts.shared.utils.HttpMsgUtils;
import dev.teamuts.shared.utils.StringUtils;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.yaml.snakeyaml.util.UriEncoder;

@Builder(access = AccessLevel.PRIVATE)
public class HttpLogMessage {
  private String httpMethod;
  private String requestUri;
  private int httpStatus;
  private String clientIp;
  private double elapsedTime;
  private String requestHeaders;
  private String requestParam;
  private String requestBody;
  private String responseHeaders;
  private String responseBody;

  private static final int MAX_BODY_LENGTH = 1000;
  private static final Gson gson =
      new GsonBuilder()
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create();

  public static HttpLogMessage newInstance(
      ContentCachingRequestWrapper requestWrapper,
      ContentCachingResponseWrapper responseWrapper,
      double elapsedTime) {
    return HttpLogMessage.builder()
        .httpMethod(requestWrapper.getMethod())
        .requestUri(requestWrapper.getRequestURI())
        .httpStatus(HttpStatus.valueOf(responseWrapper.getStatus()).value())
        .clientIp(HttpClientIpType.getClientIpInHeader(requestWrapper))
        .elapsedTime(elapsedTime)
        .requestHeaders(HttpMsgUtils.getRequestHeaders(requestWrapper))
        .requestParam(HttpMsgUtils.getRequestParams(requestWrapper))
        .requestBody(
            StringUtils.convertByteArrayToStr(
                requestWrapper.getContentAsByteArray(), MAX_BODY_LENGTH))
        .responseHeaders(HttpMsgUtils.getResponseHeaders(responseWrapper))
        .responseBody(
            StringUtils.convertByteArrayToStr(
                responseWrapper.getContentAsByteArray(), MAX_BODY_LENGTH))
        .build();
  }

  public String getLogMsg(AppProfile appProfile) {
    if (appProfile.isLocal()) {
      return this.toPrettierLog();
    }

    return this.toJson();
  }

  private String toJson() {
    return gson.toJson(this);
  }

  private String toPrettierLog() {
    return """

    [REQUEST] %s %s %s (%ss)
    >> CLIENT_IP: %s
    >> [INPUT] REQUEST_HEADERS: %s
    >> [INPUT] REQUEST_PARAM: %s
    >> [INPUT] REQUEST_BODY: %s
    >> [OUTPUT] RESPONSE_HEADERS: %s
    >> [OUTPUT] RESPONSE_BODY: %s
    """
        .formatted(
            this.httpMethod,
            UriEncoder.decode(this.requestUri),
            this.httpStatus,
            this.elapsedTime,
            this.clientIp,
            this.requestHeaders,
            this.requestParam,
            this.requestBody,
            this.responseHeaders,
            this.responseBody)
        .stripTrailing();
  }
}
