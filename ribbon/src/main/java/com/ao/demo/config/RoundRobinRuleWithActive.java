package com.ao.demo.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

/**
 * <p>
 * Title: 轮询策略，之调用存活的服务
 * </p>
 * 
 * @author Caihy
 * @date 2019年7月25日 上午11:32:51
 */
public class RoundRobinRuleWithActive extends AbstractLoadBalancerRule {

	private AtomicInteger nextServerCyclicCounter;
	//    private static final boolean AVAILABLE_ONLY_SERVERS = true;
	//    private static final boolean ALL_SERVERS = false;

	private static Logger log = LoggerFactory.getLogger(RoundRobinRuleWithActive.class);

	public RoundRobinRuleWithActive() {
		nextServerCyclicCounter = new AtomicInteger(0);
	}

	public RoundRobinRuleWithActive(ILoadBalancer lb) {
		this();
		setLoadBalancer(lb);
	}

	public Server choose(ILoadBalancer lb, Object key) {
		if (lb == null) {
			log.warn("no load balancer");
			return null;
		}

		Server server = null;
		int count = 0;
		while (server == null && count++ < 10) {
			//获取eureka上存活的服务
			List<Server> reachableServers = lb.getReachableServers();
			//获取eureka上所有的服务
			List<Server> allServers = lb.getAllServers();
			int upCount = reachableServers.size();

			int serverCount = allServers.size();
			int activeServerCount = allServers.size();

			if ((upCount == 0) || (serverCount == 0)) {
				log.warn("No up servers available from load balancer: " + lb);
				return null;
			}
			//获取本机注册的服务
			server = getLocalServer(reachableServers);
			if (server == null) {
				//轮询算法
				int nextServerIndex = incrementAndGetModulo(activeServerCount);
				server = reachableServers.get(nextServerIndex);
			}

			if (server == null) {
				/* Transient. */
				Thread.yield();
				continue;
			}

			if (server.isAlive() && (server.isReadyToServe())) {
				return (server);
			}

			// Next.
			server = null;
		}

		if (count >= 10) {
			log.warn("No available alive servers after 10 tries from load balancer: " + lb);
		}
		return server;
	}

	/**
	 * 轮询策略算法
	 * 
	 * @author Caihy
	 * @version 创建时间：2019年7月25日 上午11:34:57
	 * @param modulo
	 * @return
	 */
	private int incrementAndGetModulo(int modulo) {
		for (;;) {
			int current = nextServerCyclicCounter.get();
			int next = (current + 1) % modulo;
			if (nextServerCyclicCounter.compareAndSet(current, next))
				return next;
		}
	}

	@Override
	public Server choose(Object key) {
		return choose(getLoadBalancer(), key);
	}

	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {
	}

	@SuppressWarnings("unused")
	private Server getLocalServer(List<Server> list) {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("IP地址：" + addr);
		 String ip = addr.getHostAddress().toString(); //获取本机ip  
		 String hostName = addr.getHostName().toString(); //获取本机计算机名称  
		//获取本机注册的Server"192.168.1.254"
		for (Server ser : list) {
			if (ser.getHost().equals(hostName)) {
				return ser;
			}
		}
		return null;
	}
}
