package com.ippse.mblog.lib.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements java.io.Serializable {
	private static final long serialVersionUID = -3936141646843762694L;

	public static final class Views {
		public interface Public {
		}

		public interface Friend extends Public {
		}

		public interface Profile extends Friend {
		}
	}

	@JsonView(User.Views.Public.class)
	private String id;

	@JsonView(User.Views.Public.class)
	@NotNull(message = "user.account.notnull")
	@Size(min = 3, max = 30, message = "user.account.size")
	@Pattern(regexp = "[a-zA-Z0-9_@]{3,30}.[a-zA-Z0-9]{1,}", message = "user.account.illegal")
	private String username;

	@Size(min = 60, max = 60)
	private String password;

	@JsonView(User.Views.Public.class)
	@Size(max = 50)
	private String firstName;

	@JsonView(User.Views.Public.class)
	@Size(max = 50)
	private String lastName;

	@JsonView(User.Views.Profile.class)
	@Email
	@Size(min = 5, max = 100)
	private String email;

	@JsonView(User.Views.Profile.class)
	private String code;// 个人为身份证号码，学校或企业为执照号码
	@JsonView(User.Views.Profile.class)
	private String mobile;
	@JsonView(User.Views.Profile.class)
	private Date lastPasswordResetDate;
	@JsonView(User.Views.Profile.class)
	private String type;

	@JsonView(User.Views.Public.class)
	private String status;
	@JsonView(User.Views.Profile.class)
	private String certcode;

	@JsonView(User.Views.Public.class)
	private Integer rank;
	@JsonView(User.Views.Public.class)
	private Integer follow;
	@JsonView(User.Views.Public.class)
	private Integer fans;
	@JsonView(User.Views.Public.class)
	private Integer posts;

	@JsonView(User.Views.Public.class)
	private String name;
	@JsonView(User.Views.Public.class)
	private String image;
	@JsonView(User.Views.Public.class)
	private String nickname;

	@JsonView(User.Views.Profile.class)
	private String homepage;
	@JsonView(User.Views.Profile.class)
	private String intro;
	@JsonView(User.Views.Profile.class)
	private String sex;
	@JsonView(User.Views.Profile.class)
	private Date birthday;
	@JsonView(User.Views.Profile.class)
	private String marriage;
	@JsonView(User.Views.Profile.class)
	private String bloodtype;
	@JsonView(User.Views.Profile.class)
	private String hometown;
	@JsonView(User.Views.Profile.class)
	private String edulevel;
	@JsonView(User.Views.Profile.class)
	private String religion;
	@JsonView(User.Views.Profile.class)
	private String hobby;

	@JsonView(User.Views.Public.class)
	private String signature;

	@JsonView(User.Views.Profile.class)
	private Timestamp ctime;

	@JsonView(User.Views.Profile.class)
	private Set<String> roles = new HashSet<String>();

	@JsonView(User.Views.Profile.class)
	private Set<Config> configs = new HashSet<Config>(0);

	// 不用于json输出，仅业务层需要
	public Set<String> getConfigValue(String property) {
		for (Config config : this.getConfigs()) {
			if (StringUtils.equals(config.getProperty(), property))
				return config.getVals();
		} // TODO 增加默认值
		return new HashSet<String>();
	}

	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}