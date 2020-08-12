package com.ljz.rxjava;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.rxjava.data.Course;
import com.ljz.rxjava.data.Student;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class TestRxMapActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TestRxMapActivity.class.getSimpleName();
    private Context mContext;
    private Button map1Btn;
    private Button map2Btn;
    private Button map3Btn;
    private Button flatmapBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mContext = this;

        map1Btn = findViewById(R.id.test_map1);
        map1Btn.setOnClickListener(this);
        map2Btn = findViewById(R.id.test_map2);
        map2Btn.setOnClickListener(this);
        map3Btn = findViewById(R.id.test_map3);
        map3Btn.setOnClickListener(this);
        flatmapBtn = findViewById(R.id.test_flatmap);
        flatmapBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.test_map1:
                testRxMap1();
                break;
            case R.id.test_map2:
                testRxMap2();
                break;
            case R.id.test_map3:
                testRxMap3();
                break;
            case R.id.test_flatmap:
                testRxFlatmap();
                break;
            default:
                break;
        }
    }

    private void testRxMap1() {
        Student student1 = new Student("ljz-1", 1);
        Student student2 = new Student("ljz-2", 2);
        Student student3 = new Student("ljz-3", 3);
        final List<String> nameList = new ArrayList<>();

        Observable.just(student1, student2, student3)
                //使用map进行转换，参数1：转换前的类型，参数2：转换后的类型
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student i) {
                        String name = i.getName();//获取Student对象中的name
                        Log.d(TAG, "testRxMap1, Func1 call: name = " + name);
                        return name;//返回name
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "testRxMap1, Action1 call: s = " + s);
                        nameList.add(s);
                    }
                });
    }

    private void testRxMap2() {
        //多次使用map，想用几个用几个
        Observable.just("Hello", "World")
                .map(new Func1<String, Integer>() {//将String类型的转化为Integer类型的哈希码
                    @Override
                    public Integer call(String s) {
                        return s.hashCode();
                    }
                })
                .map(new Func1<Integer, String>() {//将转化后得到的Integer类型的哈希码再转化为String类型
                    @Override
                    public String call(Integer integer) {
                        return integer.intValue() + "";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "testRxMap2, Action1 call: s = " + s);
                    }
                });
    }

    private void testRxMap3() {
        Observable.from(getStudents()).map(new Func1<Student, List<Course>>() {
            @Override
            public List<Course> call(Student student) {
                return student.getCoursesList();
            }
        }).subscribe(new Action1<List<Course>>() {
            @Override
            public void call(List<Course> courses) {
                Log.d(TAG, "testRxMap3, call: courses = " + courses);
                if (courses != null) {
                    for (int i = 0; i < courses.size(); i++) {
                        Log.d(TAG, "testRxMap3, call: course name = " + courses.get(i).getName());
                    }
                }
            }
        });
    }

    private void testRxFlatmap() {
        Observable.from(getStudents())
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCoursesList());
                    }
                })
                .subscribe(new Action1<Course>() {
                    @Override
                    public void call(Course course) {
                        Log.d(TAG, "testRxFlatMap, call: courses name = " + course.getName());
                    }
                });
    }

    private List<Student> getStudents() {
        //数学 语文 英语 美术 物理 化学 生物 地理 音乐 体育
        List<Student> students = new ArrayList<>();
        Student student1 = new Student("ljz-1", 1);
        List<Course> courses1 = new ArrayList<>();
        courses1.add(new Course("数学", "1"));
        courses1.add(new Course("体育", "2"));
        courses1.add(new Course("语文", "3"));
        student1.setCoursesList(courses1);

        Student student2 = new Student("ljz-2", 2);
        List<Course> courses2 = new ArrayList<>();
        courses2.add(new Course("物理", "1"));
        courses2.add(new Course("地理", "2"));
        courses2.add(new Course("美术", "3"));
        student2.setCoursesList(courses2);

        Student student3 = new Student("ljz-3", 3);
        List<Course> courses3 = new ArrayList<>();
        courses3.add(new Course("生物", "1"));
        courses3.add(new Course("音乐", "2"));
        courses3.add(new Course("英语", "3"));
        student3.setCoursesList(courses3);

        students.add(student1);
        students.add(student2);
        students.add(student3);
        return students;
    }
}
