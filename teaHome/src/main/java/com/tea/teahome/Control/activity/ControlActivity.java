package com.tea.teahome.Control.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.tea.teahome.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;

import static com.tea.teahome.Control.util.ConvertUtil.getArabicFromChinese;
import static com.tea.teahome.Control.util.ConvertUtil.isArabicNum;
import static com.tea.teahome.Control.util.ConvertUtil.isChineseNum;

public class ControlActivity extends AbstractRecogActivity
        implements View.OnClickListener {
    /**
     * 时间
     */
    private long time;
    /**
     * 定时器
     */
    private Timer timer;
    /**
     * 倒计时Handler
     */
    private final Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (time >= 0) {
                if (time <= 60) {
                    tv_time.setText("倒计时：" + time % 60 + "秒");
                } else {
                    tv_time.setText("倒计时：" + time / 60 + "分" + time % 60 + "秒");
                }
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

    public ControlActivity() {
        super(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tempSeekBar.setOnSeekBarChangeListener(this);
        timeSeekBar.setOnSeekBarChangeListener(this);

        changeTimerMax();
    }

    /**
     * 根据用户设置修改计时器时间
     *
     * @author jiang yuhang
     * @date 2021-04-13 17:58
     **/
    private void changeTimerMax() {
        timeSeekBar.setMax(SEEKBAR_MAX);
        time_max.setText("" + SEEKBAR_MAX);
    }

    /**
     * 更改时间微调键
     *
     * @param value 是否显示微调键
     * @author jiang yuhang
     * @date 2021-04-13 14:56
     **/
    private void changeTimerMode(boolean value) {
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

    @OnClick({R.id.ib_set_temp_50, R.id.ib_set_temp_65, R.id.ib_set_temp_85, R.id.ib_set_temp_100,
            R.id.ib_speech, R.id.bt_time, R.id.iv_time_add, R.id.iv_time_minus})
    public void onClick(View v) {
        if (v.getTag() == null) {
            return;
        }

        switch (v.getTag().toString()) {
            //温度设置按钮
            case "50":
            case "65":
            case "85":
            case "100":
                ImageButton button = (ImageButton) v;
                tempSeekBar.setProgress(Integer.parseInt(v.getTag().toString()));
                TEMP_NOW = tempSeekBar.getProgress();
                if (lastPressed != null) {
                    ControlActivity.this.setPressed(lastPressed, false);
                }
                lastPressed = button;
                ControlActivity.this.setPressed(button, true);
                break;
            //语音按钮
            case "speech":
                start();
                break;
            case "time":
                if (timeSeekBar.getProgress() == 0) {
                    return;
                }
                if (isTimerRunning) {
                    timerStop();
                } else {
                    timerStart();
                }
                break;
            case "time_add":
                if (timeSeekBar.getProgress() + 1 <= SEEKBAR_MAX) {
                    timeSeekBar.setProgress(timeSeekBar.getProgress() + 1);
                }
                break;
            case "time_minus":
                if (timeSeekBar.getProgress() - 1 >= timeSeekBar.getMin()) {
                    timeSeekBar.setProgress(timeSeekBar.getProgress() - 1);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 打开计时器
     *
     * @author jiang yuhang
     * @date 2021-04-13 14:56
     **/
    private void timerStart() {
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
    private void timerStop() {
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

    @Override
    protected void onResume() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SEEKBAR_MAX = Integer.parseInt(preferences.getString("control_timer_max", "60"));
        changeTimerMode(preferences.getBoolean("control_time_set", true));
        changeTimerMax();
        changeTempUnit(preferences.getString("temp_unit", getString(R.string.temp_unit_default)));

        super.onResume();
    }

    private void changeTempUnit(String tempUnit) {
        if (tempUnit.equals("C")) {
            TEMP_UNIT = getString(R.string.temp_c);
            isTempUnitC = true;

        } else {
            TEMP_UNIT = getString(R.string.temp_f);
            isTempUnitC = false;
        }
        tv_temp_now.setText("设置温度：" + getTemp() + TEMP_UNIT);
        TEMP_NOW = getTemp();

        for (int i = 0; i < tv_set_temp.size(); i++) {
            tv_set_temp.get(i).setText(isTempUnitC ? temp_string_c[i++] : temp_string_f[i++]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        running = false;
        if (requestCode == 2) {
            String message = "识别结果：";
            if (resultCode == RESULT_OK) {
                ArrayList<String> results = data.getStringArrayListExtra("results");
                if (results != null && results.size() > 0) {
                    message += results.get(0);
                }
            } else {
                message += "没有结果";
            }
            tv_speech_message.setText(message);
        }

        doRecogAction();
    }

    private void doRecogAction() {
        if (nluResult == null || nluResult.length() == 0) {
            return;
        }
        String[] recog = nluResult.split("[ 。，]");

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText("");
        for (String s : recog) {
            textView2.append(s);
        }

        String toDo = null;
        String thing = null;
        int num = -1;
        int unit = 0;

        for (String s :
                recog) {
            if (verb.containsKey(s)) {
                toDo = verb.get(s);
            } else if (noun.containsKey(s)) {
                thing = noun.get(s);
            } else if (isChineseNum(s)) {
                num = getArabicFromChinese(s);
            } else if (isArabicNum(s)) {
                num = Integer.parseInt(s);
            } else if (HOUR_TIME.contains(s)) {
                unit = 60;
            } else if (MIN_TIME.contains(s)) {
                unit = 1;
            }
        }

        textView2.append(toDo + " " + thing + " " + num + " " + unit);

        if (toDo == null || thing == null) {
            tv_speech_message.append("无法理解您的意思。");
            return;
        }
        if (unit * num <= 0 && thing.equals(TIMER) && !toDo.equals(CLOSE)) {
            tv_speech_message.append("无法理解您的意思。");
            return;
        }

        tv_speech_message.append("\n");
        doRecogAction(toDo, thing, num, unit);
    }

    private void doRecogAction(String toDo, String thing, int num, int unit) {
        switch (toDo) {
            case OPEN:
                switch (thing) {
                    case TIMER:
                        if (isTimerRunning) {//如果计时器正在运行
                            tv_speech_message.append("无法开启，倒计时未关闭。");
                        } else { //计时器未运行
                            int progress = timeSeekBar.getProgress();
                            if (num == -1) {
                                if (progress == 0) {
                                    tv_speech_message.append("无法开启，时间不能为零。");
                                } else {
                                    timerStart();
                                    tv_speech_message.append("已开启" + progress + "分钟的倒计时。");
                                }
                            } else if (num > timeSeekBar.getMax() || num < timeSeekBar.getMin()) {
                                tv_speech_message.append("无法开启，时间不正确。");
                            } else {
                                timeSeekBar.setProgress(num);
                                timerStart();
                                tv_speech_message.append("已开启" + timeSeekBar.getProgress() + "分钟的倒计时。");
                            }
                        }
                        break;
                    case TEMP:
                        break;
                    default:
                        break;
                }
                break;
            case CLOSE:
                switch (thing) {
                    case TIMER:
                        if (num == -1) {
                            if (isTimerRunning) {
                                timerStop();
                                tv_speech_message.append("倒计时已关闭");
                            } else {
                                tv_speech_message.append("无法关闭，倒计时未开启。");
                            }
                        } else {
                            tv_speech_message.append("无法理解您的意思。");
                        }
                        break;
                    case TEMP:
                        break;
                    default:
                        break;
                }
                break;
            case SET:
                switch (thing) {
                    case TIMER:
                        if (isTimerRunning) {
                            tv_speech_message.append("无法设置，倒计时正在进行。");
                        } else {
                            if (num == -1) {
                                tv_speech_message.append("无法理解您的意思。");
                            } else {
                                timeSeekBar.setProgress(num * unit);
                                tv_speech_message.append("成功设置" + num * unit + "分钟的倒计时");
                            }
                        }
                        break;
                    case TEMP:
                        if (num > 100 || num < 50) {
                            tv_speech_message.append("无法设置，温度不正确。");
                        } else {
                            tempSeekBar.setProgress(num);
                            if (TEMP_UNIT.equals("C")) {
                                tv_speech_message.append("成功设置" + num + getString(R.string.temp_c) + "温度");
                            } else {
                                tv_speech_message.append("成功设置" + (int) (num * 1.8 + 32) + getString(R.string.temp_f) + "温度");
                            }
                        }
                        break;
                }
                break;
            default:
                break;
        }
    }
}
