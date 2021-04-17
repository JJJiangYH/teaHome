package com.tea.teahome.Control.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tea.teahome.Control.params.CommonRecogParams;
import com.tea.teahome.Control.params.NluRecogParams;
import com.tea.teahome.Control.recog.IStatus;
import com.tea.teahome.R;

import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.tea.teahome.Utils.ViewUtil.addStatusBar;


public abstract class ActivityUiRecog extends ActivityCommon implements IStatus {
    /**
     * 日志使用
     */
    private static final String TAG = "ActivityUiRecog";
    /**
     * Api的参数类，仅仅用于生成调用START的json字符串，本身与SDK的调用无关
     */
    private final CommonRecogParams apiParams;
    protected boolean running = false;
    /**
     * 显示温度的tv
     */
    @BindViews({R.id.tv_set_temp_50, R.id.tv_set_temp_65, R.id.tv_set_temp_85, R.id.tv_set_temp_100,
            R.id.tv_temp_min_c, R.id.tv_temp_max_c})
    List<TextView> tv_set_temp;
    /**
     * 华氏度字符串
     */
    @BindArray(R.array.temps_f)
    String[] temp_string_f;
    @BindArray(R.array.temps_c)
    String[] temp_string_c;
    /**
     * 设置硬件的开关
     */
    @BindView(R.id.bt_state_change)
    Button bt_state_change;
    /**
     * 时间最大值
     */
    @BindView(R.id.time_max)
    TextView time_max;
    /**
     * 显示温度的文本框
     */
    @BindView(R.id.tv_temp_now)
    TextView tv_temp_now;
    /**
     * 显示语音结果的文本框
     */
    @BindView(R.id.tv_speech_message)
    TextView tv_speech_message;
    /**
     * 显示倒计时的文本框
     */
    @BindView(R.id.tv_time)
    TextView tv_time;
    /**
     * 开始或停止倒计时按钮
     */
    @BindView(R.id.bt_time)
    Button bt_time;
    /**
     * 语音识别按钮
     */
    @BindView(R.id.ib_speech)
    ImageView ib_speech;
    /**
     * 温度控制条
     */
    @BindView(R.id.seekBar_temp)
    SeekBar tempSeekBar;
    /**
     * 倒计时控制条
     */
    @BindView(R.id.time_seekBar)
    SeekBar timeSeekBar;
    /**
     * 计时器增加按钮
     */
    @BindView(R.id.iv_time_add)
    ImageView iv_time_add;
    /**
     * 计时器减少按钮
     */
    @BindView(R.id.iv_time_minus)
    ImageView iv_time_minus;

    public ActivityUiRecog() {
        apiParams = new NluRecogParams();
    }

    protected Map<String, Object> fetchParams() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //  上面的获取是为了生成下面的Map， 自己集成时可以忽略
        Map<String, Object> params = apiParams.fetch(sp);
        //  集成时不需要上面的代码，只需要params参数。
        return params;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiParams.initSamplePath(this);
        setContentView(R.layout.activity_control_home);
        //绑定ButterKnife
        ButterKnife.bind(this);
        addStatusBar(this, R.id.ll_control, R.color.statusBar_color);
    }
}
