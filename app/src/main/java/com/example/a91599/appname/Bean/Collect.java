package com.example.a91599.appname.Bean;

public class Collect<T> {
    int id;
    int user_id;
    int iob_id;
    long createtime;
    long updatetime;
    private T job;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIob_id() {
        return iob_id;
    }

    public void setIob_id(int iob_id) {
        this.iob_id = iob_id;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public T getData() {
        return job;
    }

    public void setData(T job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "Collect{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", iob_id=" + iob_id +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                ", job=" + job +
                '}';
    }
}
