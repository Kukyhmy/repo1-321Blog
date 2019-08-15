package com.kuky.blog.basics.entity.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuky.blog.basics.entity.Blog;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Blog.
 * 
 */
@Document(indexName = "blog", type = "blog")
@XmlRootElement // MediaType 转为 XML
@Data
public class EsBlog implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id  // 主键
	//@Field(type = FieldType.Long,store = true)
	private String id;

	@Field(index = FieldIndex.not_analyzed,type = FieldType.Long)
	private Long blogId; // Blog 的 id

	private String blogTitle;

	private String blogSummary;

	private String blogContent;

	private String blogCategoryName;

	//@Field(type = FieldType.String, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
	private String blogTags;

	@Field(index = FieldIndex.not_analyzed,type = FieldType.String)  // 不做全文检索字段
	private String blogSubUrl;

	@Field(index = FieldIndex.not_analyzed,type = FieldType.String)  // 不做全文检索字段
	private String blogCoverImage;

	@Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)  // 不做全文检索字段
	private Integer blogCategoryId;

	@Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)  // 不做全文检索字段
	private Byte blogStatus;

	@Field(index = FieldIndex.not_analyzed,type = FieldType.String)  // 不做全文检索字段
	private String username;

	@Field(index = FieldIndex.not_analyzed,type = FieldType.String)  // 不做全文检索字段
	private String avatar;
	@Field(index = FieldIndex.not_analyzed,type = FieldType.String)  // 不做全文检索字段
	private Long blogViews; // 访问量、阅读量

	@Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)  // 不做全文检索字段
	private Byte enableComment;

	@Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)  // 不做全文检索字段
	private Byte isDeleted;

	@Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)  // 不做全文检索字段
	private Integer blogVoteSize = 0;

	@Field(index = FieldIndex.not_analyzed,type = FieldType.Date)  // 不做全文检索字段
	private Date createTime;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.Date)  // 不做全文检索字段
	private Date updateTime;


	protected EsBlog() {  // JPA 的规范要求无参构造函数；设为 protected 防止直接使用 
	}

	public EsBlog(String blogTitle, String blogContent) {
		this.blogTitle = blogTitle;
		this.blogContent = blogContent;
	}

	public EsBlog( String avatar,String username,Long blogId, String blogTitle, String blogSummary, String blogContent, String blogCategoryName,
				  String blogTags, String blogSubUrl, String blogCoverImage, Integer blogCategoryId, Byte blogStatus,
				  Long blogViews, Byte enableComment, Byte isDeleted, Integer blogVoteSize, Date createTime, Date updateTime) {
		this.avatar = avatar;
		this.blogId = blogId;
		this.blogTitle = blogTitle;
		this.blogSummary = blogSummary;
		this.username = username;
		this.blogContent = blogContent;
		this.blogCategoryName = blogCategoryName;
		this.blogTags = blogTags;
		this.blogSubUrl = blogSubUrl;
		this.blogCoverImage = blogCoverImage;
		this.blogCategoryId = blogCategoryId;
		this.blogStatus = blogStatus;
		this.blogViews = blogViews;
		this.enableComment = enableComment;
		this.isDeleted = isDeleted;
		this.blogVoteSize = blogVoteSize;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public EsBlog(Blog blog){
		this.username = blog.getAdminUser().getUsername();
		this.avatar = blog.getAdminUser().getAvatar();
		this.blogId = blog.getId();
		this.blogTitle = blog.getBlogTitle();
		this.blogSummary = blog.getBlogSummary();
		this.blogContent = blog.getBlogContent();
		this.blogCategoryName = blog.getBlogCategoryName();
		this.blogTags = blog.getBlogTags();
		this.blogSubUrl = blog.getBlogSubUrl();
		this.blogCoverImage = blog.getBlogCoverImage();
		this.blogCategoryId = blog.getBlogCategoryId();
		this.blogStatus = blog.getBlogStatus();
		this.blogViews = blog.getBlogViews();
		this.enableComment = blog.getEnableComment();
		this.isDeleted = blog.getIsDeleted();
		this.blogVoteSize = blog.getBlogVoteSize();
		this.createTime = blog.getCreateTime();
		this.updateTime = blog.getUpdateTime();
	}
 
	public void update(Blog blog){
		this.username = blog.getAdminUser().getUsername();
		this.avatar = blog.getAdminUser().getAvatar();
		this.blogId = blog.getId();
		this.blogTitle = blog.getBlogTitle();
		this.blogSummary = blog.getBlogSummary();
		this.blogContent = blog.getBlogContent();
		this.blogCategoryName = blog.getBlogCategoryName();
		this.blogTags = blog.getBlogTags();
		this.blogSubUrl = blog.getBlogSubUrl();
		this.blogCoverImage = blog.getBlogCoverImage();
		this.blogCategoryId = blog.getBlogCategoryId();
		this.blogStatus = blog.getBlogStatus();
		this.blogViews = blog.getBlogViews();
		this.enableComment = blog.getEnableComment();
		this.isDeleted = blog.getIsDeleted();
		this.blogVoteSize = blog.getBlogVoteSize();
		this.createTime = blog.getCreateTime();
		this.updateTime = blog.getUpdateTime();
	}
 
	

}
