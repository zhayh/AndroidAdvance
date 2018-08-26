package cn.edu.niit.customview.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.niit.customview.R;

public class CustomViewGroupActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.lv_one)
    ListView lvOne;
    @BindView(R.id.lv_two)
    ListView lvTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_viewgroup);
        ButterKnife.bind(this);

        String[] strs1 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, strs1);
        lvOne.setAdapter(adapter1);

        String[] strs2 = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, strs2);
        lvTwo.setAdapter(adapter2);
    }

    @OnClick({R.id.iv_titlebar_left, R.id.iv_titlebar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_titlebar_left:
                Toast.makeText(CustomViewGroupActivity.this, "点击左键", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_titlebar_right:
                Toast.makeText(CustomViewGroupActivity.this, "点击右键", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
