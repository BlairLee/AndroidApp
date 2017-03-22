package com.jiuzhang.guojing.awesomeresume;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jiuzhang.guojing.awesomeresume.model.BasicInfo;
import com.jiuzhang.guojing.awesomeresume.model.Education;
import com.jiuzhang.guojing.awesomeresume.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {

    private BasicInfo basicInfo;
    private Education education;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fakeData();
        setupUI();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

        setupBasicInfoUI();
        setupEducationUI();
    }

    private void setupBasicInfoUI() {
        ((TextView) findViewById(R.id.name)).setText(basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(basicInfo.email);
    }

    private void setupEducationUI() {
        // TODO 3: Display the education data onto the UI
        // Follow the example in setupBasicInfoUI
        // You will probably find formatItems method useful when displaying the courses
        String dataString = DateUtils.dateToString(education.startDate) + " ~ " + DateUtils.dateToString(education.endDate);
        ((TextView) findViewById(R.id.education_school)).setText(education.school + " (" + dataString + ")");
            ((TextView) findViewById(R.id.education_courses)).setText(formatItems(education.courses));
    }

    private void fakeData() {
        basicInfo = new BasicInfo();
        basicInfo.name = "Jing Guo";
        basicInfo.email = "guojing@jiuzhang.com";

        education = new Education();
        education.school = "THU";
        education.major = "Computer Science";
        education.startDate = DateUtils.stringToDate("09/2013");

        // TODO 1: Set the endDate
        // Follow the above example for startDate
        // DateUtils is a class written by ourselves, check out util/DateUtils
        education.endDate = DateUtils.stringToDate("08/2014");
        // TODO 2: Add some fake courses in education1.courses
        education.courses = new ArrayList<>();
        education.courses.add("Data Structure");
        education.courses.add("Algorithms");
        education.courses.add("OS");
    }

    public static String formatItems(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item: items) {
            sb.append(" - ").append(item).append('\n');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
