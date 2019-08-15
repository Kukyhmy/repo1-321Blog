package com.kuky.blog.basics.service.impl;

import com.kuky.blog.basics.config.MyException;
import com.kuky.blog.basics.dao.BlogLinkMapper;
import com.kuky.blog.basics.entity.BlogLink;
import com.kuky.blog.basics.service.LinkService;
import com.kuky.blog.basics.utils.PageQueryUtil;
import com.kuky.blog.basics.utils.PageResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kuky
 * @create 2019/7/23 22:36
 */
@Service
@Slf4j
public class LinkServiceImpl implements LinkService {

    @Autowired
    private BlogLinkMapper blogLinkMapper;

    @Override
    public Map<Byte, List<BlogLink>> getLinksForLinkPage() {
        //获取所有链接数据
        List<BlogLink> links = blogLinkMapper.findLinkList(null);
        if (!CollectionUtils.isEmpty(links)) {
            //根据type进行分组
            Map<Byte, List<BlogLink>> linksMap = links.stream().collect(Collectors.groupingBy(BlogLink::getLinkType));

            log.info("根据type进行分组："+linksMap);
            //{0=[xxxxxxxxx],1=[xxxxxxxxxx],2=[xxxxxxxxxx]}
            return linksMap;
        }
        return null;
    }

    @Override
    public int getTotalLinks() {
        return blogLinkMapper.getTotalLinks(null);
    }

    @Override
    public PageResult getBlogLinkPage(PageQueryUtil pageUtil) {
        List<BlogLink> links = blogLinkMapper.findLinkList(pageUtil);
        int total = blogLinkMapper.getTotalLinks(pageUtil);
        PageResult pageResult = new PageResult(links, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean saveLink(BlogLink link) {
        return blogLinkMapper.insertSelective(link) > 0;
    }

    @Override
    public BlogLink selectById(Integer id) {
        return blogLinkMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean updateLink(BlogLink tempLink) {
        return blogLinkMapper.updateByPrimaryKeySelective(tempLink) > 0;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return blogLinkMapper.deleteBatch(ids) > 0;
    }

    @Override
    public List<BlogLink> selectLink() {
        return blogLinkMapper.selectLink();
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Override
    public boolean batchImport(String fileName, MultipartFile file) throws Exception {
        boolean notNull = false;
        List<BlogLink> linkList = new ArrayList<>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new MyException("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if(sheet!=null){
            notNull = true;
        }
        BlogLink link;
        for (int r = 2; r < sheet.getLastRowNum(); r++) {//r = 2 表示从第三行开始循环 如果你的第三行开始是数据
            Row row = sheet.getRow(r);//通过sheet表单对象得到 行对象
            if (row == null){
                continue;
            }

            //sheet.getLastRowNum() 的值是 10，所以Excel表中的数据至少是10条；不然报错 NullPointerException
            link = new BlogLink();

          /* if( row.getCell(0).getCellType() !=1){//循环时，得到每一行的单元格进行判断
                throw new MyException("导入失败(第"+(r+1)+"行,友链类型请设为文本格式)");
            }*/
           row.getCell(0).setCellType(HSSFCell.CELL_TYPE_STRING);
            String linkType = row.getCell(0).getStringCellValue();//得到每一行第一个单元格的值
            if(linkType == null || linkType.isEmpty()){//判断是否为空
                throw new MyException("导入失败(第"+(r+1)+"行,友链类型未填写)");
            }

            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);//得到每一行的 第二个单元格的值
            String linkName = row.getCell(1).getStringCellValue();
            if(linkName==null || linkName.isEmpty()){
                throw new MyException("导入失败(第"+(r+1)+"行,友链名称未填写)");
            }
            row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);//得到每一行的 第三个单元格的值
            String linkUrl = row.getCell(2).getStringCellValue();
            if(linkName==null || linkName.isEmpty()){
                throw new MyException("导入失败(第"+(r+1)+"行,友链地址未填写)");
            }
            row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);//得到每一行的 第四个单元格的值
            String linkDescription = row.getCell(3).getStringCellValue();
            if(linkName==null || linkName.isEmpty()){
                throw new MyException("导入失败(第"+(r+1)+"行,友链描述详情未填写)");
            }
            row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);//得到每一行的 第五个单元格的值
            String linkRank = row.getCell(4).getStringCellValue();
            if(linkName==null || linkName.isEmpty()){
                throw new MyException("导入失败(第"+(r+1)+"行,友链序号未填写)");
            }
            row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);//得到每一行的 第六个单元格的值
            String isDelete = row.getCell(5).getStringCellValue();
            if(linkName==null || linkName.isEmpty()){
                throw new MyException("导入失败(第"+(r+1)+"行,友链是否存在未填写)");
            }
            row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);//得到每一行的 第七个单元格的值
            String createTime = row.getCell(6).getStringCellValue();
            if(linkName==null || linkName.isEmpty()){
                throw new MyException("导入失败(第"+(r+1)+"行,友链创建时间未填写)");
            }


            //完整的循环一次 就组成了一个对象
            link.setLinkType(Byte.valueOf(linkType));
            link.setLinkName(linkName);
            link.setLinkUrl(linkUrl);
            link.setLinkDescription(linkDescription);
            link.setLinkRank(Integer.parseInt(linkRank));
            link.setIsDeleted(Byte.valueOf(isDelete));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            link.setCreateTime(format.parse(createTime));


            linkList.add(link);
        }
        for (BlogLink linkResord : linkList) {
            Integer linkId = linkResord.getLinkId();
            BlogLink blogLink = blogLinkMapper.selectByPrimaryKey(linkId);
            if (blogLink==null) {
                blogLinkMapper.insertSelective(linkResord);
                log.info(" 插入 "+linkResord);
            } else {
                blogLinkMapper.updateByPrimaryKeySelective(linkResord);
                log.info(" 更新 "+linkResord);
            }
        }
        return notNull;
    }
}
