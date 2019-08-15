package com.kuky.blog;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@MapperScan("com.kuky.blog.basics.dao")
@SpringBootApplication
public class MusicBlogApplication extends SpringBootServletInitializer {

	private static final Class<MusicBlogApplication> applicationClass = MusicBlogApplication.class;
	private static Logger logger = LoggerFactory.getLogger(MusicBlogApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MusicBlogApplication.class, args);
		logger.info("Application has started");
	}
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MusicBlogApplication.class);
	}

}
