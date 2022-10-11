package com.balls.util;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 *   发送短信验证码
 */
@Slf4j
@Component
@PropertySource(value = "classpath:config/sms.properties",encoding = "UTF-8")
public class SmsUtil {

    @Value("${sms.TemplateCode}")
    private String TemplateCode;//信息模板

    @Value("${sms.SignName}")
    private String SignName;//信息签名

    @Value("${sms.SecretId}")
    private String SecretId;//AccessID

    @Value("${sms.SecretKey}")
    private String SecretKey;//AccessKey

    @Value("${sms.SdkAppId}")
    private String SdkAppId;//短信应用ID

    @Autowired
    private RedisUtil redisUtil;  //redis工具类

    public boolean sendVerificationCode(String phoneNumbers){

        try {
            Credential cred = new Credential(SecretId, SecretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-guangzhou",clientProfile);
            SendSmsRequest req = new SendSmsRequest();

            req.setSmsSdkAppId(SdkAppId);// 短信应用ID:
            req.setSenderId("");// 国际/港澳台短信
            req.setSessionContext("xxx");//用户的 session 内容
            req.setExtendCode("");//短信号码扩展号

            req.setSignName(SignName);//短信签名内容
            req.setTemplateId(TemplateCode);//模板 ID

            //下发手机号码，采用 E.164 标准，+[国家或地区码][手机号]
            String phone="+86"+phoneNumbers;
            String[] phoneNumberSet = {phone};
            req.setPhoneNumberSet(phoneNumberSet);

            //生成4位随机纯数字验证码
            String code=String.valueOf((int)(Math.random()*10000));

            String[] templateParamSet = {code};//生成验证码参数9
            req.setTemplateParamSet(templateParamSet);
            SendSmsResponse res = client.SendSms(req);//发起请求

            //将手机号与生成的验证码存入redis，并设置5分钟后过期；
            redisUtil.set(phoneNumbers,code);
            redisUtil.expire(phoneNumbers,5,MINUTES);

            // 输出json格式的字符串回包
            log.info("手机号为：{}的用户发送的验证码为：{}", phoneNumbers,code);
            System.out.println(SendSmsResponse.toJsonString(res));

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }

        return true;
    }
}
