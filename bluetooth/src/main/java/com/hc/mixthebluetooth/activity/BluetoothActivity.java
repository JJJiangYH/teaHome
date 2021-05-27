package com.hc.mixthebluetooth.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hc.basiclibrary.viewBasic.BasActivity;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.R2;
import com.hc.mixthebluetooth.activity.single.HoldBluetooth;
import com.hc.mixthebluetooth.recyclerData.MainRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tea.view.Utils.ViewUtil.addStatusBar;

/**
 * @author 广州汇承信息科技有限公司
 */
public class BluetoothActivity extends BasActivity {
    private final List<DeviceModule> mModuleArray = new ArrayList<>();
    private final List<DeviceModule> mFilterModuleArray = new ArrayList<>();

    @SuppressLint("NonConstantResourceId")
    @BindView(R2.id.main_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R2.id.main_back_not)
    LinearLayout mNotBluetooth;
    @SuppressLint("NonConstantResourceId")
    @BindView(R2.id.main_recycler)
    RecyclerView mRecyclerView;

    private MainRecyclerAdapter mainRecyclerAdapter;
    private HoldBluetooth mHoldBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);
        addStatusBar(this, R.id.main_name, R.color.flesh_black);
        setContext(this);
    }

    @Override
    public void initAll() {
        //初始化单例模式中的蓝牙扫描回调
        initHoldBluetooth();
        //初始化View
        initView();
        //初始化下拉刷新
        initRefresh();
        //设置RecyclerView的Item的点击事件
        setRecyclerListener();

        refresh();
    }

    private void initHoldBluetooth() {
        mHoldBluetooth = HoldBluetooth.getInstance();
        final HoldBluetooth.UpdateList updateList = new HoldBluetooth.UpdateList() {
            @Override
            public void update(boolean isStart, DeviceModule deviceModule) {
                if (isStart) {
                    log("回调数据..", "w");
                    setMainBackIcon();
                    mModuleArray.add(deviceModule);
                    addFilterList(deviceModule, true);
                }
            }

            @Override
            public void updateMessyCode(boolean isStart, DeviceModule deviceModule) {
                for (int i = 0; i < mModuleArray.size(); i++) {
                    if (mModuleArray.get(i).getMac().equals(deviceModule.getMac())) {
                        mModuleArray.remove(mModuleArray.get(i));
                        mModuleArray.add(i, deviceModule);
                        upDateList();
                        break;
                    }
                }
            }
        };
        mHoldBluetooth.initHoldBluetooth(BluetoothActivity.this, updateList);
    }

    private void initView() {
        setMainBackIcon();

        mainRecyclerAdapter = new MainRecyclerAdapter(this, mFilterModuleArray, R.layout.item_recycler_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mainRecyclerAdapter);
    }

    //初始化下拉刷新
    private void initRefresh() {
        //设置刷新监听器
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            refresh();
        });
    }

    //刷新的具体实现
    private void refresh() {
        if (mHoldBluetooth.scan()) {
            mModuleArray.clear();
            mFilterModuleArray.clear();
        }
    }

    //根据条件过滤列表，并选择是否更新列表
    private void addFilterList(DeviceModule deviceModule, boolean isRefresh) {
        if (deviceModule.getName().equals("N/A"))
            return;

        deviceModule.isCollectName(this);
        mFilterModuleArray.add(deviceModule);
        if (isRefresh)
            mainRecyclerAdapter.notifyDataSetChanged();
    }

    //设置点击事件
    private void setRecyclerListener() {
        mainRecyclerAdapter.setOnItemClickListener((position, view) -> {
            mHoldBluetooth.connect(mFilterModuleArray.get(position));
            setResult(RESULT_OK);
            super.finish();
        });
    }

    //更新列表
    private void upDateList() {
        mFilterModuleArray.clear();
        for (DeviceModule deviceModule : mModuleArray) {
            addFilterList(deviceModule, false);
        }
        mainRecyclerAdapter.notifyDataSetChanged();
        setMainBackIcon();
    }

    //设置列表的背景图片是否显示
    private void setMainBackIcon() {
        if (mFilterModuleArray.size() == 0) {
            mNotBluetooth.setVisibility(View.VISIBLE);
        } else {
            mNotBluetooth.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //退出这个界面，或是返回桌面时，停止扫描
        mHoldBluetooth.stopScan();
    }

    @Override
    public void finish() {
        setResult(RESULT_CANCELED);
        super.finish();
    }
}