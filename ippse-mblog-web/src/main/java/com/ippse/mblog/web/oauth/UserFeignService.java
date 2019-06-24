package com.ippse.mblog.web.oauth;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ippse.mblog.lib.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserFeignService {

	@Autowired
	protected UserFeignClient userFeignClient;
	
	public User findPublicById(String userid) {
		log.info("UserFeignService findPublicById:" + userid);
		User user = new User();
		try {
			user = userFeignClient.findPublicById(userid);
			log.info("UserFeignService user:" + user.toString());
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return user;
	}

	public User findById(String userid) {
		log.info("UserFeignService findById:" + userid);
		User user = new User();
		try {
			user = userFeignClient.findById(userid);
			log.info("UserFeignService user:" + user.toString());
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return user;
	}

	public OkUser follow(@PathVariable String followers) {
		OkUser user = userFeignClient.follow(followers);
		return user;
	}

	public OkUser removefollow(@PathVariable String followers) {
		OkUser user = userFeignClient.removefollow(followers);
		return user;
	}

	public String judge(@RequestParam String followers) {
		String follow = userFeignClient.judge(followers);
		return follow;
	}

	public void updateimage(@RequestParam String image) {
		userFeignClient.updateimage(image);
	}

	public OkUser findByUsername(@RequestParam String username) {
		log.info("......." + username);
		OkUser user = new OkUser();
		try {
			user = userFeignClient.findByUsername(username);
			log.info(user.toString());
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return user;
	}

	public void addfriends(@PathVariable String fid,
			@RequestParam(value = "confirm", defaultValue = "no", required = false) String confirm,
			@RequestParam(value = "ps", defaultValue = "1", required = false) String ps,
			@RequestParam(value = "tags", defaultValue = "", required = false) Set<String> tags) {
		userFeignClient.addfriends(fid, confirm, ps, tags);
	}
}
