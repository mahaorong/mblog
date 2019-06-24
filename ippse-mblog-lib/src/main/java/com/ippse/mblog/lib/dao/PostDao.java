package com.ippse.mblog.lib.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ippse.mblog.lib.model.Post;

@Component
@Transactional
public class PostDao {
	@PersistenceContext
	private EntityManager entityManager;

	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	public Page<Post> findAll(String sessuserid, Pageable pageable) {
		DetachedCriteria dc = DetachedCriteria.forClass(Post.class);
		Session session = getSession();
		Filter filter = session.enableFilter("postlike");
		filter.setParameter("userid", sessuserid);
		return findPageByCriteria(session, dc, pageable);
	}

	public Page<Post> findPageByCriteria(Session session, final DetachedCriteria detachedCriteria, Pageable pageable) {
		final int startindex = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		Criteria criteria = detachedCriteria.getExecutableCriteria(session);
		Object object = criteria.setProjection(Projections.rowCount()).uniqueResult();
		long totalCount = 0;
		try {
			totalCount = (Long) object;
		} catch (Exception e) {
		}
		criteria.setProjection(null);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Post> items = criteria.setFirstResult(startindex).setMaxResults(pageable.getPageSize()).list();
		Page<Post> pages = new PageImpl<Post>(items, pageable, totalCount);
		return pages;
	}
}
