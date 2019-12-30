package com.example.manage.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author zhy
 * @email 2434017367@qq.com
 * @date 2019-12-24 19:48:56
 */
@Data
@TableName("user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.INPUT)
	private Long id;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 头像
	 */
	private String headImage;

	/**
	 * 密码
	 */
	private String password;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 性别 0：女 1：男
	 */
	private Integer sex;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 住址
	 */
	private String address;
	/**
	 * 创建时间
	 */
	private Date createDate;

}
