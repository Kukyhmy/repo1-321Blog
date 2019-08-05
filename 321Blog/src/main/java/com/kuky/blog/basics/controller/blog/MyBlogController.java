package com.kuky.blog.basics.controller.blog;

import com.kuky.blog.basics.entity.AdminUser;
import com.kuky.blog.basics.entity.BlogComment;
import com.kuky.blog.basics.entity.BlogLink;
import com.kuky.blog.basics.entity.es.EsBlog;
import com.kuky.blog.basics.service.*;
import com.kuky.blog.basics.utils.*;
import com.kuky.blog.basics.vo.BlogDetailVO;
import com.kuky.blog.basics.vo.TagVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class MyBlogController {

    public static String theme = "321blog/mobanzhijia/";

    @Resource
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    @Resource
    private ConfigService configService;

    @Resource
    private CategoryService categoryService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EsBlogService esBlogService;
    /**
     * 首页
     *
     * @return
     */
    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return this.page(request, 1, userDetails);
    }

    @GetMapping("/blogs")
    public String listEsBlogs(
            @RequestParam(value="order",required=false,defaultValue="new") String order,
            @RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
            @RequestParam(value="async",required=false) boolean async,
            @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
            Model model) {

        Page<EsBlog> page = null;
        List<EsBlog> list = null;
        boolean isEmpty = true; // 系统初始化时，没有博客数据
        try {
            if (order.equals("hot")) { // 最热查询
                Sort sort = new Sort(Sort.Direction.DESC,"blogViews","blogVoteSize","createTime");
                Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
                page = esBlogService.listHotestEsBlogs(keyword, pageable);
            } else if (order.equals("new")) { // 最新查询
                Sort sort = new Sort(Sort.Direction.DESC,"createTime");
                Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
                page = esBlogService.listNewestEsBlogs(keyword, pageable);
            }

            isEmpty = false;
        } catch (Exception e) {
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = esBlogService.listEsBlogs(pageable);
        }

        list = page.getContent();	// 当前所在页面数据列表


        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);

        // 首次访问页面才加载
        if (!async && !isEmpty) {
            List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
            model.addAttribute("newest", newest);
            List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
            model.addAttribute("hotest", hotest);
            List<TagVO> tags = esBlogService.listTop30Tags();
            model.addAttribute("tags", tags);
            List<AdminUser> users = esBlogService.listTop12Users();
            model.addAttribute("users", users);
        }

        return (async==true?"/index :: #mainContainerRepleace":"/index");
    }

    @GetMapping("/newest")
    public String listNewestEsBlogs(Model model) {
        List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
        model.addAttribute("newest", newest);
        return "newest";
    }

    @GetMapping("/hotest")
    public String listHotestEsBlogs(Model model) {
        List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
        model.addAttribute("hotest", hotest);
        return "hotest";
    }

    /**
     * 首页 分页数据
     *
     * @return
     */
    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest req, @PathVariable("pageNum") int pageNum, @AuthenticationPrincipal UserDetails userDetails) {
        PageResult blogPageResult = blogService.getBlogsForIndexPage(pageNum);
        if (blogPageResult == null) {
            return "error/error_404";
        }
        if (userDetails != null) {
            req.setAttribute("username", userDetails.getUsername());
        }
        req.setAttribute("blogPageResult", blogPageResult);
        req.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        req.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        req.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        req.setAttribute("pageName", "首页");
        req.setAttribute("configurations", configService.getAllConfigs());
        return theme + "index";
    }


    /**
     * 点赞数增加
     * @return
     */
    @RequestMapping(value = "/blog/like", method = {RequestMethod.POST})
    @ResponseBody
    public Integer increaseVoteSize(HttpServletRequest request) {
        Long blogId = Long.parseLong(request.getParameter("blogId"));
        BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);
        Integer blogVoteSize = blogDetailVO.getBlogVoteSize();
        blogService.increaseVoteSize(blogId);
        return blogVoteSize + 1;
    }

    /**
     * 进入博客详情页
     *
     * @return
     */
    @GetMapping({"/blog/{blogId}", "/article/{blogId}"})
    public String detail(HttpServletRequest req, @PathVariable("blogId") Long blogId, @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);
        if (blogDetailVO != null) {
            req.setAttribute("blogDetailVO", blogDetailVO);
            req.setAttribute("commentPageResult", commentService.getCommentPageByBlogIdAndPageNum(blogId, commentPage));
        }
        // 判断操作用户的点赞情况
       /* List<Vote> votes = blogDetailVO.getVotes();
        Vote currentVote = null; // 当前用户的点赞情况

        if (principal !=null) {
            for (Vote vote : votes) {
                vote.getUser().getUsername().equals(principal.getUsername());
                currentVote = vote;
                break;
            }
        }

        req.setAttribute("currentVote",currentVote);*/
        req.setAttribute("pageName", "详情");
        req.setAttribute("configurations", configService.getAllConfigs());
        return theme + "detail";
    }

    /**
     * Categories页面(包括分类数据和标签数据)
     *
     * @return
     */
    @GetMapping({"/categories"})
    public String categories(HttpServletRequest req) {
        req.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        req.setAttribute("categories", categoryService.getAllCategories());
        req.setAttribute("pageName", "分类页面");
        req.setAttribute("configurations", configService.getAllConfigs());
        return theme + "category";
    }

    /**
     * 标签列表页
     *
     * @return
     */
    @GetMapping({"/tag/{tagName}"})
    public String tag(HttpServletRequest req, @PathVariable("tagName") String tagName) {
        return tag(req, tagName, 1);
    }

    /**
     * 标签列表页
     *
     * @return
     */
    @GetMapping({"/tag/{tagName}/{page}"})
    public String tag(HttpServletRequest req, @PathVariable("tagName") String tagName, @PathVariable("page") Integer page) {
        PageResult blogPageResult = blogService.getBlogsPageByTag(tagName, page);
        req.setAttribute("blogPageResult", blogPageResult);
        req.setAttribute("pageName", "标签");
        req.setAttribute("pageUrl", "tag");
        req.setAttribute("keyword", tagName);
        req.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        req.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        req.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        req.setAttribute("configurations", configService.getAllConfigs());
        return theme + "list";
    }

    /**
     * 分类列表页
     *
     * @return
     */
    @GetMapping({"/category/{categoryName}"})
    public String category(HttpServletRequest req, @PathVariable("categoryName") String categoryName) {
        return category(req, categoryName, 1);
    }

    /**
     * 分类列表页
     *
     * @return
     */
    @GetMapping({"/category/{categoryName}/{page}"})
    public String category(HttpServletRequest req, @PathVariable("categoryName") String categoryName, @PathVariable("page") Integer page) {
        PageResult blogPageResult = blogService.getBlogsPageByCategory(categoryName, page);
        req.setAttribute("blogPageResult", blogPageResult);
        req.setAttribute("pageName", "分类");
        req.setAttribute("pageUrl", "category");
        req.setAttribute("keyword", categoryName);
        req.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        req.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        req.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        req.setAttribute("configurations", configService.getAllConfigs());
        return theme + "list";
    }

    /**
     * 搜索列表页
     *
     * @return
     */
    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest req, @PathVariable("keyword") String keyword) {
        return search(req, keyword, 1);
    }

    /**
     * 搜索列表页
     *
     * @return
     */
    @GetMapping({"/search/{keyword}/{page}"})
    public String search(HttpServletRequest req, @PathVariable("keyword") String keyword, @PathVariable("page") Integer page) {
        PageResult blogPageResult = blogService.getBlogsPageBySearch(keyword, page);
        req.setAttribute("blogPageResult", blogPageResult);
        req.setAttribute("pageName", "搜索");
        req.setAttribute("pageUrl", "search");
        req.setAttribute("keyword", keyword);
        req.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        req.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        req.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        req.setAttribute("configurations", configService.getAllConfigs());
        return theme + "list";
    }


    /**
     * 友情链接页:
     * 判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
     *
     * @return
     */
    @GetMapping({"/link"})
    // @ResponseBody
    public String link(HttpServletRequest req) {
        req.setAttribute("pageName", "友情链接");
        Map<Byte, List<BlogLink>> linkMap = linkService.getLinksForLinkPage();
        if (linkMap != null) {
            if (linkMap.containsKey((byte) 0)) {
                req.setAttribute("favoriteLinks", linkMap.get((byte) 0));
            }
            if (linkMap.containsKey((byte) 1)) {
                req.setAttribute("recommendLinks", linkMap.get((byte) 1));
            }
            if (linkMap.containsKey((byte) 2)) {
                req.setAttribute("personalLinks", linkMap.get((byte) 2));
            }
        }
        req.setAttribute("configurations", configService.getAllConfigs());
        return theme + "link";
        //return linkMap;
    }

    /**
     * 评论操作
     */
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest req, HttpSession session,
                          @RequestParam Long blogId, @RequestParam String verifyCode,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam String websiteUrl, @RequestParam String commentBody) {
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (!verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult("验证码错误");
        }
        String ref = req.getHeader("Referer");
        if (StringUtils.isEmpty(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (null == blogId || blogId < 0) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (StringUtils.isEmpty(commentator)) {
            return ResultGenerator.genFailResult("请输入称呼");
        }
        if (StringUtils.isEmpty(email)) {
            return ResultGenerator.genFailResult("请输入邮箱地址");
        }
        if (!PatternUtil.isEmail(email)) {
            return ResultGenerator.genFailResult("请输入正确的邮箱地址");
        }
        if (StringUtils.isEmpty(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }
        BlogComment comment = new BlogComment();
        comment.setBlogId(blogId);
        comment.setCommentator(MyBlogUtils.cleanString(commentator));
        comment.setEmail(email);
        if (PatternUtil.isURL(websiteUrl)) {
            comment.setWebsiteUrl(websiteUrl);
        }
        comment.setCommentBody(MyBlogUtils.cleanString(commentBody));
        return ResultGenerator.genSuccessResult(commentService.addComment(comment));
    }

    /**
     * 关于页面 以及其他配置了subUrl的文章页
     *
     * @return
     */
    @GetMapping({"/{subUrl}"})
    public String detail(HttpServletRequest req, @PathVariable("subUrl") String subUrl) {
        BlogDetailVO blogDetailVO = blogService.getBlogDetailBySubUrl(subUrl);
        if (blogDetailVO != null) {
            req.setAttribute("blogDetailVO", blogDetailVO);
            req.setAttribute("pageName", subUrl);
            req.setAttribute("configurations", configService.getAllConfigs());
            return theme + "detail";
        } else {
            return "error/error_400";
        }
    }


}
