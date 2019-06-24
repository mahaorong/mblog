package com.ippse.mblog.web.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ippse.mblog.lib.dao.PostRepository;
import com.ippse.mblog.lib.model.Post;
import com.ippse.mblog.lib.model.User;
import com.ippse.mblog.web.oauth.UserFeignService;

@Controller
public class IndexController extends BaseController {
	@Autowired
	private PostRepository mpostRepository;
	@Autowired
	private UserFeignService userFeignService;

	@GetMapping("/")
	public String findAllDemo(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(10);
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Direction.DESC, "ctime");
		Page<Post> pages = mpostRepository.findAll(pageable);

		/*pages.getContent().stream().map(post -> {
			User user = userFeignService.findById(post.getUserid());
			System.out.println(user.toString());
			post.setUser(user);
			return post;
		}).collect(Collectors.toList());*/

		int totalElements = (int) pages.getTotalElements();
		pages = new PageImpl<Post>(pages.stream().map(post -> {
			User user = userFeignService.findPublicById(post.getUserid());
			System.out.println(user.toString());
			post.setUser(user);
			return post;
		}).collect(Collectors.toList()), pageable, totalElements);

		model.addAttribute("pages", pages);

		int totalPages = pages.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return "index";
	}

}
