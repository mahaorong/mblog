package com.ippse.mblog.lib.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface ExtendedRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

	public List<T> findByAttributeContainsText(String attributeName, String text);

}
