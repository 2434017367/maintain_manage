package com.example.manage.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author: cjl
 *
 * @date 2019-12-27 10:12:51
 */
@Data
@TableName("employees")
public class EmployeesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 工号
	 */
	private String workNumber;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 头像
	 */
	private String headImage;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 等级 0：普通员工 1：管理员
	 */
	private Integer level;
	/**
	 * 创建时间
	 */
	private Date createDate;

}
