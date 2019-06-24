package com.ippse.mblog.lib.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ippse.mblog.lib.dao.PostRepository;
import com.ippse.mblog.lib.model.Post;

@Service
public class PostService {
	@Autowired
	private PostRepository mpostRepository;
	@PersistenceContext 
    private EntityManager entityManager;
	
	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	
	public void save(Post mpost) {
		mpostRepository.save(mpost);
	}

	
}
