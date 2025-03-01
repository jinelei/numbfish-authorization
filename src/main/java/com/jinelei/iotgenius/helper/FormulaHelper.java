package com.jinelei.iotgenius.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jinelei.iotgenius.exception.InternalException;
import com.jinelei.iotgenius.exception.InvalidArgsException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @Author: jinelei
 * @Description: 表达式计算帮助类
 * @Date: 2023/04/18
 * @Version: 1.0.0
 */
@SuppressWarnings("unused")
@Slf4j
@Getter
public class FormulaHelper<T> {
    protected final static Predicate<String> IS_VARIABLE = s -> StringUtils.hasLength(s) && !s.matches("^[0-9.]*$");
    protected final static Predicate<Character> IS_OPERATOR = c -> Arrays
            .asList('+', '-', '*', '/', '%', '(', ')', ' ', '>', '<', '=', '&', '|', '!').contains(c);
    /**
     * 本地计算缓存
     */
    protected final Cache<String, T> resultCached = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(5)).build();
    /**
     * 解析表达式的任务
     */
    protected final Runnable resolveFormulaTask;
    /**
     * 计算任务
     */
    protected final Function<Class<T>, T> calculateTask;
    /**
     * 构建缓存的key
     */
    protected final Supplier<String> buildCacheKey;
    /**
     * 表达式
     */
    protected final String formula;
    /**
     * 表达式变量值集合
     */
    protected final Map<String, Object> variables = new HashMap<>();
    /**
     * 追踪数据
     */
    private final Map<String, Object> traceData = new HashMap<>(10);

    public FormulaHelper(String formula) {
        this.formula = formula;
        this.resolveFormulaTask = () -> {
            // 解析表达式，取出所有的变量
            final StringBuilder sb = new StringBuilder();
            final AtomicBoolean isVariable = new AtomicBoolean(false);
            final Runnable saveToVariable = () -> Optional.of(sb.toString())
                    .map(v -> v.replaceAll("[{}]", " "))
                    .map(String::trim)
                    .filter(IS_VARIABLE)
                    .ifPresent(s -> {
                        variables.put(s, 0.0);
                        sb.setLength(0);
                        isVariable.set(false);
                    });
            for (char c : this.formula.toCharArray()) {
                if (c == '#') {
                    isVariable.set(true);
                } else if (IS_OPERATOR.test(c)) {
                    // 是运算符，闭合变量
                    saveToVariable.run();
                } else if (isVariable.get()) {
                    // 是变量，追加进入sb
                    sb.append(c);
                }
            }
            // 结束，判断是否是变量，如果是变量，追加进入tokens
            saveToVariable.run();
        };
        this.calculateTask = clazz -> {
            try {
                // 执行计算，并设置到结果中
                ExpressionParser parser = new SpelExpressionParser();
                final StandardEvaluationContext context = new StandardEvaluationContext();
                variables.forEach(context::setVariable);
                Expression expression = parser.parseExpression(formula);
                T value = Optional.ofNullable(expression.getValue(context, clazz))
                        .orElseThrow(() -> new InvalidArgsException("计算结果为空"));
                log.debug("计算表达式成功，表达式：{}，结果：{}", formula, value);
                traceData.put("formula", formula);
                traceData.put("variableValues", variables);
                traceData.put("result", value);
                return value;
            } catch (Throwable throwable) {
                log.error("计算表达式出错，表达式：{}，错误信息：{}", formula, throwable.toString());
                throw new InvalidArgsException("计算表达式出错");
            }
        };
        this.buildCacheKey = () -> {
            final StringBuffer sb = new StringBuffer();
            sb.append("f_").append(formula).append("(");
            variables.keySet().stream().sorted()
                    .forEach(k -> sb.append("_vk_").append(k).append("_vv_").append(variables.get(k)));
            sb.append(")");
            return sb.toString();
        };
        resolveFormulaTask.run();
    }

    /**
     * 获取已经存在的key
     *
     * @return key
     */
    public Set<String> getVariableNames() {
        return variables.keySet();
    }

    /**
     * 设置变量值
     *
     * @param key   变量名
     * @param value 变量值
     */
    public void setVariable(String key, Object value) {
        variables.put(key, value);
    }

    /**
     * 添加全部字典
     *
     * @param map 字典
     */
    public void setVariables(Map<String, Object> map) {
        variables.putAll(map);
    }

    /**
     * 计算表达式
     *
     * @param clazz 返回值类型
     * @throws InvalidArgsException 参数错误
     */
    public T calculate(Class<T> clazz) {
        return resultCached.get(buildCacheKey.get(), k -> calculateTask.apply(clazz));
    }

    /**
     * 解析指定位置的变量值
     *
     * @param variable 变量
     * @param type     类型
     * @return 值
     */
    public static String resolvePlaceHolder(final String variable, final String type) {
        return Optional.ofNullable(variable)
                .map(String::trim)
                .filter(StringUtils::hasLength)
                .map(v -> v.replace('{', ' '))
                .map(v -> v.replace('}', ' '))
                .map(String::trim)
                .filter(StringUtils::hasLength)
                .map(v -> v.split("_"))
                .filter(arr -> arr.length % 2 == 0)
                .map(arr -> {
                    for (int i = 0; i < arr.length; i += 2) {
                        if (type.equals(arr[i])) {
                            return arr[i + 1];
                        }
                    }
                    return null;
                })
                .orElseThrow(() -> new InvalidArgsException("变量不合法"));
    }

    /**
     * 解析设备id
     *
     * @param variable 原始信息
     * @return 值
     */
    public static String resolveDeviceId(final String variable) {
        return resolvePlaceHolder(variable, "dev");
    }

    /**
     * 解析参数
     *
     * @param variable 原始信息
     * @return 值
     */
    public static String resolveParam(final String variable) {
        return resolvePlaceHolder(variable, "para");
    }

    public Map<String, Object> getTraceData() {
        if (traceData.isEmpty()) {
            throw new InternalException("表达式未计算");
        }
        return traceData;
    }

    public String getTraceDataString() {
        if (traceData.isEmpty()) {
            throw new InternalException("表达式未计算");
        }
        return SpringHelper.getBean(ObjectMapper.class).map(om -> {
            try {
                return om.writeValueAsString(traceData);
            } catch (Throwable ignore) {
                return "";
            }
        }).orElse("");
    }
}
