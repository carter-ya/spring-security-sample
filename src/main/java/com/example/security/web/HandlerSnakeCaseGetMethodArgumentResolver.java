package com.example.security.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 用于绑定snake_case形式的参数
 *
 * @see SnakeCaseField
 */
public class HandlerSnakeCaseGetMethodArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(SnakeCaseField.class) != null;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    String parameterName = parameter.getParameterName();

    Object bean = mavContainer.containsAttribute(parameterName) ? mavContainer.getModel().get(parameterName)
        : BeanUtils.instantiate(parameter.getParameterType());
    WebDataBinder binder = binderFactory.createBinder(webRequest, bean, parameterName);
    if (binder.getTarget() != null) {
      Map<String, String[]> convertMap = new LinkedHashMap<>();
      Iterator<String> itr = webRequest.getParameterNames();
      while (itr.hasNext()) {
        String name = itr.next();
        convertMap.put(toCamelCase(name), webRequest.getParameterValues(name));
      }
      binder.bind(new MutablePropertyValues(convertMap));
    }
    Validated validated = parameter.getDeclaringClass().getAnnotation(Validated.class);
    // 启用Bean验证
    if (parameter.getParameterAnnotation(Valid.class) != null || validated != null) {
      Object[] hints = validated != null ? validated.value() : new Object[]{};
      binder.validate(hints);
    }
    // 是否需要抛出异常
    if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
      throw new BindException(binder.getBindingResult());
    }
    return binder.convertIfNecessary(bean, parameter.getParameterType(), parameter);
  }

  /**
   * Whether to raise a fatal bind exception on validation errors.
   *
   * @param binder the data binder used to perform data binding
   * @param parameter the method parameter declaration
   * @return {@code true} if the next method parameter is not of type {@link Errors}
   */
  protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
    int i = parameter.getParameterIndex();
    Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
    boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
    return !hasBindingResult;
  }

  private String toCamelCase(String name) {
    StringBuilder builder = new StringBuilder(name.length() * 2);
    boolean preIsUnderline = false;
    for (int index = 0; index < name.length(); index++) {
      char ch = name.charAt(index);
      if (ch == '_') {
        preIsUnderline = true;
        continue;
      }
      if (preIsUnderline && (ch >= 'a' && ch <= 'z')) {
        builder.append((char) (ch - 32));
        preIsUnderline = false;
      } else {
        builder.append(ch);
      }
    }
    return builder.toString();
  }

  /**
   * 标记字段解析是snake_case形式
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.PARAMETER)
  public @interface SnakeCaseField {

  }
}
