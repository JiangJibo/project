/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.mvc.controller;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @since 2017年3月6日 下午4:51:16
 * @version $Id$
 * @author JiangJibo
 *
 */
@RestController
@RequestMapping("/files")
public class FileExtendsionController {

	/**
	 * 测试文件上传的单元测试用例
	 * 
	 * @param file
	 * @throws IOException
	 */
	@RequestMapping(value = "/uploading", method = RequestMethod.POST)
	public void upload(@RequestParam("path") MultipartFile file) throws IOException {
		for (String line : IOUtils.readLines(file.getInputStream())) {
			System.out.println("\t" + line);
		}
	}

	/**
	 * 设置文件打开的形式,是否为直接在浏览器中打开,还是生成一个下载框供下载
	 * 
	 * @param response
	 * @param downloadType
	 * @throws IOException
	 */
	@RequestMapping(value = "/downloading/{downloadType}", method = RequestMethod.GET)
	public void download(HttpServletResponse response, @PathVariable int downloadType) throws IOException {
		String type;
		switch (downloadType) {
		case 0:
			type = "inline "; // 直接在浏览器中打开
			break;
		case 1:
			type = "attachment"; // 生成一个下载框
			break;
		default:
			throw new IllegalArgumentException("文件的下载类型不能为:[" + downloadType + "]");
		}
		Resource resource = new ClassPathResource("com/bob/images/小狗.jpg");
		String fileName = new String(resource.getFilename().getBytes(), Charset.forName("ISO-8859-1"));
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, type + ";filename=" + fileName);
		response.getOutputStream().write(FileUtils.readFileToByteArray(resource.getFile()));
	}

}
