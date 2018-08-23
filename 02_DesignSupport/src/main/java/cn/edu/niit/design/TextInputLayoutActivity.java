package cn.edu.niit.design;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TextInputLayoutActivity extends AppCompatActivity {

    @BindView(R.id.tl_username)
    TextInputLayout tlUsername;
    @BindView(R.id.tl_password)
    TextInputLayout tlPassword;
    @BindView(R.id.bt_login)
    Button btLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input_layout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_login)
    public void onBtnLogin() {
        String username=tlUsername.getEditText().getText().toString();
        String password=tlPassword.getEditText().getText().toString();
        if(!validateUserName(username)) {
            tlUsername.setErrorEnabled(true);
            tlUsername.setError("请输入正确的邮箱地址");
        }else if(!validatePassword(password)) {
            tlPassword.setErrorEnabled(true);
            tlPassword.setError("密码字数过少");
        } else {
            tlUsername.setErrorEnabled(false);
            tlPassword.setErrorEnabled(false);
            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean validatePassword(String password){
        return password.length() > 6;
    }

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;" +
            "<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

    private boolean validateUserName(String username){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
