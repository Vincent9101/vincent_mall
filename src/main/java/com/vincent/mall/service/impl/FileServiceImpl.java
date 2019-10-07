package com.vincent.mall.service.impl;

import com.vincent.mall.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author: Vincent
 * @created: 2019/10/7  20:33
 * @description:文件service实现
 */
@Service
public class FileServiceImpl implements IFileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String extensionName = fileName.substring(fileName.lastIndexOf("."));
        String uploadFileName = UUID.randomUUID().toString() + extensionName;
        logger.info("开始上传文件，文件名：【{}】，上传路径为:【{}】,新文件名为:【{}】",
                fileName,
                path,
                uploadFileName);

        File dir = new File(path);
        if (!dir.exists()) {
            dir.setWritable(true);
            dir.mkdirs();
        }

        File uploadFile = new File(path, uploadFileName);
        try {
            //上传到tomcat服务器
            file.transferTo(uploadFile);
            // TODO:上传到FTP服务器 上传结束之后删除tomcat文件
//            FTPUtil.uploadFile(Lists.newArrayList(uploadFile));
            // uploadFile.delete();
        } catch (IOException e) {
            logger.error("上传文件失败，错误信息：" + e.getMessage(), e);
        }
        return uploadFile.getName();
    }
}

