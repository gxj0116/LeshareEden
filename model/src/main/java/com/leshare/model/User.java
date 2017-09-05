package com.leshare.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 作者：gxj on 2017/9/5 12:51
 * 邮箱：jun18735177413@sina.com
 *
 * TODO 用户表
 */
@Table(name = "Users", id = BaseColumns._ID)
public class User extends Model{
    @Column(name = "_id", unique = true)
    public long id;
    @Column
    public String name;
    @Column
    public String tel_no;
    @Column
    public int age;
    @Column
    public int gender;
    @Column
    public String address;

    public User() {
    }

    public User(long id, String name, String tel_no, int age, int gender, String address) {
        this.id = id;
        this.name = name;
        this.tel_no = tel_no;
        this.age = age;
        this.gender = gender;
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tel_no='" + tel_no + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                '}';
    }
}
