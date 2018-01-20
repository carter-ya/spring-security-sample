package com.example.security.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.Data;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 处理操作者参数的参数解析器
 */
@Data
public class HandlerRequireOperatorMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private OperatorResolver operatorResolver;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    if (parameter.getParameterAnnotation(RequireOperator.class) == null) {
      return false;
    }
    Class<?> clazz = parameter.getParameterType();
    return clazz.isAssignableFrom(Operator.class) && operatorResolver.isSupportClass(clazz);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    return operatorResolver.resolve(webRequest, binderFactory);
  }

  /**
   * 操作者解析器
   */
  public interface OperatorResolver {

    /**
     * 是否是支持的操作者类型
     */
    default boolean isSupportClass(Class<?> clazz) {
      return clazz == Operator.class;
    }

    /**
     * 解析为操作者
     */
    Operator resolve(NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception;
  }

  /**
   * 操作者信息不会改变的解析器
   */
  public static class NotChangedOperatorResolver implements OperatorResolver {

    private final Long userId;
    private final String name;

    public NotChangedOperatorResolver(Long userId, String name) {
      this.userId = userId;
      this.name = name;
    }

    @Override
    public Operator resolve(NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
      return new SimpleOperator(userId, name);
    }
  }

  /**
   * 操作者，该接口是一个适配器，可以适用于任何用户系统
   */
  public interface Operator {

    /**
     * 获取操作用户ID
     */
    Long getUserId();

    /**
     * 获取操作用户名字
     */
    String getName();
  }

  @Data
  public static class SimpleOperator implements Operator {

    private Long userId;
    private String name;

    public SimpleOperator(Long userId, String name) {
      this.userId = userId;
      this.name = name;
    }

    @Override
    public Long getUserId() {
      return userId;
    }

    @Override
    public String getName() {
      return name;
    }
  }

  /**
   * 标记该参数需要注入操作者信息
   */
  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface RequireOperator {

  }
}
