package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Louis on 26/4/16.
 */
public class Query implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("query_id")           String id;
  @SerializedName("user_id")            String userId;
  @SerializedName("create_timestamp")   Date createTimestamp;
  @SerializedName("query")              String query;

  public Query() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Date getCreateTimestamp() {
    return createTimestamp;
  }

  public void setCreateTimestamp(Date createTimestamp) {
    this.createTimestamp = createTimestamp;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  @Override
  public String toString() {
    return "Query{" +
        "id='" + id + '\'' +
        ", userId='" + userId + '\'' +
        ", createTimestamp=" + createTimestamp +
        ", query='" + query + '\'' +
        '}';
  }
}
