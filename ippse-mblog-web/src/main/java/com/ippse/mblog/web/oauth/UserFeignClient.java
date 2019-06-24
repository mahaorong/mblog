package com.ippse.mblog.web.oauth;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ippse.mblog.lib.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@FeignClient(value = "ippse-user-api"/*, fallback = UserFeignClientFallback.class*/)
public interface UserFeignClient {
	
	//获取id查询用户
	@RequestMapping(value = "/api/public/user/{userid}", method = RequestMethod.GET)
	public User findPublicById(@PathVariable("userid") String userid);
	
	//获取id查询用户
	@RequestMapping(value = "/api/user/{userid}", method = RequestMethod.GET)
	public User findById(@PathVariable("userid") String userid);
	
	//查询关注
	@RequestMapping(value ="/api/follow/judge", method = RequestMethod.POST)
    public String judge(@RequestParam(value = "followers") String followers);
	
	//关注
	@RequestMapping(value ="/api/follow/{followers}", method = RequestMethod.POST)
	public OkUser follow(@PathVariable(value = "followers") String followers);
	
	//取消关注
	@RequestMapping(value ="/api/unfollow/{followers}", method = RequestMethod.POST)
	public OkUser removefollow(@PathVariable(value = "followers") String followers);
	
	//更换头像
	@RequestMapping(value ="/api/u/update/userimage", method = RequestMethod.POST)
	public OkUser updateimage(@RequestParam(value = "image") String image);
	
	//更换头像
	@RequestMapping(value ="/api/friends/{fid}", method = RequestMethod.POST)
	public void addfriends(@PathVariable(value = "fid") String fid,
			@RequestParam(value = "confirm", defaultValue = "no", required = false) String confirm,
			@RequestParam(value = "ps", defaultValue = "1", required = false) String ps,
			@RequestParam(value = "tags", defaultValue = "", required = false) Set<String> tags);

	@RequestMapping(value ="/api/user/username/{username}", method = RequestMethod.GET)
	public OkUser findByUsername(@RequestParam(value = "username") String username);

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class UserParam {
	String id;
	String username;
}
/*@Component
class UserFeignClientFallback implements UserFeignClient {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public User findById(String userid) {
		log.info("failed......." + userid);
		return new User();
	}

	@Override
	public User findByUsername(UserParam userParam) {
		log.info("failed......." + userParam.toString());
		return new User();
	}

}*/
