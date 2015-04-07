package ru.kolyanov542255.schedule_classes;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

public class LessonFragment extends Fragment {
    private Lesson lesson;
    DayOfWeek day;
    private EditText titleField;
    private EditText teacherField;
    private EditText roomField;
    private Button timeButton;

    public static final String EXTRA_LESSON_ID = "ru.kolyanov542255.schedule_classes.lesson_id";
    private static final String DIALOG_TIME = "time";
    public static final int REQUEST_DATA = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID lessonId = (UUID)getArguments().getSerializable(EXTRA_LESSON_ID);
        UUID dayId = (UUID)getArguments().getSerializable(DayListFragment.EXTRA_DAY_ID);
        day = DayLab.get(getActivity()).getDay(dayId);
        lesson = day.getLesson(lessonId);
    }

    public void onPause(){
        super.onPause();
        DayLab.get(getActivity()).saveSchedule();
    }

    public static LessonFragment newInstance(UUID lessonId, UUID dayId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LESSON_ID, lessonId);
        args.putSerializable(DayListFragment.EXTRA_DAY_ID, dayId);
        LessonFragment fragment = new LessonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATA) {
            Date date = (Date)data
                    .getSerializableExtra(TimePickerFragment.TIME_EXTRA);
            lesson.setBeginTime(date);
            updateDate();
        }
    }

    public void updateDate() {
        timeButton.setText(lesson.getTime());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.lesson_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.lesson_menu_item_delele:
                int num = 1;
                if (day.isOdd()){
                    num = 0;
                }
                Intent i = new Intent(getActivity(), LessonListActivity.class);
                i.putExtra(DayListFragment.EXTRA_DAY_NAME, day.getName());
                i.putExtra(DayListFragment.EXTRA_WEEK_TYPE, num);
                i.putExtra(DayListFragment.EXTRA_DAY_ID, day.getId());

                day.removeLesson(lesson);
                startActivity(i);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_lesson, parent, false);

        timeButton = (Button)v.findViewById(R.id.week_type_begin_time);
        updateDate();

        timeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(lesson.getBeginTime());
                dialog.setTargetFragment(LessonFragment.this, REQUEST_DATA);
                dialog.show(fm, DIALOG_TIME);
            }
        });

        titleField = (EditText)v.findViewById(R.id.lesson_name);
        titleField.setText(lesson.getName());

        titleField.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence c, int start, int before, int count) {
                lesson.setName(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // Здесь намеренно оставлено пустое место
            }

            public void afterTextChanged(Editable c) {
                // И здесь тоже
            }
        });

        teacherField = (EditText)v.findViewById(R.id.teacher_name);
        teacherField.setText(lesson.getTeacher());

        teacherField.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence c, int start, int before, int count) {
                lesson.setTeacher(c.toString());
            }
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // Здесь намеренно оставлено пустое место
            }
            public void afterTextChanged(Editable c) {
                // И здесь тоже
            }
        });

        roomField = (EditText)v.findViewById(R.id.room);
        roomField.setText(lesson.getRoom());

        roomField.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence c, int start, int before, int count) {
                lesson.setRoom(c.toString());
            }
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // Здесь намеренно оставлено пустое место
            }
            public void afterTextChanged(Editable c) {
                // И здесь тоже
            }
        });

        return v;
    }
}
