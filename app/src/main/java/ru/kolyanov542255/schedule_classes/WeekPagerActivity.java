package ru.kolyanov542255.schedule_classes;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;

/**
 * Created by Адольф on 22.01.2015.
 */
public class WeekPagerActivity extends FragmentActivity {

    private ViewPager viewPager;

    private ArrayList<ArrayList<DayOfWeek>> weeks;

    private static final String APP_ID = "4866983";

    private final String ACCES_TOKEN = "VK_ACCESS_TOKEN";

    private static String[] sMyScope = new String[]{VKScope.FRIENDS, VKScope.GROUPS, VKScope.NOHTTPS, VKScope.DOCS};

    private final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            Toast.makeText(getApplicationContext(),"Captcha error", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            Toast.makeText(getApplicationContext(),"Token experied", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAccessDenied(VKError authorizationError) {
            Toast.makeText(getApplicationContext(),"Access denied", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken){
            newToken.saveTokenToSharedPreferences(WeekPagerActivity.this, ACCES_TOKEN);
            Intent i = new Intent(WeekPagerActivity.this, WeekPagerActivity.class);
            startActivity(i);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        weeks = DayLab.get(this).getWeeks();

        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {
                setTitle("Раписание ч/н");
                Log.d("PAGER", "App running");
                return DayListFragment.newInstance(i);
            }

            @Override
            public int getCount() {
                return weeks.size();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int state) { }

            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) { }

            public void onPageSelected(int pos) {
                Resources res = getResources();
                String[] oddOrEven = res.getStringArray(R.array.WeekType);
                setTitle("Расписание " + oddOrEven[pos]);
            }
        });


        VKSdk.initialize(sdkListener, APP_ID);


    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.week_pager_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.pager_week_menu:
                /*Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);*/
                //VKSdk.authorize(sMyScope);
            default:
                return super.onOptionsItemSelected(item);
        }
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
