package com.ippse.mblog.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

import com.ippse.mblog.lib.services.ServiceException;

@ControllerAdvice
public class GlobalAdvice {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalAdvice.class);

	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String notFoundView() {
		return "error";
	}
	
	@ResponseBody
	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> businessExceptionHandler(ServiceException be) {
		LOGGER.error(be.getMessage(), be);

		final Locale locale = LocaleContextHolder.getLocale();
		final String message = messageSource.getMessage(be.getMsg(), be.getParams(), locale);
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = -2147774529887359231L;
			{
				put(be.getField(), message);
			}
		};
	}

	@ExceptionHandler(MultipartException.class)
	@ResponseBody
	public ServiceException handleControllerException(HttpServletRequest request, Throwable ex) {
		HttpStatus status = getStatus(request);
		return new ServiceException("", ex.getMessage(), status);
	}

	/**
	 * bean校验未通过异常
	 * 
	 * @see javax.validation.Valid
	 * @see org.springframework.validation.Validator
	 * @see org.springframework.validation.DataBinder
	 */
	@ResponseBody
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> validExceptionHandler(BindException ex) {
		final Locale locale = LocaleContextHolder.getLocale();
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = -3222861572243435035L;
			{
				for (FieldError error : fieldErrors) {
					Object value = ex.getFieldValue(error.getField());
					String message = messageSource.getMessage(error.getDefaultMessage(), new Object[] { value },
							locale);
					put(error.getField(), message);
				}
			}
		};
	}
	
	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> validMethodArgumentHandler(MethodArgumentNotValidException ex) {
		final Locale locale = LocaleContextHolder.getLocale();
		List<ObjectError> fieldErrors = ex.getBindingResult().getAllErrors();
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = -3222861572243435035L;
			{
				for (ObjectError e : fieldErrors) {
					FieldError error = (FieldError)e;
					String message = messageSource.getMessage(error.getDefaultMessage(), null,
							locale);
					put(error.getField(), message);
				}
			}
		};
	}

	@ResponseBody
	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> nullPointerExceptionHandler(NullPointerException re) {
		return logAndWrapMessage("System data exception.", re);
	}

	@ResponseBody
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> runtimeExceptionHandler(RuntimeException re) {
		return logAndWrapMessage(re.getMessage(), re);
	}

	@ResponseBody
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> throwableHandler(Throwable t) {
		return logAndWrapMessage(t.getMessage(), t);
	}

	private Map<String, Object> logAndWrapMessage(String message, Throwable t) {
		long timestamp = System.currentTimeMillis();
		LOGGER.error("Timestamp: {}, Message: {}", timestamp, message, t);
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = -8863817677230234970L;
			{
				put("timestamp", timestamp);
				put("message", message);
			}
		};
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}
}
