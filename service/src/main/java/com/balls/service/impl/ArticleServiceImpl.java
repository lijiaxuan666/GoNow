package com.balls.service.impl;

import com.balls.mapper.ArticleMapper;
import com.balls.mapper.RelationMapper;
import com.balls.pojo.ArticleComment;
import com.balls.pojo.ArticleContent;
import com.balls.pojo.ArticleUser;
import com.balls.service.ArticleService;
import com.balls.util.DateUtil;

import com.balls.util.ElasticSearchUtil;
import com.balls.util.RedisUtil;
import com.balls.util.SnowFlakeGenerateIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RelationMapper relationMapper;

    @Autowired
    private ElasticSearchUtil elasticSearchUtil;//elasticsearch工具类

    @Autowired
    private DateUtil dateUtil;//自定义日期工具类

    @Autowired
    private RedisUtil redisUtil;//自定义日期工具类

    @Resource(name = "threadPoolInstance")
    private ExecutorService executorService;//线程池，异步执行任务

    public List<ArticleContent> getArticleList(String uid) {
        //查询出该用户的所有文章
        List<ArticleContent> list = articleMapper.getArticleList(uid);
        if (list.size() == 0)
            return null;
        //向其注入该用户的用户名和头像
        ArticleUser user = articleMapper.getInfo(uid);
        for (ArticleContent articleContent : list) {
            articleContent.setUsername(user.getUsername());
            articleContent.setHeadSculpture(user.getHeadSculpture());
        }
        return list;
    }

    public ArticleContent getArticle(String articleId) {
        ArticleContent articleContent = articleMapper.getArticle(articleId);

        if (articleContent == null)
            return null;
        ArticleUser user = articleMapper.getInfo(articleContent.getUid());
        articleContent.setUsername(user.getUsername());
        articleContent.setHeadSculpture(user.getHeadSculpture());

        //查询出与该文章相关的评论
        List<ArticleComment> list = articleMapper.getComment(articleId);
        for (ArticleComment articleComment : list) {
            user = articleMapper.getInfo(articleComment.getUid());
            articleComment.setUsername(user.getUsername());
            articleComment.setHeadSculpture(user.getHeadSculpture());
        }
        //将处理好的评论集合放入文章中
        articleContent.setComment(list);

        return articleContent;
    }

    public boolean addArticle(ArticleContent articleContent) {
        //注入文章全局唯一id与发布时间
        SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker = new SnowFlakeGenerateIdWorker(0L, 0L);
        String articleId = snowFlakeGenerateIdWorker.generateNextId();
        //利用自定义日期工具类获取字符串格式日期
        String gmtCreate = dateUtil.getNowTimeAsString();

        articleContent.setArticleId(articleId);
        articleContent.setGmtCreate(gmtCreate);


        //注入头像和用户名
        ArticleUser user = articleMapper.getInfo(articleContent.getUid());
        articleContent.setUsername(user.getUsername());
        articleContent.setHeadSculpture(user.getHeadSculpture());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //向redis中新添该文章
                    redisUtil.zAdd("putChoice", articleContent.getArticleId(), dateUtil.getTime(articleContent.getTime()));
                    //向elasticsearch上传文章
                    elasticSearchUtil.addArticle(articleContent);

                } catch (IOException e) {
                    log.info("添加文章上传elasticsearch时出现异常");
                } catch (ParseException e) {
                    log.info("日期格式出现问题，转化失败");
                }
            }
        });

        return articleMapper.addArticle(articleId, articleContent.getUid(), articleContent.getContent(), articleContent.getAddress(), articleContent.getTime(), gmtCreate);
    }

    public boolean addComment(ArticleComment articleComment) {
        //利用自定义日期工具类获取字符串格式日期
        String gmtCreate = dateUtil.getNowTimeAsString();
        return articleMapper.addComment(articleComment.getUid(), articleComment.getArticleId(), articleComment.getComment(), gmtCreate);
    }

    public boolean updateArticle(ArticleContent articleContent,String uid)  {

        Long time= null;
        try {
            time = dateUtil.getTime(articleContent.getTime());
        } catch (ParseException e) {
            log.info("日期格式错误，修改文章未成功，已返回前端");
            return false;
        }

        //获取原本的文章，保留未修改的数据
        ArticleUser user = articleMapper.getInfo(uid);
        articleContent.setUsername(user.getUsername());
        articleContent.setHeadSculpture(user.getHeadSculpture());
        articleContent.setUid(uid);
        articleContent.setGmtCreate(articleMapper.getCreateTime(articleContent.getArticleId()));

        //开启一个新线程异步更新elasticsearch与redis中的信息，同时防止elasticsearch出现问题导致数据库无法更新
        Long finalTime = time;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //向redis中加入该文章
                redisUtil.zAdd("putChoice", articleContent.getArticleId(), finalTime);
                try {
                    //更新elasticsearch中的文章
                    elasticSearchUtil.updateArticle(articleContent);
                } catch (IOException e) {
                    log.info("修改了过期的文章,导致出现异常");
                    e.printStackTrace();
                }
            }
        });


        return articleMapper.updateArticle(articleContent.getArticleId(), articleContent.getContent(), articleContent.getAddress(), articleContent.getTime());
    }

    public boolean deleteArticle(String articleId) {

        //异步删除elasticsearch数据
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    elasticSearchUtil.deleteArticle(articleId);
                } catch (IOException e) {
                    log.info("删除elasticsearch中的文章时出现异常，未成功删除");
                }
            }
        });

        List<ArticleComment> list = articleMapper.getComment(articleId);
        redisUtil.zRemove("putChoice",articleId);
        for (ArticleComment articleComment : list) {
            articleMapper.deleteComment(articleComment.getCommentId());
        }
        return articleMapper.deleteArticle(articleId);
    }

    public boolean deleteComment(String commentId) {
        return articleMapper.deleteComment(Integer.parseInt(commentId));
    }

    public List<ArticleContent> searchAddress(String address) {
        List<ArticleContent> list = new ArrayList<>();
        try {
            list = elasticSearchUtil.searchArticle(address);
        } catch (IOException e) {
            log.info("模糊查询时出现异常，可能是elasticsearch未开启");
        }
        return list;
    }


    public List<ArticleContent> putChoice() {
        //获取比赛时间在过去三小时到未来三天内的的数据
        Long now = dateUtil.getTime();
        Set<String> set = redisUtil.zRangeByScore("putChoice", now - 10800000L, now + 7 * 86400000L);

        List<ArticleContent> list = new ArrayList<>();

        //从elasticsearch查询在该时间范围内的文章放入集合中返回
        for (String articleId : set) {
            try {
                list.add(elasticSearchUtil.getArticle(articleId));
            } catch (IOException e) {
                log.info("elasticsearch出现问题，正在从数据库中直接获取");
                //直接从数据库中查
                ArticleUser user;
                for (String aid : set) {

                    ArticleContent articleContent = articleMapper.getArticle(aid);

                    if(articleContent==null)
                        continue;
                    user = articleMapper.getInfo(articleContent.getUid());
                    articleContent.setUsername(user.getUsername());
                    articleContent.setHeadSculpture(user.getHeadSculpture());
                    list.add(articleContent);
                }
                return list;
            }
        }

        return list;
    }

    public List<ArticleContent> putFollow(String uid){
        Long now = dateUtil.getTime();
        List<String> follows=relationMapper.getfollow(uid);
        List<String> allArticle=new ArrayList<>();//所有集合
        Map<String,ArticleUser> articleUserMap=new HashMap<>();//用户及用户信息
        Map<String,ArticleContent> map=new HashMap<>();//文章id与文章内容
        List<ArticleContent> list=new ArrayList<>();
        //获取过去三小时到一个月内的文章
        Set<String> set=redisUtil.zRangeByScore("putChoice", now - 10800000L, now + 30 * 86400000L);
        for(String id:follows){
            //将关注的用户发布的所有文章加入集合中
            articleUserMap.put(id,articleMapper.getInfo(id));
            allArticle.addAll(articleMapper.getArticleIdList(id));
        }

        for(String id:allArticle){
            if(!set.contains(id))
                continue;
            ArticleContent articleContent=articleMapper.getArticle(id);
            articleContent.setUsername(articleUserMap.get(articleContent.getUid()).getUsername());
            articleContent.setHeadSculpture(articleUserMap.get(articleContent.getUid()).getHeadSculpture());
            map.put(id,articleContent);
        }

        for(String articleId:set){
            if(map.containsKey(articleId))
                list.add(map.get(articleId));
        }

        /*
        //elasticsearch版,没有按时间排序
        List<String> follows=relationMapper.getfollow(uid);
        List<String> allArticle=new ArrayList<>();
        List<ArticleContent> article=new ArrayList<>();
        for(String id:follows){
            //将关注的用户发布的所有文章加入集合中
            allArticle.addAll(articleMapper.getArticleIdList(id));
        }
        for(String articleId:allArticle){
            try {
                article.add(elasticSearchUtil.getArticle(articleId));
            } catch (IOException e) {
                log.info("elasticsearch关闭导致关注推送失败");
            }
        }*/
        return list;
    }
}
