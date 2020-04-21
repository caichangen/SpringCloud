package com.ao.demo.filter;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

// @Component("MyZuulFilter")
public class MyZuulFilter extends ZuulFilter {

	@Override
	public Object run() {   //过滤器的具体逻辑。需要注意，这里我们通过ctx.setSendZuulResponse(false)令zuul过滤该请求，不对其进行路由，然后通过ctx.setResponseStatusCode(401)设置了其返回的错误码
		RequestContext ctx = RequestContext.getCurrentContext();
		if (ctx == null) {
			System.out.println("无法读取上下文，过滤失败！");
			return null;
		}
		HttpServletRequest request = ctx.getRequest();
		if (request == null) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(400);
			ctx.setResponseBody("Bad Request");
			System.out.println("无法读取请求对象，过滤失败！");
			return null;
		}
		// 获取请求头中的访问令牌
		String uid = request.getParameter("uid");
		if (StringUtils.isNotBlank(uid)) {
				ctx.setSendZuulResponse(true);
				ctx.setResponseStatusCode(200);
				return null;
			}
		
		ctx.setSendZuulResponse(false);
		ctx.setResponseStatusCode(406);
		ctx.setResponseBody("Not Acceptable");
		System.out.println("无法读取访问令牌，请求不被接受！");
		return null;
	}

	@Override
	public String filterType() { 
		// 可以在请求被路由之前调用
		return FilterConstants.PRE_TYPE;
	}
	/**
	 * pre：可以在请求被路由之前调用
	   route：在路由请求时候被调用
	   post：在route和error过滤器之后被调用
	   error：处理请求时发生错误时被调用
	 */

	@Override
	public int filterOrder() {
		
		return 1;// filter执行顺序，通过数字指定 ,优先级为0，数字越大，优先级越低
	}

	@Override
	public boolean shouldFilter() {  //返回一个boolean类型来判断该过滤器是否要执行，所以通过此函数可实现过滤器的开关。在上例中，我们直接返回true
		
		return true;
	}

}
