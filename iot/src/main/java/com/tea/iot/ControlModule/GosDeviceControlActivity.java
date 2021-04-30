package com.tea.iot.ControlModule;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.tea.iot.CommonModule.GosBaseActivity;
import com.tea.iot.R;

public class GosDeviceControlActivity extends GosBaseActivity {
    /**
     * The ActionBar actionBar
     */
    ActionBar actionBar;
    /**
     * The tv MAC
     */
    private TextView tvMAC;
    /**
     * The GizWifiDevice device
     */
    private GizWifiDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_device_control);
        initDevice();
        setActionBar(true, true, device.getProductName());
        initView();
    }

    private void initView() {
        tvMAC = (TextView) findViewById(R.id.tvMAC);
        if (null != device) {

            tvMAC.setText(device.getMacAddress());

        }
    }

    private void initDevice() {
        Intent intent = getIntent();
        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        Log.i("Apptest", device.getDid());
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

}
