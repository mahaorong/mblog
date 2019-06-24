package com.ippse.mblog.lib.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ippse.mblog.lib.model.Postlike;

@Repository
@Transactional
public interface PostLikeRepository extends PagingAndSortingRepository<Postlike, String>{
	
	@Query(value="delete from postlike where postlike.userid = ?2 and postlike.postid = ?1" ,countQuery = "delete from postlike where postlike.userid = ?2 and postlike.postid = ?1", nativeQuery = true)
	void deleteByPostId(String postid,String userid);

}
