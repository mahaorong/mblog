package com.ippse.mblog.api.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ippse.mblog.api.oauth.MediaFeignService;
import com.ippse.mblog.api.oauth.UserFeignService;
import com.ippse.mblog.lib.client.OkFile;
import com.ippse.mblog.lib.dao.PostDao;
import com.ippse.mblog.lib.dao.PostRepository;
import com.ippse.mblog.lib.model.Post;
import com.ippse.mblog.lib.model.User;
import com.ippse.mblog.lib.services.ServiceException;
import com.ippse.mblog.lib.utils.ID;
import com.ippse.mblog.lib.utils.Time;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/post")
@Api(tags = { "贴子" })
public class PostController extends BaseApiController {
	@PersistenceContext 
    private EntityManager entityManager;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostDao postDao;
	@Autowired
	private MediaFeignService mediaFeignService;
	@Autowired
	private UserFeignService userFeignService;

	@ApiOperation(value = "findAll", notes = "分页查询所有贴子", response = Page.class)
	@GetMapping
	public Page<Post> findAll(
			@SortDefault(sort = "pubtime", direction = Sort.Direction.DESC) @PageableDefault(value = 10,page=1) Pageable pageable) {
		Page<Post> pages = postDao.findAll(sessuserid,pageable);
		int totalElements = (int) pages.getTotalElements();
		pages = new PageImpl<Post>(pages.stream().map(post -> {
			User user = userFeignService.findPublicById(post.getUserid());
			System.out.println(user.toString());
			post.setUser(user);
			return post;
		}).collect(Collectors.toList()), pageable, totalElements);
		return pages;
	}
	
	@ApiOperation(value = "findReply", notes = "分页查询回复的贴子", response = Page.class)
	@GetMapping("/{id}/reply")
	public Page<Post> findReply(@PathVariable String id, 
			@SortDefault(sort = "pubtime", direction = Sort.Direction.DESC) @PageableDefault(value = 10) Pageable pageable) {
		Page<Post> pages = postRepository.findByReply(id, pageable);
		int totalElements = (int) pages.getTotalElements();
		pages = new PageImpl<Post>(pages.stream().map(post -> {
			User user = userFeignService.findPublicById(post.getUserid());
			System.out.println(user.toString());
			post.setUser(user);
			return post;
		}).collect(Collectors.toList()), pageable, totalElements);
		return pages;
	}

	@ApiOperation(value = "findOne", notes = "查询贴子", response = Post.class)
	@GetMapping("/{id}")
	public Post findOne(@PathVariable String id) {
		return postRepository.findById(id).orElseThrow(() -> new ServiceException("post_not_exist"));
	}

	@ApiOperation(value = "create", notes = "发布贴子", response = Post.class)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Post create(@ModelAttribute PostModel model) {
		Post post = new Post();
		post.setId(ID.uuid());
		post.setUserid(sessuserid);
		post.setCtime(Time.timestamp());
		post.setComments_count(0);
		post.setLike_count(0);
		post.setRepost_count(0);
		post.setPubtime(Time.timestamp());
		post.setStatus("normal");
		post.setContent(model.getContent());

		if (null != model.getFile() && model.getFile().length > 0) {
			try {
				for (MultipartFile uploadedFile : model.getFile()) {
					OkFile f = mediaFeignService.upload(uploadedFile);
					log.info(f.toString());
					post.addPhoto(f);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return postRepository.save(post);
	}
	
	@ApiOperation(value = "reply", notes = "回复贴子", response = Post.class)
	@PostMapping("/{id}/reply")
	@ResponseStatus(HttpStatus.CREATED)
	public Post reply(@PathVariable String id, @ModelAttribute PostModel model) {
		postRepository.findById(id).orElseThrow(() -> new ServiceException("post_not_exist"));
		Post post = new Post();
		post.setId(ID.uuid());
		post.setReply(id);
		post.setUserid(sessuserid);
		post.setCtime(Time.timestamp());
		post.setComments_count(0);
		post.setLike_count(0);
		post.setRepost_count(0);
		post.setPubtime(Time.timestamp());
		post.setStatus("normal");
		post.setContent(model.getContent());

		if (null != model.getFile() && model.getFile().length > 0) {
			try {
				for (MultipartFile uploadedFile : model.getFile()) {
					OkFile f = mediaFeignService.upload(uploadedFile);
					log.info(f.toString());
					post.addPhoto(f);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		postRepository.save(post);
		postRepository.updateCommentCount(id);
		return post;
	}

	@ApiOperation(value = "delete", notes = "删除贴子")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		postRepository.findById(id).orElseThrow(() -> new ServiceException(""));
		postRepository.deleteById(id);
	}

	@ApiOperation(value = "update", notes = "修改贴子内容", response = Post.class)
	@PutMapping("/{id}")
	public Post update(@ModelAttribute PostModel model, @PathVariable String id) {
		Optional<Post> op_post = postRepository.findById(id);
		if (op_post.isPresent()) {
			Post post = op_post.get();
			if (!StringUtils.equals(post.getStatus(), "draft")) {// 只有为草稿状态的贴子才能修改
				throw new ServiceException("only_draft_can_be_modified");
			}
			post.setPubtime(Time.timestamp());
			post.setContent(model.getContent());
			try {
				if (model.getFile().length > 0) {
					post.setPhotos(new HashSet<>());
					for (MultipartFile uploadedFile : model.getFile()) {
						OkFile f = mediaFeignService.upload(uploadedFile);
						log.info(f.toString());
						post.addPhoto(f);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return postRepository.save(post);
		} else {
			throw new ServiceException("post_not_exist");
		}
	}

}
