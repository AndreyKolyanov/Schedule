package ru.kolyanov542255.schedule_classes;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
        setHasOptionsMenu(true);

        String title = getArguments().getString(DayListFragment.EXTRA_DAY_NAME);
        int odd = getArguments().getInt(DayListFragment.EXTRA_WEEK_TYPE);
        Resources res = getResources();
        String[] oddOrEven = res.getStringArray(R.array.WeekType);

        getActivity().setTitle(title + " " + oddOrEven[odd]);

        lessons = new ArrayList<Lesson>();

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView)v.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(listView);
        } else {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                    //don't used
                }

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    MenuInflater inflater = actionMode.getMenuInflater();
                    inflater.inflate(R.menu.lesson_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.lesson_menu_item_delele:
                            LessonAdapter adapter = (LessonAdapter)getListAdapter();
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    day.delLesson(adapter.getItem(i));
                                    DayLab.get(getActivity()).saveSchedule();
                                }
                            }
                            actionMode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {
                    //don't used
                }
            });
        }
        return v;
    }

    public void onResume(){
        super.onResume();
        day.delLesson();
        ((LessonAdapter)getListAdapter()).notifyDataSetChanged();
        DayLab.get(getActivity()).saveSchedule();

    }

    private void openLesson(int position){
        Lesson less = ((LessonAdapter)getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), LessonActivity.class);
        i.putExtra(DayListFragment.EXTRA_DAY_ID, day.getId());
        i.putExtra(LessonFragment.EXTRA_LESSON_ID, less.getId());
        startActivity(i);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        openLesson(position);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.lesson_list_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_lesson:
                    Lesson newLesson = new Lesson("", getActivity()
                            .getSharedPreferences(WeekPagerActivity.APP_PREFS, Context.MODE_PRIVATE)
                            .getInt(SettingsActivity.DURATION,95));
                    day.addLesson(newLesson);
                    lessons = day.getLessons();
                    ((LessonAdapter)getListAdapter()).notifyDataSetChanged();
                    openLesson(((LessonAdapter)getListAdapter()).getPosition(newLesson));
                    return true;
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        getActivity().getMenuInflater().inflate(R.menu.lesson_list_context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        LessonAdapter adapter = (LessonAdapter)getListAdapter();
        Lesson lesson = adapter.getItem(position);

        switch (item.getItemId()){
            case R.id.lessonlist_context_menu_item_delete:
                day.delLesson(lesson);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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

            TextView room = (TextView)convertView.findViewById(R.id.lesson_list_item_room);
            room.setText(l.getRoom());

            if (l.isShowTime()) {
                TextView time = (TextView) convertView.findViewById(R.id.lesson_list_item_time);
                time.setText(l.getTime());
            }

            return convertView;
        }
    }
}
