package com.example.manage.utils;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 *
 * @author: cjl
 * @date: 2019/11/22
 * @time: 18:13
 */
public class PageUtils {


    /**
     * 获取分页对象
     * @param pageStr
     * @param limitStr
     * @return
     */
    public static Page getPage(String pageStr, String limitStr){

        int pageNum = 0;
        int limit = 10;

        if (CommUtils.isNotEmpty(pageStr)){
            pageNum = Integer.parseInt(pageStr);
        }

        if (CommUtils.isNotEmpty(limitStr)){
            limit = Integer.parseInt(limitStr);
        }

        Page page = new Page(pageNum, limit);

        return page;
    }

    public static Page getPage(Map<String, Object> param){
        String page = String.valueOf(param.get("page"));
        String limit = String.valueOf(param.get("limit"));
        return getPage(page, limit);
    }

    public static R getPageResult(IPage iPage){
        R ok = R.ok();
        ok.put("data", iPage.getRecords());
        ok.put("count", iPage.getTotal());

        return ok;
    }

}
