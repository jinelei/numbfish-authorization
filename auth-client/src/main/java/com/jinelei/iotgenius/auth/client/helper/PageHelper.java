package com.jinelei.iotgenius.auth.client.helper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jinelei.iotgenius.common.request.PageRequest;

@SuppressWarnings("unused")
public class PageHelper {

    public static <T, R> IPage<T> toPage(IPage<T> page, PageRequest<R> request) {
        page.setCurrent(request.getPage());
        page.setSize(request.getSize());
        return page;
    }

}
