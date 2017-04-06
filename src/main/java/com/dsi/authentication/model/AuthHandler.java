package com.dsi.authentication.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by sabbir on 6/9/16.
 */

@Entity
@Table(name = "ref_auth_handler")
public class AuthHandler {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "auth_handler_id", length = 40)
    private String authHandlerId;

    @Column(length = 50)
    private String name;

    @Column(name = "type_impl", nullable = false, length = 100)
    private String typeImpl;

    private int version;

    public String getAuthHandlerId() {
        return authHandlerId;
    }

    public void setAuthHandlerId(String authHandlerId) {
        this.authHandlerId = authHandlerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeImpl() {
        return typeImpl;
    }

    public void setTypeImpl(String typeImpl) {
        this.typeImpl = typeImpl;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
