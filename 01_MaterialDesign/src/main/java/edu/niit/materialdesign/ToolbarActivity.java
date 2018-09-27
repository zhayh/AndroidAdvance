package edu.niit.materialdesign;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ToolbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        // 添加菜单
        setSupportActionBar(toolbar);
//         toolbar.inflateMenu(R.menu.zhihu);
        // 添加导航图标
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
        // 添加主标题
        toolbar.setTitle("首页");
        // 设置主标题的颜色
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        toolbar.setSubtitle("副标题");
        //还可以代码设置标题颜色
        toolbar.setSubtitleTextColor(Color.WHITE);
        //设置logo。您要注意logo与导航位置图标的区别
        toolbar.setLogo(R.mipmap.ic_launcher_round);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        Toast.makeText(ToolbarActivity.this, "toolbar Search !", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.zhihu, menu);
        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                Toast.makeText(ToolbarActivity.this, "option item earch", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return true;
//    }
}
