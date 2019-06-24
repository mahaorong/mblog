package com.ippse.mblog.web.controller;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ippse.mblog.lib.client.OkFile;
import com.ippse.mblog.lib.dao.PostRepository;
import com.ippse.mblog.lib.model.Post;
import com.ippse.mblog.lib.services.ServiceException;
import com.ippse.mblog.lib.utils.ID;
import com.ippse.mblog.lib.utils.Time;
import com.ippse.mblog.web.oauth.MediaFeignService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/post")
public class PostController extends BaseController {
	@Autowired
	private PostRepository mpostRepository;
	@Autowired
	private MediaFeignService mediaFeignService;

	@GetMapping("/{id}")
	public Post findOne(@PathVariable String id) {
		return mpostRepository.findById(id).orElseThrow(() -> new ServiceException(""));
	}

	@PostMapping
	@ResponseBody
	public void create(MultipartHttpServletRequest request, @ModelAttribute PostModel model) {
		log.info("[model]==============={}",model);
		log.info("[model.getFile()]==============={}",model.getFile().length);
		Post mpost = new Post();
		mpost.setId(ID.uuid());
		mpost.setUserid(sessuser.getId());
		mpost.setStatus("normal");
		mpost.setContent(model.getContent());
		mpost.setCtime(Time.timestamp());
		
		try {
            //CommonsMultipartFile file = null;
            //Iterator<String> iterator = request.getFileNames();
			if(model.getFile() != null){
				for(MultipartFile uploadedFile : model.getFile()) {
					//String key = iterator.next();
					//file = (CommonsMultipartFile) request.getFile(key);
					//log.info(file.getOriginalFilename());
					if (!uploadedFile.isEmpty()) {
						OkFile f = mediaFeignService.upload(uploadedFile);
						log.info(f.toString());
						mpost.addPhoto(f);
    					}
				}
			}

            mpostRepository.save(mpost);
        } catch (Exception e) {
            e.printStackTrace();
        }

		/*try {
			for (MultipartFile file : Arrays.asList(model.getFiles())) {
				log.info(file.getOriginalFilename());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		// OkFile f = mediaFeignService.upload(file);
		// mpost.addPhoto(f);
		// mpostRepository.save(mpost);
		//return "redirect:/";
	}

	@RequestMapping("/{id}")
	public void delete(@PathVariable String id) {
		mpostRepository.findById(id).orElseThrow(() -> new ServiceException(""));
		mpostRepository.deleteById(id);
	}

	@PostMapping("/{id}")
	public Post updateBook(@RequestBody Post mpost, @PathVariable String id) {
		if (mpost.getId() != id) {
			throw new ServiceException("");
		}
		mpostRepository.findById(id).orElseThrow(() -> new ServiceException(""));
		return mpostRepository.save(mpost);
	}

}
