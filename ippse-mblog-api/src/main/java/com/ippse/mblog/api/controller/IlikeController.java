package com.ippse.mblog.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ippse.mblog.api.oauth.UserFeignService;
import com.ippse.mblog.lib.dao.PostLikeRepository;
import com.ippse.mblog.lib.dao.PostRepository;
import com.ippse.mblog.lib.model.Post;
import com.ippse.mblog.lib.model.Postlike;
import com.ippse.mblog.lib.model.User;
import com.ippse.mblog.lib.services.ServiceException;
import com.ippse.mblog.lib.utils.ID;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/ilike")
@Api(tags = { "喜欢" })
public class IlikeController extends BaseApiController{
	@Autowired
	private PostLikeRepository postLikeRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserFeignService userFeignService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void ilike(@ModelAttribute IlikeModel model) {
		Postlike postlike = new Postlike();
		User user = userFeignService.findById(sessuserid);
		Post post = new Post();
		post.setId(model.getPostid());
		postlike.setId(ID.uuid());
		postlike.setPost(post);
		postlike.setUserid(sessuserid);
		postlike.setUser(user);
		
		postLikeRepository.save(postlike);
		postRepository.updateLikeCount(model.getPostid());
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable String id) {
		postLikeRepository.deleteByPostId(id, sessuserid);
		postRepository.updateLikeCount(id);
		return "succsses";
	}
}
