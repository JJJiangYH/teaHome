package com.tea.teahome.Control.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import com.tea.teahome.R;

import java.util.ArrayList;

import butterknife.OnClick;

import static com.tea.teahome.Control.util.ConvertUtil.getArabicFromChinese;
import static com.tea.teahome.Control.util.ConvertUtil.isArabicNum;
import static com.tea.teahome.Control.util.ConvertUtil.isChineseNum;

public class ControlActivity extends AbstractRecogActivity implements View.OnClickListener {
    public ControlActivity() {
        super(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tempSeekBar.setOnSeekBarChangeListener(this);
        timeSeekBar.setOnSeekBarChangeListener(this);

        onClick(this.findViewById(R.id.ib_set_temp_100));
        changeTimerMax();
    }


    @SuppressLint("NewApi")
    @OnClick({R.id.ib_set_temp_50, R.id.ib_set_temp_65, R.id.ib_set_temp_85,
            R.id.ib_set_temp_100, R.id.ib_speech, R.id.bt_time, R.id.iv_time_add,
            R.id.iv_time_minus, R.id.bt_state_change, R.id.tv_hard_status})
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
            case "hard_open":
                hardClose();
                break;
            case "hard_close":
                hardOpen();
                break;
            default:
                break;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    protected void doRecogAction() {
        if (nluResult == null || nluResult.length() == 0) {
            return;
        }

        String[] recog = nluResult.split("[ 。，]");

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText("");

        for (String s : recog) {
            textView2.append(s + "\\");
        }

        String toDo = null;
        String thing = null;
        int num = 0;
        int unit = -1;

        for (String s : recog) {
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
        tv_speech_message.append("\n");

        if (toDo == null || thing == null) {
            tv_speech_message.append("无法理解您的意思。");
            return;
        }

        int time = num * unit;
        String error = "无法理解您的意思。";

        if (thing.equals(TIMER)) {
            switch (toDo) {
                case OPEN:
                    //只有unit
                    if (time == 0 && unit > 0) {
                        tv_speech_message.append(error);
                        return;
                    }
                    //只有num
                    if (time < 0) {
                        tv_speech_message.append(error);
                        return;
                    }
                    break;
                case SET:
                    //全没有，或只有一个
                    if (time <= 0) {
                        tv_speech_message.append(error);
                        return;
                    }
                    break;
                case CLOSE:
                    //只有unit
                    if (time == 0 && unit > 0) {
                        tv_speech_message.append(error);
                        return;
                    }
                    //只有num
                    if (time < 0) {
                        tv_speech_message.append(error);
                        return;
                    }
                    //全都有
                    if (time > 0) {
                        tv_speech_message.append(error);
                        return;
                    }
                    break;
                default:
                    tv_speech_message.append(error);
                    break;
            }
        }

        if (thing.equals(TEMP)) {
            if (SET.equals(toDo)) {//全有/没有，或只有unit
                if (time >= 0) {
                    tv_speech_message.append(error);
                    return;
                }
            } else {
                tv_speech_message.append(error);
                return;
            }
        }

        doRecogAction(toDo, thing, num, unit);
    }

    @SuppressLint("NewApi")
    protected void doRecogAction(String toDo, String thing, int num, int unit) {
        int time = num * unit;
        if (OPEN.equals(toDo)) {
            switch (thing) {
                case TIMER:
                    if (isTimerRunning) {
                        //计时器正在运行
                        tv_speech_message.append("无法开启，倒计时未关闭。");
                    } else {
                        //计时器未运行
                        int progress = timeSeekBar.getProgress();
                        //num，unit都没有
                        if (num == 0) {
                            if (progress == 0) {
                                tv_speech_message.append("无法开启，时间不能为零。");
                            } else {
                                timerStart();
                                tv_speech_message.append("已开启" + progress + "分钟的倒计时。");
                            }
                        } else if (time > timeSeekBar.getMax() || time < timeSeekBar.getMin()) {
                            tv_speech_message.append("无法开启，时间不正确。");
                        } else {
                            timeSeekBar.setProgress(time);
                            timerStart();
                            tv_speech_message.append("已开启" + timeSeekBar.getProgress() + "分钟的倒计时。");
                        }
                    }
                    break;
                default:
                    break;
            }
        } else if (CLOSE.equals(toDo)) {
            switch (thing) {
                case TIMER:
                    if (isTimerRunning) {
                        timerStop();
                        tv_speech_message.append("倒计时已关闭");
                    } else {
                        tv_speech_message.append("无法关闭，倒计时未开启。");
                    }
                    break;
                default:
                    break;
            }
        } else if (SET.equals(toDo)) {
            switch (thing) {
                case TIMER:
                    if (isTimerRunning) {
                        tv_speech_message.append("无法设置，倒计时正在进行。");
                    } else {
                        timeSeekBar.setProgress(time);
                        tv_speech_message.append("成功设置" + time + "分钟的倒计时");
                    }
                    break;
                case TEMP:
                    if (num < tempSeekBar.getMin() || num > tempSeekBar.getMax()) {
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
        }
    }
}