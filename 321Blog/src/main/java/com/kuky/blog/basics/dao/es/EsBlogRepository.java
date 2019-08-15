package com.kuky.blog.basics.dao.es;

import com.kuky.blog.basics.entity.es.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Blog 存储库.
 */
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {

	/**
	 * 模糊查询(去重) 分页
	 * 	 * 如果不设置分页信息，默认带分页，每页显示10条数据。
	 * 	 * 如果设置分页信息，应该在方法中添加一个参数Pageable
	 * 	 * Pageable pageable = PageRequest.of(0,15);
	 * 	 * *******设置分页信息，默认是从第0页开始
	 * @param blogTitle
	 * @param blogSummary
	 * @param blogContent
	 * @param blogTags
	 * @param blogCategoryName
	 * @param pageable
	 * @return
	 */
	Page<EsBlog> findDistinctEsBlogByBlogTitleContainingOrBlogSummaryContainingOrBlogContentContainingOrBlogTagsContainingOrBlogCategoryNameContaining(String blogTitle, String blogSummary, String blogContent, String blogTags,String blogCategoryName, Pageable pageable);
	
	EsBlog findByBlogId(Long blogId);
}
