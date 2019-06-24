package com.ippse.mblog.lib.client;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OkFile implements java.io.Serializable {
	private static final long serialVersionUID = -6801344966724571715L;

	private String id;
	private String userid;
	private String userimage;
	private String username;
	private String pid;

	private String url;
	private String type;
	private String extname;
	private Integer size;
	private String name;
	private String status;
	private String address;
	private Double latitude;
	private Double longitude;
	private Timestamp ctime;

	private String cdate;

	public String getCdate() {
		this.cdate = new SimpleDateFormat("yyyy-MM-dd").format(ctime);
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public boolean equal(String date) {
		String c = new SimpleDateFormat("yyyy-MM-dd").format(ctime);
		if (StringUtils.equals(c, date)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean notEqual(String date) {
		return !equal(date);
	}

}