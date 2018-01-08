package com.igrs.beacon.model.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by jianw on 17-12-3.
 */
@Entity
public class UUIDBean {
    @Id
    public Long id;

    public String name;

    public String uuid;

    @Generated(hash = 1445717030)
    public UUIDBean(Long id, String name, String uuid) {
        this.id = id;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
