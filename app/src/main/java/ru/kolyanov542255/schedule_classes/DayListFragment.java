package ru.kolyanov542255.schedule_classes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.ListFragment;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Адольф on 19.01.2015.
 */
public class DayListFragment extends ListFragment {

    public static final String EXTRA_DAY_ID = "ru.kolyanov542255.schedule_classes.day_id";
    public static final String EXTRA_DAY_NAME = "ru.kolyanov542255.schedule_classes.day_name";
    public static final String EXTRA_WEEK_NUMBER = "ru.kolyanov542255.schedule_classes.week_number";
    public static final String EXTRA_WEEK_TYPE = "ru.kolyanov542255.schedule_classes.week_type";
    public static final String TAG = "DayListFragment";

    private ArrayList<DayOfWeek> days;
    private ArrayList<ArrayList<DayOfWeek>> weeks;
    private int num;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        num = getArguments().getInt(EXTRA_WEEK_NUMBER);

        weeks = DayLab.get(getActivity()).getWeeks();

        days = weeks.get(num);


        DaysAdapter adapter = new DaysAdapter(days);
        setListAdapter(adapter);

    }

    private class DaysAdapter extends ArrayAdapter<DayOfWeek>{

        public DaysAdapter(ArrayList<DayOfWeek> days){
            super(getActivity(), 0, days);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            if (convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_day, null);
            }

            DayOfWeek d = getItem(position);

            TextView title = (TextView) convertView.findViewById(R.id.day_title);
            title.setText(d.getName());

            return convertView;
        }
    }

    public static DayListFragment newInstance(int weekNumber){
        Bundle args = new Bundle();
        args.putInt(EXTRA_WEEK_NUMBER, weekNumber);
        DayListFragment fragment = new DayListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        DayOfWeek day = (DayOfWeek)getListAdapter().getItem(position);
        Log.d(TAG, day.getName() + " was clicked");
        Intent i = new Intent(getActivity(), LessonListActivity.class);
        i.putExtra(EXTRA_DAY_NAME, day.getName());
        i.putExtra(EXTRA_WEEK_TYPE, num);
        i.putExtra(EXTRA_DAY_ID, day.getId());
        startActivity(i);
    }

}
