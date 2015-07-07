package ru.kolyanov542255.schedule_classes;

import android.app.Fragment;
import android.content.Intent;

import java.util.UUID;


public class LessonListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        UUID dayId = (UUID)getIntent()
                .getSerializableExtra(DayListFragment.EXTRA_DAY_ID);
        String dayName = getIntent().getStringExtra(DayListFragment.EXTRA_DAY_NAME);
        int odd = getIntent().getIntExtra(DayListFragment.EXTRA_WEEK_TYPE, 0);
        return LessonListFragment.newInstance(dayId, dayName, odd);
    }
}
