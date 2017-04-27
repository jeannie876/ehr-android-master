package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Louis on 3/6/2016.
 */
public class ServerStatus implements Serializable {
  //{"app":"0.7.0","node":null,"npm":null,"port":33274,"running_for":{"year":0,"week":0,"day":0,"hour":0,"minute":21,"second":51,"millisecond":760}}
  @SerializedName("app") String appVersion;
  @SerializedName("node") String nodeVersion;
  @SerializedName("npm") String npmVersion;
  @SerializedName("port") Integer port;
  @SerializedName("running_for") ServerRunningFor runningFor;

  public ServerStatus() {
  }

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }

  public String getNodeVersion() {
    return nodeVersion;
  }

  public void setNodeVersion(String nodeVersion) {
    this.nodeVersion = nodeVersion;
  }

  public String getNpmVersion() {
    return npmVersion;
  }

  public void setNpmVersion(String npmVersion) {
    this.npmVersion = npmVersion;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public ServerRunningFor getRunningFor() {
    return runningFor;
  }

  public void setRunningFor(ServerRunningFor runningFor) {
    this.runningFor = runningFor;
  }

  @Override
  public String toString() {
    return "ServerStatus{" +
        "appVersion='" + appVersion + '\'' +
        ", nodeVersion='" + nodeVersion + '\'' +
        ", npmVersion='" + npmVersion + '\'' +
        ", port=" + port +
        ", runningFor=" + runningFor +
        '}';
  }
}
