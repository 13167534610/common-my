package com.common;

import com.common.funciton.SysCacheUtil;
import com.common.init.InitOne;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/26 9:22
 */
@SpringBootApplication
public class CommonMyApplication {

    public static void main(String[] args) throws MalformedURLException {
        //SpringApplication.run(CommonMyApplication.class, args);
        SpringApplication application = new SpringApplication(CommonMyApplication.class);
        application.addInitializers(new InitOne());
        application.setBannerMode(Banner.Mode.OFF);
        /*ClassLoader classLoader = CommonMyApplication.class.getClassLoader();
        URL url = classLoader.getResource("static/img/test.jsp");
        ImageBanner imageBanner = new ImageBanner(new UrlResource(url));
        application.setBanner(imageBanner);*/
        application.run(args);

    }

    @PostConstruct
    public void cacheInit(){
        SysCacheUtil.setCache("name", "zhangsan");

        System.out.println("========================postconstruct==================================");
    }
}
