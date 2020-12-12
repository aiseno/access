package com.github.aiseno.access.listener.handle;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aiseno.access.consts.AccessConst.AccessServletOptEnums;
import com.github.aiseno.access.listener.handle.events.AccessHandleEvent;
import com.github.aiseno.access.listener.handle.events.AccessHandleEvent.AccessHttpServletObject;
import com.github.aiseno.access.listener.session.impl.AccessLoginImpl;
import com.github.aiseno.access.listener.session.impl.AccessLogoutImpl;

@SuppressWarnings("rawtypes")
public class AccessHandleApplicationListener implements ApplicationListener<AccessHandleEvent> {

	private static Logger logger = LoggerFactory.getLogger(AccessHandleApplicationListener.class);

	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void onApplicationEvent(AccessHandleEvent event) {
		if (event == null || event.getSccessHttpServletObject() == null) {
			logger.error("AccessEvent must not be null.");
			return;
		}
		AccessHttpServletObject eventObject = event.getSccessHttpServletObject();
		AccessServletOptEnums opt = eventObject.getOpt();
		ResponseEntity responseEntity = null;
		switch (opt) {
		case LOGIN:
			responseEntity = AccessLoginImpl.builder(eventObject).doHandle();
			break;
		case LOGOUT:
			responseEntity = AccessLogoutImpl.builder(eventObject).doHandle();
			break;
		default:
			responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body("not support path.");
			logger.error("AbstractAccessListener handle type error. {}", opt);
			break;
		}
		transformResponseEntity(responseEntity, eventObject.getResponse());
	}
	
	protected static void transformResponseEntity(ResponseEntity entity, HttpServletResponse response) {
		if (entity == null) {
			entity = ResponseEntity.ok().build();
		}
		HttpHeaders httpHeaders = entity.getHeaders();
		if (httpHeaders != null && !httpHeaders.isEmpty()) {
			httpHeaders.forEach((k, v) -> {
				response.setHeader(k, v.get(0));
			});
		}
		response.setStatus(entity.getStatusCodeValue());
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			Object body = entity.getBody();
			out.write(mapper.writeValueAsBytes(body == null ? "" : body));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
