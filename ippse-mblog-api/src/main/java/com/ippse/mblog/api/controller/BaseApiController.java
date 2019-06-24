package com.ippse.mblog.api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ippse.mblog.api.oauth.OkUser;
import com.ippse.mblog.api.oauth.UserFeignService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/")
public abstract class BaseApiController {
	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	protected UserFeignService userFeignService;
	protected String sessuserid = "";
	protected OkUser sessuser;
	protected String accessToken;

	protected String keyword;
	protected int pagesize;
	protected int page;

	@ModelAttribute
	public void getSessionUser(Authentication authentication) {
		if (authentication != null && authentication.getDetails() != null) {
			Object details = authentication.getDetails();
			if ( details instanceof OAuth2AuthenticationDetails ){
			    OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails)details;
			    log.info(oAuth2AuthenticationDetails.getTokenValue());
			    log.info(authentication.getName());
			    Map<String, Object> decodedDetails = (Map<String, Object>)oAuth2AuthenticationDetails.getDecodedDetails();
			    if (null != decodedDetails) {
				    log.info(decodedDetails.toString());
				    log.info("BaseController", "sessuserid: " + decodedDetails.get("uid"));
				    sessuserid = (String)decodedDetails.get("uid");
			    }
			}
		}
	}

	@ModelAttribute
	public void input_init(Model model, @RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "pagesize", defaultValue = "20", required = false) Integer pagesize,
			@RequestParam(value = "page", defaultValue = "1", required = false) Integer page) {
		log.info("BaseController input_init", "sessuserid:" + sessuserid);
		this.keyword = keyword;
		this.pagesize = pagesize;
		this.page = page;
	}

	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
}
