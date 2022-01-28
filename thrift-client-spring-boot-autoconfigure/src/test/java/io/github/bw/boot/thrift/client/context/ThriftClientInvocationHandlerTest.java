package io.github.bw.boot.thrift.client.context;

import io.github.bw.boot.thrift.client.ThriftClient;
import io.github.bw.boot.thrift.client.context.demo.SharedClient;
import io.github.bw.example.shared.SharedStruct;
import java.lang.reflect.Type;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotationUtils;

@Slf4j
class ThriftClientInvocationHandlerTest {

  @Test
  void test() throws Exception {

    Class<?> beanClass = SharedClient.class;

    ThriftClient thriftClient = AnnotationUtils.findAnnotation(beanClass, ThriftClient.class);

    ThriftClientFactoryBean<SharedClient> factoryBean = new ThriftClientFactoryBean<>(
        thriftClient.serviceId(), thriftClient.name(), beanClass);

    SharedClient client = factoryBean.getObject();
    Assertions.assertNotNull(client);
    SharedStruct result = client.getStruct(2);
    Assertions.assertNotNull(result);

  }

  @Test
  void testObtainClientClass() throws Exception {
    Class<?> beanClass = SharedClient.class;
    Type iFaceType = Stream.of(beanClass.getGenericInterfaces())
        .filter(clazz -> ((Class<?>) clazz).getName().endsWith("$Iface"))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No thrift IFace found on implementation"));
    log.info("{}", iFaceType.getTypeName());
    log.info("{}", iFaceType.getTypeName().replace("$Iface", "$Client"));

    Class<?> clientClass = Class.forName(iFaceType.getTypeName().replace("$Iface", "$Client"));
    log.info("{}", clientClass);

//    TTransport transport = new TFramedTransport(new TSocket("localhost", PORT));
//    TProtocol protocol = new TCompactProtocol(transport);
//
//    TMultiplexedProtocol sharedProtocol = new TMultiplexedProtocol(protocol, "SharedService");
//    SharedService.Client client = new SharedService.Client(sharedProtocol);
//
//    try {
//      transport.open();

  }

}