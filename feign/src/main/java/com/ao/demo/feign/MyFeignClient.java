package com.ao.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value="ribbon")
public interface MyFeignClient {
	@GetMapping("requestribbon")
	String requestribbon();
}
