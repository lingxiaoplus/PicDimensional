package com.lingxiaosuse.picture.tudimension.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
@Table(database = AppDatabase.class)
public class NetCacheModel extends BaseModel {
    @PrimaryKey(autoincrement = true)
    public long id;
    @Column
    public String homeData;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHomeData() {
        return homeData;
    }

    public void setHomeData(String homeData) {
        this.homeData = homeData;
    }
}
