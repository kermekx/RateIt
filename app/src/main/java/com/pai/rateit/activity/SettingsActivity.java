package com.pai.rateit.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.pai.rateit.R;
import com.pai.rateit.controller.account.AccountController;
import com.pai.rateit.model.user.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class SettingsActivity extends AppCompatActivity implements AccountController.AccountStateListener {

    @BindView(R.id.switch_should_notify)
    Switch shouldNotifySwitch;

    private AccountController mAccountController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mAccountController = new AccountController(this, this);
    }

    @Override
    public void onAccountChanged(FirebaseUser firebaseUser) {

    }

    @Override
    public void onUserDataChanged(User user) {
        shouldNotifySwitch.setEnabled(user != null);
        shouldNotifySwitch.setChecked(user != null && user.isNotify());
    }

    @OnCheckedChanged(R.id.switch_should_notify)
    public void onShouldNotifyChecked(boolean isChecked) {
        mAccountController.setNotificationPermission(isChecked);
    }
}
