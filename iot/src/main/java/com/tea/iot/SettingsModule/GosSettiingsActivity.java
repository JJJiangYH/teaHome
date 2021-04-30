package com.tea.iot.SettingsModule;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tea.iot.CommonModule.GosBaseActivity;
import com.tea.iot.R;
import com.tea.iot.UserModule.GosUserManager;
import com.tea.iot.sharingdevice.SharedDeviceListAcitivity;

public class GosSettiingsActivity extends GosBaseActivity
        implements OnClickListener {
    private static final int SETTINGS = 123;
    /**
     * The Intent
     */
    Intent intent;
    /**
     * The ll About
     */
    private LinearLayout llAbout;
    private LinearLayout usermanager;

    private LinearLayout lllogin;

    private TextView phoneusername;
    private LinearLayout deviceshared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_settings);
        // 设置ActionBar
        // setActionBar(true, true, R.string.personal_center);

        initView();
        initEvent();
    }

    private void initView() {
        llAbout = (LinearLayout) findViewById(R.id.llAbout);

        usermanager = (LinearLayout) findViewById(R.id.usermanager);

        lllogin = (LinearLayout) findViewById(R.id.lllogin);

        deviceshared = (LinearLayout) findViewById(R.id.deviceshared);

        phoneusername = (TextView) findViewById(R.id.phoneusername);

        if (!TextUtils.isEmpty(spf.getString("UserName", "")) && !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
            usermanager.setVisibility(View.VISIBLE);

            lllogin.setVisibility(View.GONE);
            phoneusername.setText(spf.getString("UserName", ""));
        } else if (TextUtils.isEmpty(spf.getString("UserName", "")) && TextUtils.isEmpty(spf.getString("PassWord", ""))
                && !TextUtils.isEmpty(spf.getString("thirdUid", ""))) {

            usermanager.setVisibility(View.VISIBLE);
            String uid = spf.getString("thirdUid", "");

            lllogin.setVisibility(View.GONE);
            String myuid = uid.substring(0, 2) + "***" + uid.substring(uid.length() - 4);
            phoneusername.setText(myuid);
        } else {
            usermanager.setVisibility(View.GONE);
            lllogin.setVisibility(View.VISIBLE);
        }
    }

    private void initEvent() {
        llAbout.setOnClickListener(this);
        usermanager.setOnClickListener(this);
        lllogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.llAbout) {
            intent = new Intent(GosSettiingsActivity.this, GosAboutActivity.class);
            startActivity(intent);
            llAbout.setEnabled(false);
            llAbout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    llAbout.setEnabled(true);
                }
            }, 1000);
        } else if (id == R.id.usermanager) {
            intent = new Intent(GosSettiingsActivity.this, GosUserManager.class);
            startActivityForResult(intent, SETTINGS);

            usermanager.setEnabled(false);
            usermanager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    usermanager.setEnabled(true);
                }
            }, 1000);
        } else if (id == R.id.lllogin) {
            setResult(SETTINGS);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 234) {
            setResult(SETTINGS);
            finish();
        }
    }

    // 设备共享
    public void deviceshared(View v) {

        Intent deviceshared1 = new Intent(this, SharedDeviceListAcitivity.class);

        startActivity(deviceshared1);

        deviceshared.setEnabled(false);
        deviceshared.postDelayed(new Runnable() {
            @Override
            public void run() {
                deviceshared.setEnabled(true);
            }
        }, 1000);

    }
}
