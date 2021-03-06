package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HtmlGenController {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception{
//		1、从Spring容器获取FreeMarkerConfigurer对象
//		2、从freeMarkerConfigurer获取configration对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
//		3、使用configuration对象获得Template对象
		Template template = configuration.getTemplate("hello.ftl");
//		4、创建数据集
		Map dataModel = new HashMap<>();
		dataModel.put("hello", "1000");
//		5、创建输出文件的writer对象
		Writer out = new FileWriter(new File("/Users/medxing"));
//		6、调用模板对象的process方法，生成文件
		template.process(dataModel, out);
		
		out.close();
		return "OK";
	}
}
