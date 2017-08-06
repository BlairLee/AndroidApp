package com.jiuzhang.guojing.awesomeresume.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jiuzhang.guojing.awesomeresume.util.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

// type alt + enter: to implements Parcelable
public class Education implements Parcelable {

    public String id;

    public String school;

    public String major;

    public Date startDate;

    public Date endDate;

    public List<String> courses;

    // 允许声明一个新的 Education Object
    public Education() {
        // UUID: 生成一个专门的唯一的 id：
        id = UUID.randomUUID().toString();
    }

    // 顺序要跟 writeToParcel 存储时的顺序一样！！
    protected Education(Parcel in) {
        id = in.readString();
        school = in.readString();
        major = in.readString();
        courses = in.createStringArrayList();
        startDate = DateUtils.stringToDate(in.readString());
        endDate = DateUtils.stringToDate(in.readString());
    }


    // 打散的过程：装着纯数据的容器
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(school);
        dest.writeString(major);
        dest.writeStringList(courses);
        // 手动添加 Date 信息：
        dest.writeString(DateUtils.dateToString(startDate));
        dest.writeString(DateUtils.dateToString(endDate));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Education> CREATOR = new Creator<Education>() {
        @Override
        public Education createFromParcel(Parcel in) {
            return new Education(in);
        }

        @Override
        public Education[] newArray(int size) {
            return new Education[size];
        }
    };
}
