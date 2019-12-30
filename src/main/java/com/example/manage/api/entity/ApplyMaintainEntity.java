package com.example.manage.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("apply_maintain")
public class ApplyMaintainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.INPUT)
	private Long id;

	/**
	 * 订单号
	 */
	private String orderNumber;
	/**
	 * 申请人
	 */
	private String name;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 性别 0：女 1：男
	 */
	private Integer sex;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 员工id
	 */
	private Long employeeId;
	/**
	 * 状态 0：提单 1：派单 2：接单 3：取消订单 4：完成
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 完成时间
	 */
	private Date endDate;

	@TableField(exist = false)
	private EmployeesEntity employees;

}
