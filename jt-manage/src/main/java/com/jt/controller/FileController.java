package com.jt.controller;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class FileController {

	/**
	 * 要求：实现图片上传，返回上传成功信息
	 * 规范：参数名称必须与页面属性名称一致
	 * 步骤：
	 * 		1 确定文件上传路径
	 * 		2 校验文件路径是否正确
	 * 	 	3 实现文件的上传
	 */
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws IOException {
		String fileDir = "D:/images-gsb";
		File file = new File(fileDir);
		if(!file.exists()){
			file.mkdirs();
		}
		String fileName = fileImage.getOriginalFilename();
		String path = "D:/images-gsb/"+fileName;
		fileImage.transferTo(new File(path));
		return null;
	}

	@Autowired
	private FileService fileService;

	@RequestMapping("/pic/upload")
	public ImageVO uploadFile(MultipartFile uploadFile){
		return fileService.uploadFile(uploadFile);
	}
}
