package com.jt.service;

import com.jt.vo.ImageVO;
import org.hibernate.validator.cfg.defs.URLDef;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {

	/**
	 * 步骤：
	 * 		1 文件上传的根目录 D:/images-gsb
	 * 		2 校验文件类型 jpg|png|gif...
	 * 		3 为了检索快速，需要分目录存储 1 按照类型 2
	 * 		4 文件名称不能重复 UUID
	 * @param uploadFile
	 * @return
	 */
	@Value("${image.localDirPath}")
	private String localDirPath;// = "D:/images-gsb/";

	@Value("${image.urlPath}")
	private String urlPath;

	@Override
	public ImageVO uploadFile(MultipartFile uploadFile) {
		ImageVO imageVO = null;
		// 1 获取文件名称
		String fileName = uploadFile.getOriginalFilename();

		// 2 校验是否为图片类型
		fileName = fileName.toLowerCase(); // 将字符转化成小写
		if(!fileName.matches("^.+\\.(jpg|png|gif)$")) return ImageVO.fail(); // 不是图片

		// 3 校验程序为恶意程序， 检查图片是否有宽高
		try {
			BufferedImage bi = ImageIO.read(uploadFile.getInputStream());
			int height = bi.getHeight();
			int width = bi.getWidth();
			if(height == 0 || width == 0){
				return ImageVO.fail();
			}

			// 4 分目录保存
			String datePath = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
			String fileLocalPath = localDirPath + datePath;
			File file = new File(fileLocalPath);
			if(!file.exists()) file.mkdirs();

			// 5 准备文件名称 UUID.type
			String uuid = UUID.randomUUID().toString();
			int index = fileName.lastIndexOf(".");
			String type = fileName.substring(index);
			String uuidName = uuid + type;

			// 6 实现文件上传
			String realFilePath =fileLocalPath + uuidName;
			uploadFile.transferTo(new File(realFilePath));
			String url = urlPath + datePath +uuidName;
			imageVO = new ImageVO(0,url,width,height);

		}catch (Exception e){
			e.printStackTrace();
			return ImageVO.fail();
		}
		return imageVO;
	}
}
