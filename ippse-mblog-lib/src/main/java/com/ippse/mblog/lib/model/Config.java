package com.ippse.mblog.lib.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config implements java.io.Serializable {
	private static final long serialVersionUID = 1801219483748426473L;

	@JsonView(User.Views.Profile.class)
	private String property;

	@JsonView(User.Views.Profile.class)
	private Set<String> vals = new HashSet<String>();

	@JsonView(User.Views.Profile.class)
	private String defval;

	public Config(String property, Set<String> vals) {
		this.property = property;
		this.vals = vals;
	}

}