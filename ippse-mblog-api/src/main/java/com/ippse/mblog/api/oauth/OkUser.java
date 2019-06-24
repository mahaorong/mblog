package com.ippse.mblog.api.oauth;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OkUser implements OAuth2User,Serializable {
	private static final long serialVersionUID = 1767052910033405847L;
	private List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
	private Map<String, Object> attributes;

	public static final class Views {
		public interface Public {
		}

		public interface Friend extends Public {
		}

		public interface Profile extends Friend {
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public Map<String, Object> getAttributes() {
		if (this.attributes == null) {
			this.attributes = new HashMap<>();
			this.attributes.put("id", this.getId());
			this.attributes.put("name", this.getName());
			this.attributes.put("username", this.getUsername());
			this.attributes.put("email", this.getEmail());
		}
		return attributes;
	}

	@JsonView(OkUser.Views.Public.class)
	private String id;

	@JsonView(OkUser.Views.Public.class)
	private String username;
	private String password;

	@JsonView(OkUser.Views.Public.class)
	private String firstName;

	@JsonView(OkUser.Views.Public.class)
	private String lastName;

	@JsonView(OkUser.Views.Profile.class)
	private String email;

	@JsonView(OkUser.Views.Profile.class)
	private String code;
	@JsonView(OkUser.Views.Profile.class)
	private String mobile;
	@JsonView(OkUser.Views.Profile.class)
	private Date lastPasswordResetDate;
	@JsonView(OkUser.Views.Profile.class)
	private String type;

	@JsonView(OkUser.Views.Public.class)
	private String status;
	@JsonView(OkUser.Views.Profile.class)
	private String certcode;

	@JsonView(OkUser.Views.Public.class)
	private Integer rank;
	@JsonView(OkUser.Views.Public.class)
	private Integer follow;
	@JsonView(OkUser.Views.Public.class)
	private Integer fans;
	@JsonView(OkUser.Views.Public.class)
	private Integer posts;

	@JsonView(OkUser.Views.Public.class)
	private String name;
	@JsonView(OkUser.Views.Public.class)
	private String image;
	@JsonView(OkUser.Views.Public.class)
	private String nickname;

	@JsonView(OkUser.Views.Profile.class)
	private String homepage;
	@JsonView(OkUser.Views.Profile.class)
	private String intro;
	@JsonView(OkUser.Views.Profile.class)
	private String sex;
	@JsonView(OkUser.Views.Profile.class)
	private Date birthday;
	@JsonView(OkUser.Views.Profile.class)
	private String marriage;
	@JsonView(OkUser.Views.Profile.class)
	private String bloodtype;
	@JsonView(OkUser.Views.Profile.class)
	private String hometown;
	@JsonView(OkUser.Views.Profile.class)
	private String edulevel;
	@JsonView(OkUser.Views.Profile.class)
	private String religion;
	@JsonView(OkUser.Views.Profile.class)
	private String hobby;

	@JsonView(OkUser.Views.Public.class)
	private String signature;

	@JsonView(OkUser.Views.Profile.class)
	private Timestamp ctime;

	@JsonView(OkUser.Views.Profile.class)
	private Set<String> roles = new HashSet<String>();

	@JsonView(OkUser.Views.Profile.class)
	private Set<OkUserConfig> configs = new HashSet<OkUserConfig>(0);

	// 不用于json输出，仅业务层需要
	public Set<String> getConfigValue(String property) {
		for (OkUserConfig config : this.getConfigs()) {
			if (StringUtils.equals(config.getProperty(), property))
				return config.getVals();
		} // TODO 增加默认值
		return new HashSet<String>();
	}

}
