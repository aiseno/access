# access
基于springboot 2.x 开发的 权限校验框架

#引入依赖


#使用方式

```java
@Configuration
public class AisenoAccessConfig {
	
	private static AccessSessionConfig accessSessionConfig = new AccessSessionConfig();

	public AisenoAccessConfig(@Qualifier ApplicationContext applicationContext) {
		accessSessionConfig.setApplicationContext(applicationContext);
	}
        
	static {
	        //Api开放忽略链接
	        List<String> ignoreUris = new ArrayList<>();
		ignoreUris.add("/doc*");
		ignoreUris.add("/webjars/**");
		ignoreUris.add("/swagger-resources**");
		ignoreUris.add("/v3/api-docs**");
		//验证模式
		AccessModeProperties modeProperties = new AccessModeProperties();
		modeProperties.setMode(AccessMode.TOKEN);
		modeProperties.setMultipleLogin(true);
		modeProperties.setTokenAgent(DefaultHeadersConst.HeaderAccess.ACCESS_AGENT.getCode());
		modeProperties.setTokenName(DefaultHeadersConst.HeaderAccess.ACCESS_TOKEN.getCode());
		modeProperties.setTokenPrefix(DefaultHeadersConst.HeaderAgents.getCodeArray());
		//配置信息
		AccessProperties accessProperties = new AccessProperties();
		accessProperties.setSuccessUrl("/access/index/v1");
		//忽略地址
		accessProperties.setIgnoreUris(ignoreUris);
		accessProperties.setModeProperties(modeProperties);
		//校验地址
		//accessProperties.setValidUris(validUris);
		
		accessSessionConfig.setMaxInactiveInterval(600); //十分钟
		accessSessionConfig.setCache(AccessCacheFactory.createDefaultCache().getInstance());
		accessSessionConfig.setAccessProperties(accessProperties);
		accessSessionConfig.setAccessCustomHandle(new CustomAccessHandleImpl());
	}

	/**
	 * -登录实现类
	 * @return
	 */
	@Bean
	public LoginAccessHandleImpl loginAccessHandleImpl() {
		return new LoginAccessHandleImpl();
	}
	
	@Bean
	public AccessDefaultPointcutAdvisor accessDefaultPointcutAdvisor() {
		return new AccessDefaultPointcutAdvisor();
	}

	@Bean
	public AccessServletConfiguration accessServletConfiguration() {
		return AccessServletConfiguration.create(accessSessionConfig, accessDefaultPointcutAdvisor(),loginAccessHandleImpl());
	}
}
```
