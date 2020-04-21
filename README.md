#### 简介
        这是一个最简洁SpringCloud项目主要是为了方便运维人员做测试的，组件、功能齐备，可直接使用，具体使用方法如下；
###### 使用方法
```bash
# 项目端口
eureka(6210)、zuul(6220)、feign(6230)、ribbon(6240)

# 每个项目的编译，编程成功之后会在target生成jar
~]$ mvn clean install -Dmaven.test.skip=true compile package spring-boot:repackage

# 运行项目
~]$ java -jar project_name.jar

# 测试项目是否运行正常
~]$ curl http://${project_host_ip}:${project_host_port}/check

# 通过zuul来调用项目，主要是为了测试zuul是否能调通后端的项目
~]$ curl http://${zuul_host_ip}:${zuul_host_port}/zcheck

# 通过eureka来调用项目，因为eureka是一个注册中心，所有的项目都会在eureka上注册自己的地址，eureka里面有每个子项目的连接信息，所以这里主要是测试是否能够直接通过zuul拿到服务的地址并且调通，该项目是使用feign到eureka里面去拿ribbon项目的地址(并且请求该地址拿到返回值并且返回)，然后ribbon会从eureka里面去拿feign的返回值,所以最终的流程应该是这样的feign->eureka->ribbon->eureka->feign，这里所以我们在请求这个feign接口的时候，最终返回的其实是feign自己中的一个接口的返回值
~]$ curl http://${feign_host_ip}:${feign_host_port}/echeck
```
