package com.balls.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@PropertySource(value = "classpath:config/oss.properties",encoding = "UTF-8")
public class OssUtil {
    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String type) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        InputStream inputStream;
        try {
            // 上传文件流
            inputStream = file.getInputStream();

            //为了使得文件可以重复上传，每次上传的时候需要将文件名进行修改
            String fileName = file.getOriginalFilename();
            log.info("图片上传的名字为：{}", fileName);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String newFileName = uuid + fileName;

            //获取当前日期,然后以日期和新的文件名组成全路径，使得oss中的文件按照日期进行分类存储
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String date = simpleDateFormat.format(new Date());

            String fullFileName = type + "/" + date + "/" + newFileName;
            log.info("图片保存在oss的全路径为：{}", fullFileName);

            //第一个参数Bucket名称 第二个参数 上传到oss文件路径和文件名称
            ossClient.putObject(bucketName, fullFileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            return "https://" + bucketName + "." + endpoint + "/" + fullFileName;
        } catch (Exception e) {
            log.info("文件上传失败");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }
}
