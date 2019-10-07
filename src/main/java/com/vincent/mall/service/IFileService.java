package com.vincent.mall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Vincent
 * @created: 2019/10/7  20:32
 * @description:文件service
 */
public interface IFileService {

    /**
     * 上传文件
     *
     * @param file
     * @param path
     * @return
     */
    String upload(MultipartFile file, String path);
}
