package com.ippse.mblog.web.oauth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ippse.mblog.lib.client.OkFile;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

@FeignClient(name = "kxmedia-api", configuration = MediaFeignClient.MultipartSupportConfig.class)
public interface MediaFeignClient {
	
	@PostMapping(value = "/api/photo/public", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public OkFile upload(@RequestPart(value = "file") MultipartFile file);

	@Configuration
	public class MultipartSupportConfig {

		/*@Autowired
		private ObjectFactory<HttpMessageConverters> messageConverters;

		@Bean
		public Encoder feignFormEncoder() {
			return new SpringFormEncoder(new SpringEncoder(messageConverters));
		}*/
		
		@Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }

		@Bean
		public feign.Logger.Level multipartLoggerLevel() {
			return feign.Logger.Level.FULL;
		}

	}

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