package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Louis on 20/4/16.
 */
public class Role {
    @SerializedName("role_id")      private String roleId;
    @SerializedName("name")         private String name;

    public Role() {
    }

    public Role(String roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
