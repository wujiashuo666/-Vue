package org.javaboy.vhr.model;

import java.io.Serializable;

public class Meta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean keepAlive;

    private Boolean requireAuth;

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Boolean getRequireAuth() {
        return requireAuth;
    }

    public void setRequireAuth(Boolean requireAuth) {
        this.requireAuth = requireAuth;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "keepAlive=" + keepAlive +
                "requireAuth=" + requireAuth +
                '}';
    }
}
