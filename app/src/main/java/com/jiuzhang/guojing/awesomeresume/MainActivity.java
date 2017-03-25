package com.jiuzhang.guojing.awesomeresume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jiuzhang.guojing.awesomeresume.model.BasicInfo;
import com.jiuzhang.guojing.awesomeresume.model.Education;
import com.jiuzhang.guojing.awesomeresume.util.DateUtils;
import com.jiuzhang.guojing.awesomeresume.model.Experience;
import com.jiuzhang.guojing.awesomeresume.model.Project;
import com.jiuzhang.guojing.awesomeresume.util.ImageUtils;
import com.jiuzhang.guojing.awesomeresume.util.ModelUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {

    // 一般从100开始的：
    private static final int REQ_CODE_EDIT_EDUCATION = 100;
    private static final int REQ_CODE_EDIT_EXPERIENCE = 101;
    private static final int REQ_CODE_EDIT_PROJECT = 102;
    private static final int REQ_CODE_EDIT_BASIC_INFO = 103;

    private static final String MODEL_EDUCATIONS = "educations";
    private static final String MODEL_EXPERIENCES = "experiences";
    private static final String MODEL_PROJECTS = "projects";
    private static final String MODEL_BASIC_INFO = "basic_info";

    private BasicInfo basicInfo;
    private List<Education> educations;
    private List<Experience> experiences;
    private List<Project> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        setupUI();
    }

    // onActivityResult： Activities之间数据沟通用的
    // 告诉主调activity， 被调activity已经结束，并返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        // 判断返回的code是不是edit education：
        if (resultCode == Activity.RESULT_OK) {
            // Request Edit Education
            if (requestCode == REQ_CODE_EDIT_EDUCATION) {
                String educationId = data.getStringExtra(EducationEditActivity.KEY_EDUCATION_ID);
                if (educationId != null) {
                    deleteEducation(educationId);
                } else {
                    Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
                    updateEducation(education);
                }
            }
            // Request Edit Experience
            else if (requestCode == REQ_CODE_EDIT_EXPERIENCE) {
                String experienceId = data.getStringExtra(ExperienceEditActivity.KEY_EXPERIENCE_ID);
                if (experienceId != null) {
                    deleteExperience(experienceId);
                } else {
                    Experience experience = data.getParcelableExtra(ExperienceEditActivity.KEY_EXPERIENCE);
                    updateExperience(experience);
                }
            }
            // Request Edit Project
            else if (requestCode == REQ_CODE_EDIT_PROJECT) {
                String projectId = data.getStringExtra(ProjectEditActivity.KEY_PROJECT_ID);
                if (projectId != null) {
                    deleteProject(projectId);
                } else {
                    Project project = data.getParcelableExtra(ProjectEditActivity.KEY_PROJECT);
                    updateProject(project);
                }
            }
            // Request Edit Basic Info
            else if (requestCode == REQ_CODE_EDIT_BASIC_INFO) {
                BasicInfo basicInfo = data.getParcelableExtra(BasicInfoEditActivity.KEY_BASIC_INFO);
                updateBasicInfo(basicInfo);
            }
        }
    }


    // -------------------------------SETUP UI (ADD "ADD" BUTTON ACTION) ----------------------------//
    private void setupUI() {
        setContentView(R.layout.activity_main);

        ImageButton addEducationBtn = (ImageButton) findViewById(R.id.add_education_btn);
        addEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });

        ImageButton addExperienceBtn = (ImageButton) findViewById(R.id.add_experience_btn);
        addExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });

        ImageButton addProjectBtn = (ImageButton) findViewById(R.id.add_project_btn);
        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_PROJECT);
            }
        });

        setupBasicInfoUI();
        setupEducations();
        setupExperiences();
        setupProjects();
    }


    // -------------------------------SETUP EDUCATIONS, EXPERIENCES, PROJECTS ----------------------------//
    private void setupBasicInfo() {
        ((TextView) findViewById(R.id.name)).setText(TextUtils.isEmpty(basicInfo.name)
                ? "Your name"
                : basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(TextUtils.isEmpty(basicInfo.email)
                ? "Your email"
                : basicInfo.email);

        ImageView userPicture = (ImageView) findViewById(R.id.user_picture);
        if (basicInfo.imageUri != null) {
            ImageUtils.loadImage(this, basicInfo.imageUri, userPicture);
        } else {
            userPicture.setImageResource(R.drawable.user_ghost);
        }

        findViewById(R.id.edit_basic_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BasicInfoEditActivity.class);
                intent.putExtra(BasicInfoEditActivity.KEY_BASIC_INFO, basicInfo);
                startActivityForResult(intent, REQ_CODE_EDIT_BASIC_INFO);
            }
        });
    }

    // SETUP EDUCATIONS----------------
    private void setupEducations() {
        LinearLayout educationContainer = (LinearLayout) findViewById(R.id.education_list);
        // 每次画之前，把原有的UI都去掉，就不会重复出现了
        educationContainer.removeAllViews();
        for (Education education : educations) {
            View educationView = getLayoutInflater().inflate(R.layout.education_item, null);
            View view = setupEducation(educationView, education);
            // 显示任意多个UI在界面上
            educationContainer.addView(view);
        }

        // 新建教育经历 "+"：
        findViewById(R.id.add_education_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                // 用startActivityForResult，才能回到onActivityResult 函数里面
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });
    }


    // 把data转成的一个view
    // set education as final: So that inner class object can make a copy of this variable and store the copy in heap
    private View setupEducation(View educationView, final Education education) {
        // getLayoutInflater： 把layout文件转化成一个view在手机上
        // inflate():读取layout文件转化成一个树，画出来
        // view 就是树的root
        // R.layout.education_item: XML layout文件的定位id
        // null: 该layout 的父亲 （一般限定layout的大小，一般设成null）
        String dateString = DateUtils.dateToString(education.startDate)
                + " ~ " + DateUtils.dateToString(education.endDate);
        // 一定要在刚生成的view里用findViewById。因为要在那里找school 和courses的信息
        ((TextView) educationView.findViewById(R.id.education_school)).setText(education.school + " (" + dateString + ")");
        ((TextView) educationView.findViewById(R.id.education_edit_major)).setText(education.major);
        ((TextView) educationView.findViewById(R.id.education_courses)).setText(formatItems(education.courses));

        // 以下当结论记：主窗口调用次窗口
        // This is how we launch an Activity within Activity
        // setOnClickListener 是一个view的类，所有的view都可以点击，所以可以不强制类型转换
        ((ImageButton) educationView.findViewById(R.id.edit_education_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                intent.putExtra(EducationEditActivity.KEY_EDUCATION, education);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });

        educationView.findViewById(R.id.edit_education_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                // This Education object is the one that’s correspondent to "edit" butten that's being clicked on
                intent.putExtra(EducationEditActivity.KEY_EDUCATION, education);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });

        return educationView;
    }


    // SETUP EXPERIENCES--------------------------
    private void setupExperiences() {
        LinearLayout experienceLayout = (LinearLayout) findViewById(R.id.experience_list);
        experienceLayout.removeAllViews();
        for (Experience experience : experiences) {
            View experienceView = getLayoutInflater().inflate(R.layout.experience_item, null);
            setupExperience(experienceView, experience);
            experienceLayout.addView(experienceView);
        }
    }

    private void setupExperience(@NonNull View experienceView, final Experience experience) {
        String dateString = DateUtils.dateToString(experience.startDate)
                + " ~ " + DateUtils.dateToString(experience.endDate);
        ((TextView) experienceView.findViewById(R.id.experience_company)).setText(experience.company + " (" + dateString + ")");
        ((TextView) experienceView.findViewById(R.id.experience_title)).setText(experience.title);
        ((TextView) experienceView.findViewById(R.id.experience_details)).setText(formatItems(experience.details));
        experienceView.findViewById(R.id.edit_experience_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                intent.putExtra(ExperienceEditActivity.KEY_EXPERIENCE, experience);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });
    }

    // SETUP PROJECTS------------------------------
    private void setupProjects() {
        LinearLayout projectListLayout = (LinearLayout) findViewById(R.id.project_list);
        projectListLayout.removeAllViews();
        for (Project project : projects) {
            View projectView = getLayoutInflater().inflate(R.layout.project_item, null);
            setupProject(projectView, project);
            projectListLayout.addView(projectView);
        }
    }

    private void setupProject(@NonNull View projectView, final Project project) {
        ((TextView) projectView.findViewById(R.id.project_name)).setText(project.name);
        ((TextView) projectView.findViewById(R.id.project_details)).setText(formatItems(project.details));
        projectView.findViewById(R.id.edit_project_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                intent.putExtra(ProjectEditActivity.KEY_PROJECT, project);
                startActivityForResult(intent, REQ_CODE_EDIT_PROJECT);
            }
        });
    }

    // -------------------------------DELETE & UPDATE EDUCATION, EXPERIENCE, PROJECT ----------------------------//
    // UPDATE BASIC INFO---------------------------
    private void updateBasicInfo(BasicInfo basicInfo) {
        ModelUtils.save(this, MODEL_BASIC_INFO, basicInfo);

        this.basicInfo = basicInfo;
        setupBasicInfo();
    }

    // DELETE AND UPDATE EDUCATION------------------------------
    private void deleteEducation(@NonNull String educationId) {
        for (int i = 0; i < educations.size(); i++) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, educationId)) {
                educations.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }

    private void updateEducation(Education education) {
        boolean found = false;
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, education.id)) {
                found = true;
                educations.set(i, education);
                break;
            }
        }
        // 有多少activity，就add多少到educations里呈现出来
        if (!found) {
            educations.add(education);
        }

        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }

    // DELETE AND UPDATE EXPERIENCE------------------------------
    private void deleteExperience(@NonNull String experienceId) {
        for (int i = 0; i < experiences.size(); i++) {
            Experience e = experiences.get(i);
            if (TextUtils.equals(e.id, experienceId)) {
                experiences.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
        setupExperiences();
    }

    private void updateExperience(Experience experience) {
        boolean found = false;
        for (int i = 0; i < experiences.size(); i++) {
            Experience e = experiences.get(i);
            if (TextUtils.equals(e.id, experience.id)) {
                found = true;
                experiences.set(i, experience);
                break;
            }
        }

        if (!found) {
            experiences.add(experience);
        }

        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
        setupExperiences();
    }

    // DELETE AND UPDATE PROJECT------------------------------
    private void deleteProject(@NonNull String projectId) {
        for (int i = 0; i < projects.size(); i++) {
            Project p = projects.get(i);
            if (TextUtils.equals(p.id, projectId)) {
                projects.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_PROJECTS, projects);
        setupProjects();
    }

    private void updateProject(Project project) {
        boolean found = false;
        for (int i = 0; i < projects.size(); i++) {
            Project p = projects.get(i);
            if (TextUtils.equals(p.id, project.id)) {
                found = true;
                projects.set(i, project);
                break;
            }
        }

        if (!found) {
            projects.add(project);
        }

        ModelUtils.save(this, MODEL_PROJECTS, projects);
        setupProjects();
    }


    // -------------------------------SETUP BASIC INFO UI ----------------------------//
    private void setupBasicInfoUI() {
        ((TextView) findViewById(R.id.name)).setText(basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(basicInfo.email);
    }

    // -------------------------------LOAD DATA ----------------------------//
    private void loadData() {
        BasicInfo savedBasicInfo = ModelUtils.read(this,
                MODEL_BASIC_INFO,
                new TypeToken<BasicInfo>(){});
        basicInfo = savedBasicInfo == null ? new BasicInfo() : savedBasicInfo;

        List<Education> savedEducation = ModelUtils.read(this,
                MODEL_EDUCATIONS,
                new TypeToken<List<Education>>(){});
        educations = savedEducation == null ? new ArrayList<Education>() : savedEducation;

        List<Experience> savedExperience = ModelUtils.read(this,
                MODEL_EXPERIENCES,
                new TypeToken<List<Experience>>(){});
        experiences = savedExperience == null ? new ArrayList<Experience>() : savedExperience;

        List<Project> savedProjects = ModelUtils.read(this,
                MODEL_PROJECTS,
                new TypeToken<List<Project>>(){});
        projects = savedProjects == null ? new ArrayList<Project>() : savedProjects;
    }

//    private void fakeData() {
//        basicInfo = new BasicInfo();
//        basicInfo.name = "Bowen Li";
//        basicInfo.email = "guojing@jiuzhang.com";
//
//        Education education1 = new Education();
//        education1.school = "BUPT";
//        education1.major = "Electrical Engineering";
//        education1.startDate = DateUtils.stringToDate("09/2010");
//
//        // TODO 1: Set the endDate
//        education1.endDate = DateUtils.stringToDate("06/2014");
//        // TODO 2: Add some fake courses in education1.courses
//        education1.courses = new ArrayList<>();
//        education1.courses.add("Circuit");
//        education1.courses.add("Telecommunication Enginnering");
//        education1.courses.add("Java");
//
//        Education education2 = new Education();
//        education2.school = "NCSU";
//        education2.major = "Electrical and Computer Engineering";
//        education2.startDate = DateUtils.stringToDate("08/2014");
//
//        education2.endDate = DateUtils.stringToDate("12/2017");
//        education2.courses = new ArrayList<>();
//        education2.courses.add("Data Structure");
//        education2.courses.add("Algorithms");
//        education2.courses.add("OS");
//        educations = new ArrayList<Education>();
//        educations.add(education1);
//        educations.add(education2);
//
//        Project p1 = new Project();
//        p1.name = "Project1";
//        p1.details = new ArrayList<>();
//        p1.details.add("thing1");
//        p1.details.add("thing2");
//        Project p2 = new Project();
//        p2.name = "Project2";
//        p2.details = new ArrayList<>();
//        p2.details.add("thing3");
//        p2.details.add("thing4");
//        projects = new ArrayList<Project>();
//        projects.add(p1);
//        projects.add(p2);
//    }

    public static String formatItems(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item : items) {
            sb.append(" - ").append(item).append('\n');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
