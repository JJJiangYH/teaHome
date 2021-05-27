package com.tea.teahome.Control.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.hc.mixthebluetooth.fragment.FragmentMessage;
import com.tea.teahome.Control.recog.MyRecognizer;
import com.tea.teahome.Control.recog.listener.ChainRecogListener;
import com.tea.teahome.Control.recog.listener.MessageStatusRecogListener;
import com.tea.teahome.R;
import com.tea.teahome.UI.BaiduASRDigitalDialog;
import com.tea.teahome.UI.DigitalDialogInput;

import java.util.Map;

/**
 * 识别的基类Activity。 ActivityCommon定义了通用的UI部分
 * 封装了识别的大部分逻辑，包括MyRecognizer的初始化，资源释放
 * <p>
 * <p>
 * 集成流程代码，只需要一句： myRecognizer.start(params);具体示例代码参见startRough()
 * =》.实例化 myRecognizer   new MyRecognizer(this, listener);
 * =》 实例化 listener  new MessageStatusRecogListener(null);
 * </p>
 * 集成文档： http://ai.baidu.com/docs#/ASR-Android-SDK/top 集成指南一节
 * demo目录下doc_integration_DOCUMENT
 * ASR-INTEGRATION-helloworld  ASR集成指南-集成到helloworld中 对应 ActivityMiniRecog
 * ASR-INTEGRATION-TTS-DEMO ASR集成指南-集成到合成DEMO中 对应 ActivityUiRecog
 * <p>
 * 大致流程为
 * 1. 实例化MyRecognizer ,调用release方法前不可以实例化第二个。参数中需要开发者自行填写语音识别事件的回调类，实现开发者自身的业务逻辑
 * 2. 如果使用离线命令词功能，需要调用loadOfflineEngine。在线功能不需要。
 * 3. 根据识别的参数文档，或者demo中测试出的参数，组成json格式的字符串。调用 start 方法
 * 4. 在合适的时候，调用release释放资源。
 * <p>
 */
public abstract class AbstractRecogActivity extends ActivityUiRecog
        implements SeekBar.OnSeekBarChangeListener {
    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;
    /**
     * 本Activity中是否需要调用离线命令词功能。根据此参数，判断是否需要调用SDK的ASR_KWS_LOAD_ENGINE事件
     */
    protected boolean enableOffline;
    /**
     * 对话框界面的输入参数
     */
    protected DigitalDialogInput input;
    protected ChainRecogListener chainRecogListener;
    /**
     * 之前点击的按钮
     */
    protected ImageButton lastPressed;
    protected FragmentMessage mMessage;

    /**
     * @param enableOffline 展示的activity是否支持离线命令词
     */
    public AbstractRecogActivity(boolean enableOffline) {
        this.enableOffline = enableOffline;
        chainRecogListener = new ChainRecogListener();
        chainRecogListener.addListener(new MessageStatusRecogListener(handler));
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     * 基于DEMO集成2.1, 2.2 设置识别参数并发送开始事件
     */
    protected void start() {
        final Map<String, Object> params = fetchParams();
        // BaiduASRDigitalDialog的输入参数
        input = new DigitalDialogInput(myRecognizer, chainRecogListener, params);
        BaiduASRDigitalDialog.setInput(input); // 传递input信息，在BaiduASRDialog中读取,
        Intent intent = new Intent(this, BaiduASRDigitalDialog.class);
        running = true;
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!running) {
            myRecognizer.release();
        }
    }

    @Override
    protected void onResume() {
        // 基于DEMO集成第1.1, 1.2, 1.3 步骤 初始化EventManager类并注册自定义输出事件
        // DEMO集成步骤 1.2 新建一个回调类，识别引擎会回调这个类告知重要状态和识别结果
        // DEMO集成步骤 1.1 1.3 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例,并注册输出事件
        if (!running) {
            if (myRecognizer != null) {
                myRecognizer.release();
            }
            myRecognizer = new MyRecognizer(this, chainRecogListener);
        }
        super.onResume();
    }

    /**
     * 销毁时需要释放识别资源。
     */
    @Override
    protected void onDestroy() {
        // 如果之前调用过myRecognizer.loadOfflineEngine()， release()里会自动调用释放离线资源
        // 基于DEMO5.1 卸载离线资源(离线时使用) release()方法中封装了卸载离线资源的过程
        // 基于DEMO的5.2 退出事件管理器
        myRecognizer.release();
        super.onDestroy();
    }

    /**
     * @param seekBar  滑动条实例
     * @param progress 滑动条数值
     * @param fromUser 是否是来自用户的更改
     * @author jiang yuhang
     * @date 2021-04-11 19:18
     **/
    @SuppressLint({"DefaultLocale", "NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBar_temp:
                tv_temp_set.setText("设置温度：" + getTemp() + TEMP_UNIT);
                TEMP_NOW = getTemp();
                setPressed(lastPressed, false);
                lastPressed = null;
                break;
            case R.id.time_seekBar:
                tv_time.setText("倒计时：" + progress + "分");
                break;
            default:
                break;
        }
    }

    /**
     * 设置button按钮的按下状态
     *
     * @param button  ImageButton
     * @param pressed 为true，将button设置为按下状态
     * @author jiang yuhang
     * @date 2021-04-13 15:03
     **/
    @SuppressLint("UseCompatLoadingForDrawables")
    public void setPressed(ImageButton button, boolean pressed) {
        if (lastPressed != null) {
            if (pressed) {
                button.setBackground(getResources().getDrawable(R.drawable.item_button_temp_background_onclick));
                button.setSelected(true);
            } else {
                button.setBackground(getResources().getDrawable(R.drawable.item_button_temp_background));
                button.setSelected(false);
            }
        }
    }

    /**
     * 开始触摸
     *
     * @author jiang yuhang
     * @date 2021-04-13 19:16
     **/
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    /**
     * 停止触摸
     *
     * @author jiang yuhang
     * @date 2021-04-13 19:16
     **/
    @SuppressLint("DefaultLocale")
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        mMessage.setSendData(String.format("SET%d;", getTemp()));
//        Log.e("TEMP", String.format("SET%d", getTemp()));
    }
}