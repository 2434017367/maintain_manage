package com.example.manage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @email 2434017367@qq.com
 * @author: zhy
 * @date: 2019/12/16
 * @time: 11:20
 */

@Data
@Component
@ConfigurationProperties(prefix = "config")
public class ConfigEntity {

    private String imagePath;

}
