package dev.teamuts.shared.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.stream.Collectors;

public class HttpMsgUtils {
  public static String getRequestHeaders(HttpServletRequest request) {
    return Collections.list(request.getHeaderNames()).stream()
        .map(name -> "%s=%s".formatted(name, request.getHeader(name)))
        .collect(Collectors.joining("&"));
  }

  public static String getRequestParams(HttpServletRequest request) {
    return request.getParameterMap().entrySet().stream()
        .map(entry -> "%s=%s".formatted(entry.getKey(), String.join(",", entry.getValue())))
        .collect(Collectors.joining("&"));
  }

  public static String getResponseHeaders(HttpServletResponse response) {
    return response.getHeaderNames().stream()
        .map(name -> "%s=%s".formatted(name, response.getHeader(name)))
        .collect(Collectors.joining("&"));
  }
}
