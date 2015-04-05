package ru.kolyanov542255.schedule_classes;

import android.app.Fragment;

import java.util.UUID;

public class LessonActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        UUID lessonId = (UUID)getIntent()
                .getSerializableExtra(LessonFragment.EXTRA_LESSON_ID);
        UUID dayId = (UUID)getIntent().getSerializableExtra(DayListFragment.EXTRA_DAY_ID);
        return LessonFragment.newInstance(lessonId, dayId);
    }

}
