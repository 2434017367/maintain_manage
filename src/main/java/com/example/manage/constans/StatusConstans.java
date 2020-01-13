package com.example.manage.constans;

/**
 *
 * @author: gyh
 * @date: 2019/9/29
 * @time: 15:36
 */
public interface StatusConstans {

    /**
     * 提单
     */
    final Integer SUBMIT = 0;

    /**
     * 派单
     */
    final Integer ALLOT = 1;

    /**
     * 接单
     */
    final Integer ACCEPT = 2;

    /**
     * 取消订单
     */
    final Integer CANCEL = 3;

    /**
     * 完成订单
     */
    final Integer COMPLETE = 4;

    /**
     * 男
     */
    final Integer MAN = 1;

    /**
     * 女
     */
    final Integer FEMALE = 0;

    /**
     * 普通员工
     */
    final Integer EMP = 0;

    /**
     * 管理员
     */
    final Integer ADMIN = 1;


    /**
     * 员工的一些状态
     */
    /**
     * 正常
     */
    final Integer EMP_NORMAL = 0;

    /**
     * 禁止
     */
    final Integer EMP_FORBID = 1;


    /**
     * 调休
     */
    final Integer EMP_REST = 2;


}
