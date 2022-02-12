package io.github.bw.boot.thrift.client.context;

import io.github.bw.boot.thrift.client.ThriftClientHolder;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CollectionUtils;

@Slf4j
public class ThriftPostListener implements ApplicationListener<ApplicationEvent> {

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    if (event instanceof ApplicationStartedEvent) {
      log.info("Spring 项目启动完成，开始检测 Thrift 后置处理");
      List<ThriftPostProcessor> needPostProcessors = ThriftClientHolder.getNeedPostProcessor();

      if (CollectionUtils.isEmpty(needPostProcessors)) {
        log.info("未检测到 Thrift 后置处理，Thrift 启动完成");
      } else {
        log.info("检测到 Thrift 后置处理，开始执行");
        for (ThriftPostProcessor processor : needPostProcessors) {
          // execute 可以抛出异常，如果使用其他方法遍历，要保证异常可以抛出去
          processor.execute();
        }
      }
    }
  }
}
