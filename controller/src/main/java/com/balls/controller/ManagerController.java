package com.balls.controller;

import com.balls.enums.StatusCode;
import com.balls.mapper.ArticleMapper;
import com.balls.pojo.ArticleContent;
import com.balls.pojo.ArticleUser;
import com.balls.util.DateUtil;
import com.balls.util.ElasticSearchUtil;
import com.balls.util.RedisUtil;
import com.balls.util.ResultUtil;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

//方便后台管理数据接口
@RestController
public class ManagerController {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ElasticSearchUtil elasticSearchUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //重置elasticsearch中的数据
    @PostMapping("/reset")
    public Map create() throws IOException {

        //1.查询索引请求
        GetIndexRequest request1=new GetIndexRequest("article");
        //2.执行exists方法判断是否存在
        boolean exists=restHighLevelClient.indices().exists(request1,RequestOptions.DEFAULT);

        if(exists){
            //若存在就删除索引
            DeleteIndexRequest request=new DeleteIndexRequest("article");
            restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        }

        //创建索引
        CreateIndexRequest request = new CreateIndexRequest("article");
        restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        //向该索引添加数据
        bulkAdd();

        return ResultUtil.getResult(StatusCode.SUCCESS);
    }

    //批量上传到elasticsearch
    @PostMapping("/bulkAdd")
    public Map bulkAdd() throws IOException {

        //查询出数据库所有未删除的文章，加入elasticsearch
        List<ArticleContent> list = articleMapper.bulkAddArticle();
        ArticleUser user;
        for (ArticleContent articleContent : list) {
            //注入每个人的头像与用户名
            user = articleMapper.getInfo(articleContent.getUid());
            articleContent.setUsername(user.getUsername());
            articleContent.setHeadSculpture(user.getHeadSculpture());
        }
        elasticSearchUtil.bulkAddArticle(list);

        return ResultUtil.getResult(StatusCode.SUCCESS);
    }

    //批量更新redis与elasticsearch中的数据与mysql一致
    @PostMapping("/bulkUpdate")
    public Map bulkUpdateFromMysql() throws IOException, ParseException {

        List<ArticleContent> list = articleMapper.bulkAddArticle();
        ArticleUser user;
        for (ArticleContent articleContent : list) {
            //注入每个人的头像与用户名
            user = articleMapper.getInfo(articleContent.getUid());
            articleContent.setUsername(user.getUsername());
            articleContent.setHeadSculpture(user.getHeadSculpture());
            elasticSearchUtil.updateArticle(articleContent);
            redisUtil.zAdd("putChoice", articleContent.getArticleId(), dateUtil.getTime(articleContent.getTime()));
        }

        return ResultUtil.getResult(StatusCode.SUCCESS);
    }

}
