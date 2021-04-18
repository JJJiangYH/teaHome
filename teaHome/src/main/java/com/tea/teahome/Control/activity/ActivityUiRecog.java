package com.tea.teahome.Control.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tea.teahome.Control.params.CommonRecogParams;
import com.tea.teahome.Control.params.NluRecogParams;
import com.tea.teahome.Control.recog.IStatus;
import com.tea.teahome.R;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
     * 定时器
     */
    protected Timer timer;
    /**
     * 显示温度的tv
     */
    @BindViews({R.id.tv_set_temp_50, R.id.tv_set_temp_65, R.id.tv_set_temp_85, R.id.tv_set_temp_100,
            R.id.tv_temp_min_c, R.id.tv_temp_max_c})
    List<TextView> tv_set_temp;
    /**
     * 设置硬件的开关
     */
    @BindView(R.id.bt_state_change)
    Button bt_state_change;
    /**
     * 硬件状态显示
     */
    @BindView(R.id.tv_hard_status)
    TextView tv_hard_status;
    /**
     * 显示温度的文本框
     */
    @BindView(R.id.tv_temp_set)
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
    /**
     * 时间
     */
    private long time;
    /**
     * 倒计时Handler
     */
    protected final Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (time >= 0) {
                tv_time.setText(String.format("倒计时：%s%02d秒", time >= 60 ? (time / 60 + "分") : "", (time % 60)));
                if (time % 60 == 0) {
                    timeSeekBar.setProgress((int) (time / 60));
                }
                time--;
            } else {
                timerStop();
                tv_time.setText("倒计时：结束");
            }
        }
    };

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

    public void changeTempUnit(String tempUnit) {
        if (tempUnit.equals("C")) {
            TEMP_UNIT = getString(R.string.temp_c);
            isTempUnitC = true;

        } else {
            TEMP_UNIT = getString(R.string.temp_f);
            isTempUnitC = false;
        }
        tv_temp_now.setText("设置温度：" + getTemp() + TEMP_UNIT);
        TEMP_NOW = getTemp();

        String[] temp_string_f = getResources().getStringArray(R.array.temps_f);
        String[] temp_string_c = getResources().getStringArray(R.array.temps_c);

        for (int i = 0; i < tv_set_temp.size(); i++) {
            tv_set_temp.get(i).setText(isTempUnitC ? temp_string_c[i++] : temp_string_f[i++]);
        }
    }

    protected int getTemp() {
        if (isTempUnitC) {
            return tempSeekBar.getProgress();
        } else {
            return (int) (32 + tempSeekBar.getProgress() * 1.8);
        }
    }

    /**
     * 打开计时器
     *
     * @author jiang yuhang
     * @date 2021-04-13 14:56
     **/
    public void timerStart() {
        isTimerRunning = true;
        time = timeSeekBar.getProgress() * 60L;
        timer = new Timer();
        bt_time.setText("停止计时");
        timeSeekBar.setEnabled(false);
        iv_time_add.setEnabled(false);
        iv_time_minus.setEnabled(false);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = timeHandler.obtainMessage();
                message.obj = time;
                timeHandler.sendMessage(message);
            }
        }, 0L, 1000L);
    }

    /**
     * 关闭计时器
     *
     * @author jiang yuhang
     * @date 2021-04-13 14:56
     **/
    public void timerStop() {
        isTimerRunning = false;
        tv_time.setText("倒计时：关闭");
        if (timer != null) {
            timer.cancel();
        }
        bt_time.setText("开始计时");
        timeSeekBar.setEnabled(true);
        iv_time_add.setEnabled(true);
        iv_time_minus.setEnabled(true);
    }

    /**
     * 更改时间微调键
     *
     * @param value 是否显示微调键
     * @author jiang yuhang
     * @date 2021-04-13 14:56
     **/
    protected void changeTimerMode(boolean value) {
        if (value) {
            if (iv_time_add.getVisibility() != VISIBLE) {
                iv_time_add.setVisibility(VISIBLE);
            }
            if (iv_time_minus.getVisibility() != VISIBLE) {
                iv_time_minus.setVisibility(VISIBLE);
            }
        } else {
            if (iv_time_minus.getVisibility() != GONE) {
                iv_time_minus.setVisibility(GONE);
            }
            if (iv_time_add.getVisibility() != GONE) {
                iv_time_add.setVisibility(GONE);
            }
        }
    }

    /**
     * 根据用户设置修改计时器时间
     *
     * @author jiang yuhang
     * @date 2021-04-13 17:58
     **/
    protected void changeTimerMax() {
        timeSeekBar.setMax(SEEKBAR_MAX);
        ((TextView) findViewById(R.id.time_max)).setText("" + SEEKBAR_MAX);
    }

    /**
     * 开启硬件
     *
     * @author jiang yuhang
     * @date 2021-04-18 17:39
     **/
    protected void hardOpen() {
        bt_state_change.setText("关闭");
        bt_state_change.setTag("hard_open");
        tv_hard_status.setText("已开启");
    }

    /**
     * 关闭硬件
     */
    protected void hardClose() {
        bt_state_change.setText("开启");
        bt_state_change.setTag("hard_close");
        tv_hard_status.setText("已关闭");
    }
}
