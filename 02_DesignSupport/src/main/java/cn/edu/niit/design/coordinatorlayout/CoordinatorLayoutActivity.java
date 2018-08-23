package cn.edu.niit.design.coordinatorlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.niit.design.R;
import cn.edu.niit.design.tablayout.FragmentAdapter;
import cn.edu.niit.design.tablayout.ListFragment;

public class CoordinatorLayoutActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViewPager();
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("精选");
        titles.add("新闻");
        titles.add("体育");
        titles.add("热点");
        titles.add("教育");

        for(String title : titles) {
            tabs.addTab(tabs.newTab().setText(title));
            fragments.add(new ListFragment());
        }

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments,
                titles);
        viewpager.setAdapter(adapter);
        tabs.setupWithViewPager(viewpager);
    }

    public void checkin(View view) {
        Snackbar.make(view, "点击成功", Snackbar.LENGTH_LONG).show();
    }
}
