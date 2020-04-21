package com.ao.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class MyZuulController{
	@RequestMapping(value = "check")
	@ResponseBody
	public String check(){
		return "Zuul Health OK";
	}
}