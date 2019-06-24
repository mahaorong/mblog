package com.ippse.mblog.lib.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ippse.mblog.lib.client.OkFile;
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
@EqualsAndHashCode(exclude = {"photos","postlike","reply"})
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Entity
@Table(name = "post")
@FilterDef(name = "postlike", parameters = {@ParamDef(name = "userid", type = "string")})
public class Post implements Serializable {
	private static final long serialVersionUID = -2420589926684719191L;

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	private String id;

	@Column(name = "pid", length = 32)
	private String pid;// 连发N条，下一条的pid写上一条的id；
	
	@Column(name = "forward", length = 32)
	private String forward;//转发；mpost字段forward为被转发的id
	
	@Column(name = "reply", length = 32)
	private String reply;//回复就是评论；mpost字段reply为被回复的id
	
	@Column(name = "personal", length = 32)
	private String personal;//私信，mpost字段personal为接收者的userid

	@Lob
	@Column(name = "refurl", length = 65535)
	private String refurl;// 引用网址

	@Column(nullable = false)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String content;// 消息内容

	@Type(type = "json")
	@Column(columnDefinition = "json")
	private Set<OkFile> photos = new HashSet<>();// 照片或视频列表，最多四张

	private String emoji;// 表情图

	private Double longitude; // 经度
	private Double latitude; // 纬度
	private Double altitude; // 海拔，高度
	private Double speed; // 速度 m/s
	private Double course; // 航向 、路径 取值为：0.0 ~ 359.9 真北方向表示：0.0
	private Double floor; // 显示楼层的信息，如果当地支持的话
	private Integer level; // CLFloor中的一个属性，显示低第几层楼

	@Column(name = "userid", nullable = false, length = 32)
	private String userid;// 发布者id
	@Transient
	private User user;// 发布者

	private Integer comments_count;// 评论数
	private Integer repost_count;// 转发数
	private Integer like_count;// 喜欢数
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="post")
	@Filter(name = "postlike", condition = "userid=:userid")
	private Set<Postlike> postlike = new HashSet<Postlike>(0);

	@Column(name = "pubtime", length = 19)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
	private Timestamp pubtime;// 正式发布时间

	@Column(nullable = false)
	private String status;// 状态
	
	@Column(name = "ctime", nullable = false, length = 19)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
	private Timestamp ctime;// 保存时间
	
	public void addPhoto(OkFile file) {
		this.photos.add(file);
	}

}
