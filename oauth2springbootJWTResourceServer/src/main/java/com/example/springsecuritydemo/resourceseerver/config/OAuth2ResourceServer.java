package com.example.springsecuritydemo.resourceseerver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {

	/*
	 * @Override public void configure(ResourceServerSecurityConfigurer resources) {
	 * resources .resourceId(RESOURCE_ID) .tokenStore(tokenStore); }
	 */
	
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		/*
		 * http.authorizeRequests().antMatchers("/api/**").authenticated().antMatchers(
		 * "/").permitAll();
		 */
		 
		http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated();
	}
	@Bean
    public ResourceServerTokenServices tokenService() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setClientId("clientapp");
        tokenServices.setClientSecret("123456");
        tokenServices.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
        return tokenServices;
    }
	
}

