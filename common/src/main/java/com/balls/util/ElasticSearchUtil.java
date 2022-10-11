package com.balls.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.balls.pojo.ArticleContent;
import net.minidev.json.JSONValue;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ElasticSearchUtil {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //单文件上传
    public void addArticle(ArticleContent articleContent) throws IOException {
        //1.创建请求
        IndexRequest request=new IndexRequest("article");
        //2.设置规则 PUT /article/_doc/文章id
        //设置文档id，设置超时=1s等，不设置会使用默认的
        request.id(articleContent.getArticleId()).timeout("1s");

        //3.将数据放入请求，要将对象转化为json格式
        //XContentType.JSON，告诉它传的数据是JSON类型
        request.source(JSONValue.toJSONString(articleContent), XContentType.JSON);

        //4.客户端发送请求，获取响应结果
        IndexResponse indexResponse=restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
    }

    //模糊查询
    public List<ArticleContent> searchArticle(String text) throws IOException {
        SearchRequest searchRequest=new SearchRequest("article");//里面可以放多个索引
        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();//构造搜索条件

        //此处可以使用QueryBuilders工具类中的方法
        //查询content,article_name中含有text的
        sourceBuilder.query(QueryBuilders.multiMatchQuery(text,"address"));

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse=restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);

        //获取总条数
        System.out.println(text+"共查询到"+searchResponse.getHits().getTotalHits().value+"条结果");

        //输出结果数据（如果不设置返回条数，大于10条默认只返回10条）
        SearchHit[] hits=searchResponse.getHits().getHits();
        List<ArticleContent> list=new ArrayList<>();
        for(SearchHit hit :hits){
            Map<String, Object> map =hit.getSourceAsMap();
            list.add(JSONObject.parseObject(JSON.toJSONString(map),ArticleContent.class));
        }
        return list;
    }

    //获取文档
    public ArticleContent getArticle(String articleId) throws IOException {
        //1.创建请求,指定索引、文档id
        GetRequest request=new GetRequest("article",articleId);
        GetResponse getResponse=restHighLevelClient.get(request,RequestOptions.DEFAULT);

        Map<String,Object> map=getResponse.getSource();
        return JSONObject.parseObject(JSON.toJSONString(map),ArticleContent.class);
    }

    //更新文档
    public void updateArticle(ArticleContent articleContent) throws IOException {
        //1.创建请求,指定索引、文档id
        UpdateRequest request=new UpdateRequest("article",articleContent.getArticleId());

        //将创建的对象放入文档中
        request.doc(JSONValue.toJSONString(articleContent),XContentType.JSON);
        UpdateResponse updateResponse=restHighLevelClient.update(request,RequestOptions.DEFAULT);

        System.out.println(updateResponse.status());//更新成功返回OK
    }

    //删除文档
    public void deleteArticle(String articleId) throws IOException {
        DeleteRequest request=new DeleteRequest("article",articleId);

        DeleteResponse updateResponse=restHighLevelClient.delete(request,RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());//删除成功返回OK，没有找到返回NOT_FOUND
    }

    //批量上传
    public void bulkAddArticle(List<ArticleContent> list) throws IOException {
        BulkRequest bulkRequest=new BulkRequest();
        bulkRequest.timeout("20s");

        //批量处理请求
        for (ArticleContent articleContent :list){
            //不设置id会生成随机id
            bulkRequest.add(new IndexRequest("article")
                    .id(articleContent.getArticleId())
                    .source(JSONValue.toJSONString(articleContent),XContentType.JSON));
        }

        BulkResponse bulkResponse=restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
        System.out.println("批量上传是否失败："+bulkResponse.hasFailures());//是否执行失败,false为执行成功
    }

}
