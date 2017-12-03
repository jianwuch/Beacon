package com.igrs.beacon.moudle.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by jianw on 17-12-3.
 */
@Entity
public class UUIDBean {
    public String name;

    @Unique
    public String uuid;

    @Generated(hash = 293200863)
    public UUIDBean(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    @Generated(hash = 580629344)
    public UUIDBean() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
