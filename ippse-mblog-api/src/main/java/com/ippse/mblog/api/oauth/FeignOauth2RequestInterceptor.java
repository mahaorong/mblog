package com.ippse.mblog.api.oauth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FeignOauth2RequestInterceptor implements RequestInterceptor {
	@Autowired
	private OAuth2AuthorizedClientService clientService;

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate requestTemplate) {
    	log.info("---------FeignOauth2RequestInterceptor-----------");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        
        if (authentication != null && authentication.getDetails() != null) {
			Object details = authentication.getDetails();
			//sessusername = authentication.getName();
			if ( details instanceof OAuth2AuthenticationDetails ){
			    OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails)details;
			    log.info(oAuth2AuthenticationDetails.getTokenValue());
			    String accessToken = oAuth2AuthenticationDetails.getTokenValue();
			    if (StringUtils.isNotBlank(accessToken)) {
			    	log.info("accessToken--------------------"+accessToken);
		            requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, accessToken));
			    }
			}
		}
        
        //这一段在资源服务器中无效，https://github.com/spring-cloud/spring-cloud-security/issues/132
        if (authentication instanceof OAuth2AuthenticationToken) {
        	OAuth2AuthenticationToken oauthToken = ((OAuth2AuthenticationToken)authentication);
        	OAuth2AuthorizedClient client =
				    clientService.loadAuthorizedClient(
				            oauthToken.getAuthorizedClientRegistrationId(),
				            oauthToken.getName());
        	if (null != client) {// 重启后授权的客户端信息全部没有了，因授权客户端仓库默认是在内存中。
				String accessToken = client.getAccessToken().getTokenValue();
				log.info(accessToken);
				log.info("--------FeignOauth2RequestInterceptor-------client-----");
	            requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, accessToken));
        	}
        	System.out.println(client);
        }

    }
}
