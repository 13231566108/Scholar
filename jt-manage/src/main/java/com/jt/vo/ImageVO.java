package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVO {
	private Integer error; // 0代表正常 1代表失败
	private String url;    // 图片虚拟路径
	private Integer width; // 图片宽度
	private Integer hight; // 图片高度

	public static ImageVO fail(){
		return new ImageVO(1,null,null,null);
	}

	public static ImageVO success(String url,Integer width,Integer hight){
		return new ImageVO(0,url,width,hight);
	}
}
