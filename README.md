# access
基于springboot 2.x 开发的 权限校验框架

#引入依赖


#使用方式

`
@Configuration
public class AutoAccessServletConfiguration {
	
	private static AccessSessionConfig accessSessionConfig = new AccessSessionConfig();
	
	public AutoAccessServletConfiguration (@Qualifier ApplicationContext applicationContext) {
		accessSessionConfig.setApplicationContext(applicationContext);
	}
	
	static {
		AccessProperties accessProperties = new AccessProperties();
		accessProperties.setSuccessUrl("test/hello");
		//
		accessSessionConfig.setCache(AccessCacheFactory.createDefaultCache().getInstance());
		accessSessionConfig.setAccessProperties(accessProperties);
		accessSessionConfig.setAccessCustomHandle(new AccessCustomHandleImpl());
	}
	
	@Bean
	public AccessHandleImpl accessHandleImpl() {
		return new AccessHandleImpl();
	}
	
	@Bean
	public AccessDefaultPointcutAdvisor accessDefaultPointcutAdvisor() {
		return new AccessDefaultPointcutAdvisor();
	}
	
	@Bean
	public AccessServletConfiguration accessServletConfiguration() {
		return AccessServletConfiguration.create(accessSessionConfig, this.accessDefaultPointcutAdvisor(), this.accessHandleImpl());
	}
}
`
