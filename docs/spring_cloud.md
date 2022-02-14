# 提前准备工作

1. 必须是 Spring Boot 项目
1. 可以是 Spring Cloud 项目
2. 根据 Thrift IDL 生成对应的代码，这里不在赘述

# 服务端

1. 支持 `Apache Thrift` 的各种原生服务线程模型，包括单线程阻塞模型(`TSimpleServer`)、单线程非阻塞模型(`TNonblockingServer`)、线程池阻塞模型(`TThreadPoolServer`)
   、半同步半异步模型(`THsHaServer`) 和半同步半异步线程选择器模型(`TThreadedSelectorServer`)；
2. 使用 `@ThriftService` 注解即可完成 Thrift Handler 的注册管理。其中注解应该是 `Iface` 接口的实现类；

> 暂时只支持 Iface 不支持 AsyncIface

## 引入依赖

**Apache Maven**

```xml

<dependency>
  <groupId>io.github.bug-wheels</groupId>
  <artifactId>thrift-server-spring-boot-starter</artifactId>
  <version>0.2</version>
</dependency>
```

**Gradle Groovy DSL**

```groovy
implementation 'io.github.bug-wheels:thrift-server-spring-boot-starter:0.2'
```

**Gradle Kotlin DSL**

```Kotlin
implementation("io.github.bug-wheels:thrift-server-spring-boot-starter:0.2")
```

最新版本查看 [maven:thrift-server-spring-boot-starter](https://search.maven.org/artifact/io.github.bug-wheels/thrift-server-spring-boot-starter)

## 用法示例

1. 使用 `@ThriftService` 来标记 `Iface` 的实现类，其会自动注入到 Spring 容器中；
2. 可以像正常的 Spring 的 Bean 使用。

示例代码如下：

```java

@ThriftService("SharedService")  // 注意这里的名称，在 client 使用时需要和这里对应
public class SharedServiceImpl implements SharedService.Iface {

  @Autowired
  private RandomComponent randomComponent;

  @Override
  public SharedStruct getStruct(int key) {
    return new SharedStruct(key, randomComponent.randomString(key));
  }
}
```

## application 配置

| 参数                 | 类型  | 默认值 | 说明                                                                                                                                                |
|:-------------------|:----|:----|:--------------------------------------------------------------------------------------------------------------------------------------------------|
| thrift.server.port | 数字  | 10109    | Thrift 启动占用端口号                                                                                                                                    |
| thrift.server.serverMode | 字符串 | THsHaServer    | Thrift 线程模型，可选 TSimpleServer、TNonblockingServer、TThreadPoolServer、THsHaServer 和 TThreadedSelectorServer |

示例

```yaml
thrift:
  server:
    port: 10109
    serverMode: THsHaServer
```

在 Spring Cloud 项目中，在项目启动的过程中，会在注册中心的实例信息中增加元数据，元数据中包括 Thrift Server 启动的端口号。增加的元数据 key 如下:

| key                     | 说明                      |
| ----------------------- | ------------------------- |
| THRIFT_CLOUD_PORT       | Server 启动的 Thrift 端口 |
| PRESERVED_REGISTER_TIME | 服务注册的时间            |

可以在 Consul 或者 Nacos 的控制台查看该信息。

# 客户端

1. 支持 Apache Thrift 的各种客户端配置；
2. 在 Spring Boot 环境中，可以指定 Server 端的 ip + port 进行访问；
2. 在 Spring Cloud 环境中，可以通过服务名进行发现 Server 端的 ip + port；
3. 使用 `@ThriftClient` 注解即可完成 Thrift Client 的注册管理。其中注解应该是 `Iface` 接口的子接口，提供降级功能，用法类似 FeignClient

## 引入依赖

**Apache Maven**

```xml

<dependency>
  <groupId>io.github.bug-wheels</groupId>
  <artifactId>thrift-client-spring-boot-starter</artifactId>
  <version>0.2</version>
</dependency>
```

**Gradle Groovy DSL**

```groovy
implementation 'io.github.bug-wheels:thrift-client-spring-boot-starter:0.2'
```

**Gradle Kotlin DSL**

 ```kotlin
 implementation("io.github.bug-wheels:thrift-client-spring-boot-starter:0.2")
 ```

最新版本查看 [Maven: thrift-client-spring-boot-starter](https://search.maven.org/artifact/io.github.bug-wheels/thrift-client-spring-boot-starter)

## 用法示例

**示例代码**

```java
// 这里的 name 需要和服务端对应的 @ThriftService 相互对应
// serviceId 可以任意指定，主要用来在配置文件中指定 ip 使用，建议和 Server 端的 application name 对应
// 如果该 serviceId 由Spring Cloud 管理，必须配置成 Server 端的 application.name
@ThriftClient(name = "SharedService", serviceId = "spring-boot-thrift-server")
public interface SharedClient extends SharedService.Iface {

}
```

在 Application 启动类上增加注解 `@ThriftClientScan` 配置包扫描路径

```java

@ThriftClientScan("io.github.bw.example.client")
@SpringBootApplication
public class ThriftExampleAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(ThriftExampleAppApplication.class, args);
  }
}
```

## application 配置

| 参数                               | 类型        | 默认值 | 说明                                                         |
| :--------------------------------- | :---------- | :----- | :----------------------------------------------------------- |
| thrift.client.enabled              | 布尔        | True   | 是否启用 Thrift Client                                       |
| thrift.client.loadBalance.enabled  | 布尔        | True   | 是否启用自定义的服务发现功能（默认的服务发现功能基于 Spring Cloud），在 Spring Cloud 环境中需要设置为 True。 |
| thrift.client.loadBalance.services | ServiceNode | 无     | 手动指定服务的地址信息，优先使用该配置，如果不存在则从服务发现中查询 |

ServiceNode 说明

| 配置名  | 类型                      | 默认值 | 说明                                       |
| ------- | ------------------------- | ------ | ------------------------------------------ |
| name    | 字符串                    | 无     | ThriftClient 注解对应的 serviceId          |
| address | `List<String>` 字符串列表 | 无     | 该 Server 的所有地址信息，采用随机算法轮询 |

**注意：** `thrift.client.loadBalance.services` 可以不配置，如果配置了则首先使用这个配置。

示例

```yaml
thrift:
  client:
    loadBalance:
      enabled: false
      services:
        - name: spring-boot-thrift-server  # 和 ThriftClient 上的 serviceId 一致，手动指定其地址，可以指定多个，采用随机算法轮询
          address:
            - 127.0.0.1:10109
```

