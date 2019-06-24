package com.ippse.mblog.web.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ippse.mblog.web.oauth.OkUser;
//import com.openkx.article.client.UserFeignService;
import com.ippse.mblog.web.oauth.UserFeignService;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseController {
	// @Autowired
	// private TokenStore tokenStore;
	@Autowired
	private OAuth2AuthorizedClientService clientService;

	@Autowired
	protected UserFeignService userFeignService;
	protected String sessuserid = "";
	protected OkUser sessuser;
	protected String accessToken;

	@ModelAttribute
	public void getSessionUser(Model model, Principal principal, Authentication authentication) {
		log.info("BaseController getSessionUser");

		if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;

			OAuth2AuthorizedClient client = clientService
					.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
			if (null != client) {// 重启后授权的客户端信息全部没有了，因授权客户端仓库默认是在内存中。
				accessToken = client.getAccessToken().getTokenValue();
				log.info(accessToken);
				log.info(oauthToken.getPrincipal().toString());
				sessuser = (OkUser) oauthToken.getPrincipal();
				model.addAttribute("sessuser", sessuser);
			}
		}
		if (null == sessuser) {
			log.warn("sessuserid is null in BaseController getSessionUser()");
			sessuser = new OkUser();
			sessuser.setName("请登录");
			sessuser.setImage("");
		} else {
			sessuserid = sessuser.getId();
			log.info("BaseController getSessionUser sessuserid:" + sessuserid);
			try {
				// sessuser =
				// userFeignService.findByUsername(sessuser.getUsername());
				log.info("========" + sessuser.toString());
			} catch (FeignException e) {
				e.printStackTrace();
			}
		}
	}
}
