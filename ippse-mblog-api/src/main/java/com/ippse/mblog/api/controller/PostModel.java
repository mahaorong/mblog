package com.ippse.mblog.api.controller;

import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostModel {

	@Size(min = 2, max = 280)
	private String content;// 消息内容
	
	private MultipartFile[] file;

}
