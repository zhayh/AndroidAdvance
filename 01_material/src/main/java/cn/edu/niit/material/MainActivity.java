package cn.edu.niit.material;

import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;

import cn.edu.niit.material.fragment.BaseFragment;
import cn.edu.niit.material.fragment.FragmentFactory;


public class MainActivity extends Activity implements DrawerLayout.DrawerListener, DrawerFragment.OnDrawerItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private DrawerFragment mDrawerFragment;
    private ActionBarDrawerToggle mToggle;
    private BaseFragment mCurrentFragment;
    private CharSequence mTitle;
    public static int mTheme = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mTheme != -1) {
            setTheme(mTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDrawer();
        initActionBar();

        sendNormalNotification();
        sendFoldNotification();
        sendHangNotification();

    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerListener(this);

        //菜单
        mDrawerFragment = (DrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mDrawerFragment.setOnDrawerItemSelectedListener(this);
        mDrawerFragment.selectItem(0);
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//设置显示左侧按钮
        actionBar.setHomeButtonEnabled(true);//设置左侧按钮可点
        actionBar.setDisplayShowTitleEnabled(true);//设置显示标题

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        //初始化开关，并和drawer关联
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mToggle.syncState();//该方法会自动和actionBar关联

        actionBar.setTitle(getString(R.string.app_name));//设置标题
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.main, menu);
            mCurrentFragment.onCreateOptionsMenu(menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || mCurrentFragment.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Drawer的回调方法，需要在该方法中对Toggle做对应的操作
     */
    @Override
    public void onDrawerOpened(View drawerView) {// 打开drawer
        mToggle.onDrawerOpened(drawerView);//需要把开关也变为打开
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(View drawerView) {// 关闭drawer
        mToggle.onDrawerClosed(drawerView);//需要把开关也变为关闭
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {// drawer滑动的回调
        mToggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerStateChanged(int newState) {// drawer状态改变的回调
        mToggle.onDrawerStateChanged(newState);
    }

    /**
     * DrawerMenu的回调方法，需要在该方法中添加对应的Framgment
     */
    @Override
    public void onDrawerItemSelected(int position) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        FragmentManager fragmentManager = getFragmentManager();
        BaseFragment fragment = FragmentFactory.createFragment(position);
        mCurrentFragment = fragment;
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void sendNormalNotification() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("Http://www.niit.edu.cn"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setContentTitle("普通通知");

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void sendFoldNotification() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("Http://www.niit.edu.cn"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.foldleft);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setContentTitle("折叠式通知");

        // 用RemoteViews创建自定义Notification视图
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_fold);
        Notification notification = builder.build();
        notification.bigContentView = remoteViews;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, notification);
    }


    private void sendHangNotification() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("Http://www.niit.edu.cn"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.foldleft);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setContentTitle("悬挂式通知");

        // 设置点击跳转
        Intent hangIntent = new Intent();
        hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        hangIntent.setClass(this, MainActivity.class);

        // 如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消当前的
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setFullScreenIntent(hangPendingIntent, true);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());
    }
}
