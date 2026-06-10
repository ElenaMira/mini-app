package cn.iocoder.boot.web.web.core.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.exception.ServiceException;
import cn.iocoder.boot.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.common.util.collection.SetUtils;
import cn.iocoder.boot.common.util.servlet.ServletUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Set;

import static cn.iocoder.boot.common.exception.enums.GlobalErrorCodeConstants.*;

/**
 *
 * 全局异常处理器，将 Exception 翻译成 CommonResult + 对应的异常编号
 * @author xiaosheng
 */
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 忽略的 ServiceException 错误提示，避免打印过多 logger
     */
    public static final Set<String> IGNORE_ERROR_MESSAGES = SetUtils.asSet("无效的刷新令牌");

    /**
     * 处理所有异常，主要是提供给 Filter 使用
     * 因为 Filter 不走 SpringMVC 的流程，但是我们又需要兜底处理异常，所以这里提供一个全量的异常处理过程，保持逻辑统一。
     *
     * @param request 请求
     * @param ex 异常
     * @return 通用返回
     */
    public CommonResult<?> allExceptionHandler(HttpServletRequest request, Throwable ex) {
        if (ex instanceof MissingServletRequestParameterException) {
            return missingServletRequestParameterExceptionHandler((MissingServletRequestParameterException) ex);
        }
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return methodArgumentTypeMismatchExceptionHandler((MethodArgumentTypeMismatchException) ex);
        }
        if (ex instanceof MethodArgumentNotValidException) {
            return methodArgumentNotValidExceptionExceptionHandler((MethodArgumentNotValidException) ex);
        }
        if (ex instanceof BindException) {
            return bindExceptionHandler((BindException) ex);
        }
        if (ex instanceof ConstraintViolationException) {
            return constraintViolationExceptionHandler((ConstraintViolationException) ex);
        }
//        if (ex instanceof ValidationException) {
//            return validationException((ValidationException) ex);
//        }
//        if (ex instanceof MaxUploadSizeExceededException) {
//            return maxUploadSizeExceededExceptionHandler((MaxUploadSizeExceededException) ex);
//        }
//        if (ex instanceof NoHandlerFoundException) {
//            return noHandlerFoundExceptionHandler((NoHandlerFoundException) ex);
//        }
//        if (ex instanceof HttpRequestMethodNotSupportedException) {
//            return httpRequestMethodNotSupportedExceptionHandler((HttpRequestMethodNotSupportedException) ex);
//        }
//        if (ex instanceof HttpMediaTypeNotSupportedException) {
//            return httpMediaTypeNotSupportedExceptionHandler((HttpMediaTypeNotSupportedException) ex);
//        }
        if (ex instanceof ServiceException) {
            return serviceExceptionHandler((ServiceException) ex);
        }
