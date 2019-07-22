package com.kuky.blog;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@MapperScan("com.kuky.blog.basics.dao")
@SpringBootApplication
@Import(FdfsClientConfig.class)
public class MusicBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicBlogApplication.class, args);
	}

}
