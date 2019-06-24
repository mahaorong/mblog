package com.ippse.mblog.lib.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = { "post" })
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Entity
@Table(name = "postlike")
public class Postlike implements Serializable {
	private static final long serialVersionUID = 4527394431259161002L;
	
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	private String id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postid")
	private Post post;// 贴子id；
	
	@Column(name = "userid", nullable = false, length = 32)
	private String userid;// 发布者id
	@Transient
	private User user;// 发布者
	
}
