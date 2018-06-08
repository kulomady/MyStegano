package com.kulomady.mystegano.Text;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.kulomady.mystegano.R;
import com.kulomady.mystegano.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.link_signup) void btnSignupClicked() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @OnClick(R.id.btn_login) void btnLoginClicked() {
        Toast.makeText(this,"test", Toast.LENGTH_LONG).show();
    }
}
