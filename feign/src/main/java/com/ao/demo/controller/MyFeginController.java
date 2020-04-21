package com.ao.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.ao.demo.feign.MyFeignClient;



@Controller
public class MyFeginController{
	@SuppressWarnings("unused")
	@Autowired
	private RestTemplate restTemplate;
	
	
	@SuppressWarnings("unused")
	private static final String REST_URL_PREFIX = "http://FEIGN";
	@Autowired
	private MyFeignClient myFeignClient;

	@RequestMapping(value = "check")
	@ResponseBody
	public String check(){
//		String messager = restTemplate.getForObject(REST_URL_PREFIX+"/fegin/test",String.class );
		return "Fegin Health OK";
	}

	@RequestMapping(value = "zuulcheck")
	@ResponseBody
	public String zuulcheck(){
//		String messager = restTemplate.getForObject(REST_URL_PREFIX+"/fegin/test",String.class );
		return "fegin->eureka->ribbon->eureka->fegin";
	}

	@RequestMapping(value = "zcheck")
	@ResponseBody
	public String zcheck(){
//		String messager = restTemplate.getForObject(REST_URL_PREFIX+"/fegin/test",String.class );
		return "Feign Health OK from zuul";
	}

	@RequestMapping(value = "echeck")
	@ResponseBody
	public String echeck(){
		return myFeignClient.requestribbon();
	}
}