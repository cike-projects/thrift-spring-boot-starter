package io.github.bw.boot.thrift.server.autoconfigure;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@ConditionalOnClass({ConsulRegistration.class})
public class ThriftCloudMetadataConsulAutoConfiguration {

  @Autowired
  private ConsulRegistration consulRegistration;

  @Autowired
  private ThriftServerProperties thriftServerProperties;

  @PostConstruct
  public void init() {
    if (consulRegistration != null) {
      final int port = thriftServerProperties.getPort();
      Map<String, String> meta = consulRegistration.getService().getMeta();
      if (meta == null) {
        meta = new HashMap<>();
      }
      meta.put(ThriftServerProperties.THRIFT_CLOUD_PORT, Integer.toString(port));
      meta.put("PRESERVED_REGISTER_TIME",
          new DateFormatter("yyyy-MM-dd HH:mm:ss").print(new Date(), Locale.SIMPLIFIED_CHINESE));
      consulRegistration.getService().setMeta(meta);

    }
  }
}
