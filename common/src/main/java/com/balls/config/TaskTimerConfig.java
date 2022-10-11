package com.balls.config;

//import com.balls.mapper.ArticleMapper;
import com.balls.util.DateUtil;
import com.balls.util.ElasticSearchUtil;
import com.balls.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

@Slf4j
@Configuration
@EnableScheduling
public class TaskTimerConfig {

    //@Autowired
    //private ArticleMapper articleMapper;

    @Autowired
    private ElasticSearchUtil elasticSearchUtil;//elasticsearch工具类

    @Autowired
    private DateUtil dateUtil;//自定义日期工具类

    @Autowired
    private RedisUtil redisUtil;//自定义日期工具类

    //每3小时执行一次清理任务
    /*@Scheduled(cron = "0 0 0/3 * * ?")
    public void updatePutChoice(){

        Long now = dateUtil.getTime();
        log.info(dateUtil.getNowTimeAsString()+"执行了一次检测过期操作");

        //从redis与elasticsearch中移除比赛过期时间超过6小时的
        Set<String> removeSet = redisUtil.zRangeByScore("putChoice", 0L, now - 21600000L);
        for (String remove : removeSet) {
            redisUtil.zRemoveRangeByScore("putChoice", 0, now - 21600000L);
            //将数据库中过期的文章状态改为已过期
            //articleMapper.updateArticleState(remove);
            *//*try {
                //暂时禁用elasticsearch
                //elasticSearchUtil.deleteArticle(remove);
            } catch (IOException e) {
                log.info("elasticsearch删除过期文章时出现错误，本次定时更新推送已终止");
            }*//*
        }

    }*/

}
