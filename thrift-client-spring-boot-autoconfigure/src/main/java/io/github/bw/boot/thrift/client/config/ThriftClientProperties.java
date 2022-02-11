package io.github.bw.boot.thrift.client.config;

import java.time.Duration;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ToString
@Setter
@Getter
@ConfigurationProperties("thrift.client")
public class ThriftClientProperties {

  private ThriftClientLoadBalance loadBalance;

  private boolean enabled = true;

  private String basePackages = "";

  @Setter
  @Getter
  @ToString
  public static class ThriftClientLoadBalance {

    private boolean enabled = false;

    private List<ServiceNode> services;
  }

  public static class Pool {

    /**
     * Maximum number of "idle" connections in the pool. Use a negative value to indicate an unlimited number of idle
     * connections.
     */
    private int maxIdle = 8;

    /**
     * Target for the minimum number of idle connections to maintain in the pool. This setting only has an effect if
     * both it and time between eviction runs are positive.
     */
    private int minIdle = 0;

    /**
     * Maximum number of connections that can be allocated by the pool at a given time. Use a negative value for no
     * limit.
     */
    private int maxActive = 8;

    /**
     * Maximum amount of time a connection allocation should block before throwing an exception when the pool is
     * exhausted. Use a negative value to block indefinitely.
     */
    private Duration maxWait = Duration.ofMillis(-1);

    /**
     * Time between runs of the idle object evictor thread. When positive, the idle object evictor thread starts,
     * otherwise no idle object eviction is performed.
     */
    private Duration timeBetweenEvictionRuns;

    public int getMaxIdle() {
      return this.maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
      this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
      return this.minIdle;
    }

    public void setMinIdle(int minIdle) {
      this.minIdle = minIdle;
    }

    public int getMaxActive() {
      return this.maxActive;
    }

    public void setMaxActive(int maxActive) {
      this.maxActive = maxActive;
    }

    public Duration getMaxWait() {
      return this.maxWait;
    }

    public void setMaxWait(Duration maxWait) {
      this.maxWait = maxWait;
    }

    public Duration getTimeBetweenEvictionRuns() {
      return this.timeBetweenEvictionRuns;
    }

    public void setTimeBetweenEvictionRuns(Duration timeBetweenEvictionRuns) {
      this.timeBetweenEvictionRuns = timeBetweenEvictionRuns;
    }
  }

}
