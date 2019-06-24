package com.ippse.mblog.api.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ippse.mblog.lib.client.OkFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MediaFeignService {

	@Autowired
	protected MediaFeignClient mediaFeignClient;

	public OkFile upload(MultipartFile file) {
		return mediaFeignClient.upload(file);
	}

}
