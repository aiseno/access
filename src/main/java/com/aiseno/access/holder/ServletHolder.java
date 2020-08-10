package com.aiseno.access.holder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServletHolder {
	
	private static Logger logger = LoggerFactory.getLogger(ServletHolder.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
    public static Map<String, Object> getParameter(HttpServletRequest request) {
		return new ParamBuilder(request).getParameter();
    }
    
    public static String generatorTokenId() {
  		return UUID.randomUUID().toString().replace("-", "");
  	}
    
    public static void registerBean(ConfigurableApplicationContext context ,String beanName , Object bean) {
		final Map<String, ? extends Object> map = context.getBeansOfType(bean.getClass());
		if(map == null || map.isEmpty()) {
			ConfigurableListableBeanFactory factory = context.getBeanFactory();
			factory.initializeBean(bean, beanName);
			factory.registerSingleton(beanName,bean);
			factory.autowireBean(bean);
			factory.resolveNamedBean(bean.getClass());
		}
	}
    
    /**
     * - 建造器
     * @author VULCAN
     */
    @SuppressWarnings("unchecked")
    private static class ParamBuilder {
    	
    	private HttpServletRequest request;
    	
    	private ParamBuilder(HttpServletRequest request) {
    		this.request = request;
    	}
    	
		public Map<String, Object> getParameter() {
            try {
            	String mehtod = request.getMethod();
                if ("GET".equals(mehtod.toUpperCase())) {
                	return getQueryParams();
                } 
                Map<String, String[]> params = request.getParameterMap();
                return (params != null && params.size() > 0) ? objectMapper.readValue(objectMapper.writeValueAsBytes(params), Map.class) : getReaderParams(); 
    		} catch (Exception e) {
    			logger.error("get request 参数异常,{}" , e.getMessage());
    			return null;
    		}
        }
		private Map<String, Object> getReaderParams() {
    		try {
    			if(null == request.getInputStream()) {
    			   return null;
            	}
    			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
    			StringBuffer sb = new StringBuffer();
    			String s = null;
    			while ((s = br.readLine()) != null) {
    				sb.append(s);
    			}
    			return objectMapper.readValue(sb.toString(), Map.class);
    		} catch (IOException e) {
    			logger.error("获取request 参数异常,{}" , e.getMessage());
    		}
    		return null;
    	}
        private Map<String, Object> getQueryParams() throws UnsupportedEncodingException {
        	if(StringUtils.isEmpty(request.getQueryString())) {
        		return null;
        	}
        	final String input = URLDecoder.decode(request.getQueryString(), "UTF-8");
        	String[] ps = input.split("&");
    		Map<String,Object> paramMap = new HashMap<>();
    		for(String p : ps) {
    			String[] mm = p.split("=");
    			if(mm.length <= 1) {
    			   paramMap.put(mm[0],null);
    			   continue;
    			}
    			paramMap.put(mm[0], mm[1]);
    		}
    		return paramMap;
        }
    }
}
