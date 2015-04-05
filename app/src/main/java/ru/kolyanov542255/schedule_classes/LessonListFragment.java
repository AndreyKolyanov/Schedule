package ru.kolyanov542255.schedule_classes;

import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Адольф on 17.01.2015.
 */
public class LessonListFragment extends ListFragment {

    public static final String TAG = "LessonListFragment";

    private ArrayList<Lesson> lessons;
    private DayOfWeek day;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String title = getArguments().getString(DayListFragment.EXTRA_DAY_NAME);
        int odd = getArguments().getInt(DayListFragment.EXTRA_WEEK_TYPE);
        Resources res = getResources();
        String[] oddOrEven = res.getStringArray(R.array.WeekType);

        getActivity().setTitle(title + " " + oddOrEven[odd]);


        lessons = new ArrayList<Lesson>();
        //lessons = LessonLab.get(getActivity()).getLessons();

        UUID dayId = (UUID)getArguments().getSerializable(DayListFragment.EXTRA_DAY_ID);
        day = DayLab.get(getActivity()).getDay(dayId);

        lessons = day.getLessons();

        LessonAdapter adapter = new LessonAdapter(lessons);
        setListAdapter(adapter);

        Log.d(TAG, day.getName() + " onCreate completed");
    }

    public static LessonListFragment newInstance(UUID dayId, String dayName, int odd) {
        Bundle args = new Bundle();
        args.putSerializable(DayListFragment.EXTRA_DAY_ID, dayId);
        args.putInt(DayListFragment.EXTRA_WEEK_TYPE, odd);
        args.putString(DayListFragment.EXTRA_DAY_NAME, dayName);
        LessonListFragment fragment = new LessonListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onResume(){
        super.onResume();
        day.delLesson();
        ((LessonAdapter)getListAdapter()).notifyDataSetChanged();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        if (position == 0){
            day.addLesson(new Lesson(""));
            position = lessons.size() - 1;
            lessons = day.getLessons();
            ((LessonAdapter)getListAdapter()).notifyDataSetChanged();
        }
        Lesson less = ((LessonAdapter)getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), LessonActivity.class);
        i.putExtra(DayListFragment.EXTRA_DAY_ID, day.getId());
        i.putExtra(LessonFragment.EXTRA_LESSON_ID, less.getId());
        startActivity(i);
    }


    private class LessonAdapter extends ArrayAdapter<Lesson>{

        public LessonAdapter(ArrayList<Lesson> lessons){
            super(getActivity(), 0, lessons);
        }

        public View getView(int position, View convertView, ViewGroup parent){

            if (convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_lesson, null);
            }

            Lesson l = getItem(position);

            TextView name_lesson = (TextView)convertView.findViewById(R.id.lesson_list_item_name);
            name_lesson.setText(l.getName());

            TextView teacher = (TextView)convertView.findViewById(R.id.lesson_list_item_teacher);
            teacher.setText(l.getTeacher());

            if (l.getName().length() > 16){
                name_lesson.setTextSize(teacher.getTextSize());
            }

            TextView room = (TextView)convertView.findViewById(R.id.lesson_list_item_room);
            room.setText(l.getRoom());

            if (l.getName().length() > 18){
                name_lesson.setTextSize(room.getTextSize());
            }
            if (l.getTeacher().length() > 18){
                teacher.setTextSize(room.getTextSize());
            }


            if (l.isShowTime()) {
                TextView time = (TextView) convertView.findViewById(R.id.lesson_list_item_time);
                time.setText(l.getTime());
            }

            return convertView;
        }
    }
}