//        if (ex instanceof AccessDeniedException) {
//            return accessDeniedExceptionHandler(request, (AccessDeniedException) ex);
//        }
        return defaultExceptionHandler(request, ex);
    }

    /**
     * 处理 SpringMVC 请求参数缺失
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    private CommonResult<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数缺失:%s", ex.getParameterName()));
    }
    /**
     * 处理 SpringMVC 请求参数类型错误
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CommonResult<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.warn("[methodArgumentTypeMismatchExceptionHandler]", ex);
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数类型错误:%s", ex.getMessage()));
    }
    /**
     * 处理 SpringMVC 参数校验不正确
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<?> methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
        log.warn("[methodArgumentNotValidExceptionExceptionHandler]", ex);
        // 获取 errorMessage
        String errorMessage = null;
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError == null) {
            //兼容ObjectError(对象校验报错)
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            if (CollUtil.isNotEmpty(allErrors)) {
                errorMessage = allErrors.get(0).getDefaultMessage();
            }
        } else {
            errorMessage = fieldError.getDefaultMessage();
        }
        // 转换 CommonResult
        if (StrUtil.isEmpty(errorMessage)) {
            return CommonResult.error(BAD_REQUEST);
        }
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数不正确:%s", errorMessage));
    }

    /**
     * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
     * todo
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<?> bindExceptionHandler(BindException ex) {
        log.warn("[handleBindException]", ex);
        FieldError fieldError = ex.getFieldError();
        assert fieldError != null; // 断言，避免告警
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数不正确:%s", fieldError.getDefaultMessage()));
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     *
     * 例如说，接口上设置了 @RequestBody 实体中 xx 属性类型为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @SuppressWarnings("PatternVariableCanBeUsed")
    public CommonResult<?> methodArgumentTypeInvalidFormatExceptionHandler(HttpMessageNotReadableException ex) {
        log.warn("[methodArgumentTypeInvalidFormatExceptionHandler]", ex);
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
            return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数类型错误:%s", invalidFormatException.getValue()));
        }
        if (StrUtil.startWith(ex.getMessage(), "Required request body is missing")) {
            return CommonResult.error(BAD_REQUEST.getCode(), "请求参数类型错误: request body 缺失");
        }
        return defaultExceptionHandler(ServletUtils.getRequest(), ex);
    }

    /**
     * 处理 Validator 校验不通过产生的异常
     *
     * 例如: @RequestParam @NotBlank校验异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public CommonResult<?> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数不正确:%s", constraintViolation.getMessage()));
    }
    /**
     * 处理业务异常 ServiceException
     *
     * 例如说，商品库存不足，用户手机号已存在。
     */
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<?> serviceExceptionHandler(ServiceException ex) {
        // 不包含的时候，才进行打印(排除频繁且无用的报错)，避免 ex 堆栈过多
        if (!IGNORE_ERROR_MESSAGES.contains(ex.getMessage())) {
            // 即使打印，也只打印第一层 StackTraceElement，并且使用 warn 在控制台输出，更容易看到
            try {
                StackTraceElement[] stackTraces = ex.getStackTrace();
                for (StackTraceElement stackTrace : stackTraces) {
                    //排除工具类的报错(一般报错是先工具类->业务代码)
                    if (ObjUtil.notEqual(stackTrace.getClassName(), ServiceExceptionUtil.class.getName())) {
                        log.warn("[serviceExceptionHandler]\n\t{}", stackTrace);
                        break;
                    }
                }
            } catch (Exception ignored) {
                // 忽略日志，避免影响主流程
            }
        }
        return CommonResult.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理系统异常，兜底处理所有的一切
     * todo: 完善日志
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResult<?> defaultExceptionHandler(HttpServletRequest req, Throwable ex) {
        // 特殊：如果是 ServiceException 的异常，则直接返回
        if (ex.getCause() != null && ex.getCause() instanceof ServiceException) {
            return serviceExceptionHandler((ServiceException) ex.getCause());
        }

        // 情况一：处理表不存在的异常
        CommonResult<?> tableNotExistsResult = handleTableNotExists(ex);
        if (tableNotExistsResult != null) {
            return tableNotExistsResult;
        }

        // 情况二：处理异常
        log.error("[defaultExceptionHandler]", ex);
        // 插入异常日志
//        createExceptionLog(req, ex);
        // 返回 ERROR CommonResult
        return CommonResult.error(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMsg());
    }
    /**
     * 处理 Table 不存在的异常情况
     *todo: 完善其他Table异常
     *
     * @param ex 异常
     * @return 如果是 Table 不存在的异常，则返回对应的 CommonResult
     */
    private CommonResult<?> handleTableNotExists(Throwable ex) {
        String message = ExceptionUtil.getRootCauseMessage(ex);
        if (!message.contains("doesn't exist")) {
            return null;
        }

        // 7. 支付平台
        if (message.contains("pay_")) {
            log.error("[支付模块 yudao-module-pay - 表结构未导入][参考 https://cloud.iocoder.cn/pay/build/ 开启]");
            return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                    "[支付模块 yudao-module-pay - 表结构未导入][参考 https://cloud.iocoder.cn/pay/build/ 开启]");
        }

        return null;
    }
}
