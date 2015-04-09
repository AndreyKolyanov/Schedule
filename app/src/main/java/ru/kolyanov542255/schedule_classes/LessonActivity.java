package ru.kolyanov542255.schedule_classes;

import android.app.Fragment;
import android.content.Intent;

import com.vk.sdk.VKUIHelper;

import java.util.UUID;

public class LessonActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        UUID lessonId = (UUID)getIntent()
                .getSerializableExtra(LessonFragment.EXTRA_LESSON_ID);
        UUID dayId = (UUID)getIntent().getSerializableExtra(DayListFragment.EXTRA_DAY_ID);
        return LessonFragment.newInstance(lessonId, dayId);
    }

    @Override
    protected void onResume(){
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }
}
