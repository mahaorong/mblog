package com.ippse.mblog.web.controller;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostModel {

	@NotNull(message = "content.notnull")
	@Size(min = 3, max = 60, message = "content.size.error")
	private String content;// 消息内容
	private MultipartFile[] file;

}
