package com.ao.demo.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;



@Controller
public class MyRibbonController{
	@Autowired
	private RestTemplate restTemplate;
	
	private static final String REST_URL_PREFIX = "http://FEIGN";
	
	
	@RequestMapping(value = "requestribbon")
	@ResponseBody
	public String requestribbon(){
		String message = restTemplate.getForObject(REST_URL_PREFIX+"/zuulcheck",String.class );
		return message;
	}

	@RequestMapping(value = "zcheck")
	@ResponseBody
	public String zcheck(){
//		String messager = restTemplate.getForObject(REST_URL_PREFIX+"/fegin/test",String.class );
		return "Ribbon Health OK from zuul";
	}

	public String fallback(){
		return "异常熔断";
	}
	
	@RequestMapping(value = "check")
	@ResponseBody
	public String check(){
		return "Ribbon Health OK";
	}
}