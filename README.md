# Thrift Spring Boot Starter

Spring Boot starter module for Thrift framework.

## 简介

本项目主要为了提高 Thrift 在 Spring Boot 和 Spring Cloud 中的使用体验，简化其配置方式，提供开箱即用的体验，在 Spring Cloud 的环境中提高注册发现和负载均衡功能。个模块。

**服务端：**

1. 支持 `Apache Thrift` 的各种原生服务线程模型，包括单线程阻塞模型(`TSimpleServer`)、单线程非阻塞模型(`TNonblockingServer`)、线程池阻塞模型(`TThreadPoolServer`)
   、半同步半异步模型(`THsHaServer`)
   和半同步半异步线程选择器模型(`TThreadedSelectorServer`)。

**客户端：**

1.

## 版本支持

| Thrift Starter | Spring Boot | Spring Cloud | Apache Thrift |
| -------------- | ----------- | ------------ | ------------- |
|                |             |              |               |
|                |             |              |               |
|                |             |              |               |

**未列出不代表不支持，可以自行尝试**

## 快速开始

> 以下示例未做说明均在 Spring Boot 项目中，而非单纯的 Spring

### Thrift IDL

这是使用官方示例代码中的两个 IDL 文件, 为了简化，这里删除了注释和版权声明部分

**shared.thrift**

```
namespace java com.github.sbb.example.shared

struct SharedStruct {
  1: i32 key
  2: string value
}

service SharedService {
  SharedStruct getStruct(1: i32 key)
}
```

**tutorial.thrift**

```
include "shared.thrift"

namespace java com.github.sbb.example.tutorial

typedef i32 MyInteger

const i32 INT32CONSTANT = 9853
const map<string,string> MAPCONSTANT = {'hello':'world', 'goodnight':'moon'}

enum Operation {
  ADD = 1,
  SUBTRACT = 2,
  MULTIPLY = 3,
  DIVIDE = 4
}

struct Work {
  1: i32 num1 = 0,
  2: i32 num2,
  3: Operation op,
  4: optional string comment,
}

exception InvalidOperation {
  1: i32 whatOp,
  2: string why
}

service Calculator extends shared.SharedService {

   void ping(),

   i32 add(1:i32 num1, 2:i32 num2),

   i32 calculate(1:i32 logid, 2:Work w) throws (1:InvalidOperation ouch),

   oneway void zip()

}
```

### 服务端程序：

在 `pom.xml` 文件中引入服务端依赖 `thrift-server-spring-boot-starter`：

```xml

<dependency>
  <groupId>com.github.some-big-bugs</groupId>
  <artifactId>thrift-server-spring-boot-starter</artifactId>
</dependency>
```

application.yml

```yaml
server:
  port: 9999

thrift: # thrift 的相关配置，可以不配
  server:
    port: 10109  # thrift 服务的启动端口，默认值是 10109
    serverMode: THsHaServer  # thrift 的服务线程模型，默认 THsHaServer，可选 THsHaServer  TNonblockingServer  TSimpleServer   TThreadPoolServer TThreadedSelectorServer
```

Application.java

```java

@SpringBootApplication
public class ThriftExampleAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(ThriftExampleAppApplication.class, args);
  }
}
```

实现 `Thrift IDL` 生成的 `SharedService` 中的 `Iface` 接口：

SharedServiceImpl.java

```java
package com.github.sbb.example.service;

import com.github.sbb.boot.thrift.server.ThriftService;
import com.github.sbb.example.component.RandomComponent;
import com.github.sbb.example.shared.SharedService;
import com.github.sbb.example.shared.SharedStruct;
import org.springframework.beans.factory.annotation.Autowired;

@ThriftService("SharedService") // 这里需要起个别名，Client 调用时，需要指定这个名字，如果不设置名字，则默认使用 classname
public class SharedServiceImpl implements SharedService.Iface {

  @Autowired
  private RandomComponent randomComponent;

  @Override
  public SharedStruct getStruct(int key) {
    return new SharedStruct(key, randomComponent.randomString(key));
  }
}
```

启动项目：

```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.3.7.RELEASE)

2022-01-22 16:11:50,493 DEBUG [main] com.github.sbb.example.ThriftExampleAppApplication [StartupInfoLogger.java:56] Running with Spring Boot v2.3.7.RELEASE, Spring v5.2.12.RELEASE
2022-01-22 16:11:50,493 INFO [main] com.github.sbb.example.ThriftExampleAppApplication [SpringApplication.java:651] No active profile set, falling back to default profiles: default
2022-01-22 16:11:51,264 INFO [main] org.apache.coyote.http11.Http11NioProtocol [DirectJDKLog.java:173] Initializing ProtocolHandler ["http-nio-9999"]
2022-01-22 16:11:51,265 INFO [main] org.apache.catalina.core.StandardService [DirectJDKLog.java:173] Starting service [Tomcat]
2022-01-22 16:11:51,265 INFO [main] org.apache.catalina.core.StandardEngine [DirectJDKLog.java:173] Starting Servlet engine: [Apache Tomcat/9.0.41]
2022-01-22 16:11:51,324 INFO [main] org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/] [DirectJDKLog.java:173] Initializing Spring embedded WebApplicationContext
2022-01-22 16:11:51,596 INFO [main] org.apache.coyote.http11.Http11NioProtocol [DirectJDKLog.java:173] Starting ProtocolHandler ["http-nio-9999"]
2022-01-22 16:11:51,632 INFO [main] com.github.sbb.boot.thrift.server.server.THsHaThriftServer [THsHaThriftServer.java:42] Thrift Starting ["THsHaServer-10109"]
2022-01-22 16:11:51,642 INFO [main] com.github.sbb.example.ThriftExampleAppApplication [StartupInfoLogger.java:61] Started ThriftExampleAppApplication in 1.522 seconds (JVM running for 2.022)
```

日志中也可以显示 Thrift 启动的线程模型和端口号

### 客户端程序：


