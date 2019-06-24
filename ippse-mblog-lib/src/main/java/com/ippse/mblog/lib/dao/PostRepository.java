package com.ippse.mblog.lib.dao;


import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ippse.mblog.lib.model.Post;

@Repository
@Transactional
public interface PostRepository extends PagingAndSortingRepository<Post, String>{
	
	@Query(value = "SELECT * FROM post WHERE reply = ?1", countQuery = "SELECT count(*) FROM post WHERE reply = ?1", nativeQuery = true)
	Page<Post> findByReply(String replyid, Pageable pageable);
	
	@Query(value = "SELECT * FROM post WHERE reply is null", countQuery = "SELECT count(*) FROM post WHERE reply is null", nativeQuery = true)
	Page<Post> findAll(Pageable pageable);
	
	@Query(value = "update post set comments_count=(select count(id) as number from post where reply = ?1) where id= ?1", countQuery = "update post set comments_count=(select count(id) as number from post where reply = ?1) where id= ?1", nativeQuery = true)
	void updateCommentCount(String id);
	
	@Query(value = "update post set like_count=(select count(id) as number from postlike where postid = ?1) where id= ?1", countQuery = "update post set like_count=(select count(id) as number from postlike where postid = ?1) where id= ?1", nativeQuery = true)
	void updateLikeCount(String id);
}
