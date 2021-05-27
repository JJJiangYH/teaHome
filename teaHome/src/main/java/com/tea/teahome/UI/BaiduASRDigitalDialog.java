package com.tea.teahome.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tea.teahome.R;

import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import static android.view.View.inflate;

/**
 * 语音识别对话框
 *
 * @author yangliang02
 */
@SuppressLint("Registered")
public class BaiduASRDigitalDialog extends BaiduASRDialog {
    public static final String PARAM_DIALOG_THEME = "BaiduASRDigitalDialog_theme";
    /**
     * 对话框启动后展示引导提示，不启动识别
     */
    public static final String PARAM_SHOW_TIPS_ON_START = "BaiduASRDigitalDialog_showTips";
    /**
     * 引擎启动后3秒没检测到语音，在动效下方随机出现一条提示语。在配置了提示语列表后，默认开启。
     */
    public static final String PARAM_SHOW_TIP = "BaiduASRDigitalDialog_showTip";
    /**
     * 提示语列表。String数组
     */
    public static final String PARAM_TIPS = "BaiduASRDigitalDialog_tips";
    protected static final int ERROR_NONE = 0;
    private static final String TAG = "BSDigitalDialog";
    // 国际化标识定义Begin
    private static final String KEY_TIPS_STATE_READY = "tips.state.ready";
    private static final String KEY_TIPS_STATE_WAIT = "tips.state.wait";
    private static final String KEY_TIPS_STATE_INITIALIZING = "tips.state.initializing";
    private static final String KEY_TIPS_STATE_LISTENING = "tips.state.listening";
    private static final String KEY_TIPS_STATE_RECOGNIZING = "tips.state.recognizing";
    private static final String KEY_TIPS_WAITNET = "tips.wait.net";
    private static final String KEY_BTN_DONE = "btn.done";
    private static final String KEY_BTN_RETRY = "btn.retry";
    private static final String KEY_TIPS_HELP_TITLE = "tips.help.title";
    private static final String KEY_BTN_START = "btn.start";
    private static final String KEY_BTN_HELP = "btn.help";
    private static final String KEY_TIPS_PREFIX = "tips.suggestion.prefix";
    private static final int ERROR_NETWORK_UNUSABLE = 0x90000;
    // 国际化标识定义end
    /**
     * 识别中的进度条
     */
    private static final int BAR_ONEND = 0;
    private static final int BAR_ONFINISH = 1;
    // 识别启动后间隔多长时间不说话出现提示，单位毫秒
    private static final long SHOW_SUGGESTION_INTERVAL = 3000;
    /**
     * “说完了”按钮背景
     */
    private final StateListDrawable mButtonBg = new StateListDrawable();
    /**
     * 左侧按钮背景
     */
    private final StateListDrawable mLeftButtonBg = new StateListDrawable();
    /**
     * 右侧按钮背景
     */
    private final StateListDrawable mRightButtonBg = new StateListDrawable();
    /**
     * 帮助按钮
     */
    private final StateListDrawable mHelpButtonBg = new StateListDrawable();
    private final Handler mHandler = new Handler();
    private final Random mRandom = new Random();
    private final OnClickListener mClickListener;
    Message mMessage = Message.obtain();
    private int mErrorCode;
    private View mMainLayout;
    private View mErrorLayout;
    private TextView mTipsTextView;
    private TextView mWaitNetTextView;
    private TextView mCompleteTextView;
    private TextView mCancelTextView;
    private TextView mRetryTextView;
    private SDKAnimationView mVoiceWaveView;
    private ImageButton mHelpBtn;
    private TextView mTitle;
    private View mHelpView;
    private TipsAdapter mTipsAdapter;
    /**
     * 动效下面的提示，3S不说话出现，文字在列表中随机出。出现后隐藏版权声明
     */
    private TextView mSuggestionTips;
    private View mRecognizingView;
    /**
     * 连续上屏控件
     */
    private EditText mInputEdit;
    private SDKProgressBar mSDKProgressBar;
    private int step = 0;
    // 3秒不出识别结果，显示网络不稳定,15秒转到重试界面
    private int delayTime = 0;
    private Drawable mBg;
    /**
     * 按钮文字颜色
     */
    private ColorStateList mButtonColor;
    /**
     * 按钮文字颜色反色
     */
    private ColorStateList mButtonReverseColor;
    /**
     * 底部版本声明字体颜色
     */
    private int mCopyRightColor = 0;
    /**
     * 状态提示字体 颜色
     */
    private int mStateTipsColor = 0;
    /**
     * 错误提示字体颜色
     */
    private int mErrorTipsColor = 0;
    private int mTheme = 0;
    /**
     * 国际化文本资源
     */
    private ResourceBundle mLableRes;
    private final Runnable mShowSuggestionTip = this::showSuggestionTips;
    /**
     * 单条提示语前缀
     */
    private String mPrefix;
    /**
     * 进度条
     */
    @SuppressLint("HandlerLeak")
    Handler barHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == BAR_ONEND) {
                {
                    if (delayTime >= 3000) {
                        if (mInputEdit.getVisibility() == VISIBLE) {
                            mInputEdit.setVisibility(INVISIBLE);
                        }

                        mTipsTextView.setVisibility(INVISIBLE);
                        // 仅在线时显示“网络不稳定”
                        // 当前活跃的引擎类型
                        mWaitNetTextView.setText(getString(KEY_TIPS_WAITNET));
                        mWaitNetTextView.setVisibility(VISIBLE);
                    } else {
                        if (mInputEdit.getVisibility() == VISIBLE) {
                            mTipsTextView.setVisibility(INVISIBLE);
                            mWaitNetTextView.setVisibility(INVISIBLE);
                        } else {
                            mTipsTextView.setVisibility(VISIBLE);
                            mWaitNetTextView.setVisibility(INVISIBLE);
                        }
                    }
                    mMessage.what = BAR_ONEND;
                    if (step <= 30) {
                        delayTime = delayTime + 10;
                        step = step + 1;
                        barHandler.sendEmptyMessageDelayed(BAR_ONEND, 10);
                    } else if (step < 60) {
                        delayTime = delayTime + 100;
                        step = step + 1;
                        barHandler.sendEmptyMessageDelayed(BAR_ONEND, 100);
                    } else {

                        if (delayTime >= 15000) {
                            cancelRecognition();
                            onFinish(SpeechRecognizer.ERROR_NETWORK, ERROR_NETWORK_UNUSABLE);
                            step = 0;
                            delayTime = 0;
                            mSDKProgressBar.setVisibility(INVISIBLE);
                            barHandler.removeMessages(BAR_ONEND);
                        } else {
                            step = 60;
                            delayTime = delayTime + 100;
                            barHandler.sendEmptyMessageDelayed(BAR_ONEND, 100);
                        }
                    }
                    mSDKProgressBar.setProgress(step);
                }
            } else if (msg.what == BAR_ONFINISH) {
                if (step <= 80) {
                    step = step + 3;
                    barHandler.sendEmptyMessageDelayed(BAR_ONFINISH, 1);
                } else {
                    step = 0;
                    delayTime = 0;
                    mInputEdit.setVisibility(GONE);
                    mSDKProgressBar.setVisibility(INVISIBLE);
                    if (mErrorCode == ERROR_NONE) {
                        finish();
                    }
                    barHandler.removeMessages(BAR_ONFINISH);
                }
                mSDKProgressBar.setProgress(step);
            }
        }
    };

    public BaiduASRDigitalDialog() {
        mClickListener = v -> {
            if ("speak_complete".equals(v.getTag())) {
                String btnTitle = mCompleteTextView.getText().toString();
                if (btnTitle.equals(getString(KEY_BTN_START))) {
                    step = 0;
                    // 3秒不出识别结果，显示网络不稳定,15秒转到重试界面
                    delayTime = 0;
                    mSDKProgressBar.setVisibility(INVISIBLE);
                    startRecognition();
                } else if (btnTitle.equals(getString(KEY_BTN_DONE))) {
                    if (status == STATUS_Speaking) {
                        speakFinish();
                        onEndOfSpeech();
                    } else {
                        cancelRecognition();
                        onFinish(SpeechRecognizer.ERROR_NO_MATCH, 0);
                    }
                }
            } else if ("cancel_text_btn".equals(v.getTag())) {
                String btntitle = mCancelTextView.getText().toString();
                if (btntitle.equals(getString(KEY_BTN_HELP))) {
                    showSuggestions();
                } else {
                    finish();
                }
            } else if ("retry_text_btn".equals(v.getTag())) {
                step = 0;
                // 3秒不出识别结果，显示网络不稳定,15秒转到重试界面
                delayTime = 0;
                mInputEdit.setVisibility(GONE);
                mSDKProgressBar.setVisibility(INVISIBLE);
                startRecognition();
            } else if ("cancel_btn".equals(v.getTag())) {
                finish();
            } else if ("help_btn".equals(v.getTag())) {
                showSuggestions();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent params = getIntent();
        if (params != null) {
            mTheme = params.getIntExtra(PARAM_DIALOG_THEME, mTheme);
        }
        initView();
        loadI18N();
        startRecognition();
        internalOnStart();
    }

    private void initView() {
        initResources();
        View mContentRoot = inflate(this,
                getResources().getIdentifier("bdspeech_digital_layout", "layout", getPackageName()), null);
        if (mContentRoot != null) {
            mContentRoot.findViewWithTag("bg_layout").setBackgroundDrawable(mBg);
            mTipsTextView = mContentRoot.findViewWithTag("tips_text");
            mTipsTextView.setTextColor(mStateTipsColor);
            mWaitNetTextView = mContentRoot.findViewWithTag("tips_wait_net");
            mWaitNetTextView.setVisibility(INVISIBLE);
            mWaitNetTextView.setTextColor(mStateTipsColor);
            mSuggestionTips = mContentRoot.findViewWithTag("suggestion_tips");
            mSuggestionTips.setTextColor(mCopyRightColor);

            TextView mSuggestionTips2 = mContentRoot.findViewWithTag("suggestion_tips_2");
            mSuggestionTips2.setTextColor(mCopyRightColor);
            // 进度条
            mSDKProgressBar = mContentRoot.findViewWithTag("progress");
            mSDKProgressBar.setVisibility(INVISIBLE);

            mCompleteTextView = mContentRoot.findViewWithTag("speak_complete");
            mCompleteTextView.setOnClickListener(mClickListener);
            mCompleteTextView.setBackgroundDrawable(mButtonBg);
            mCompleteTextView.setTextColor(mButtonReverseColor);

            mCancelTextView = mContentRoot.findViewWithTag("cancel_text_btn");
            mCancelTextView.setOnClickListener(mClickListener);
            mCancelTextView.setBackgroundDrawable(mLeftButtonBg);
            mCancelTextView.setTextColor(mButtonColor);
            mRetryTextView = mContentRoot.findViewWithTag("retry_text_btn");
            mRetryTextView.setOnClickListener(mClickListener);

            mRetryTextView.setBackgroundDrawable(mRightButtonBg);
            mRetryTextView.setTextColor(mButtonReverseColor);

            TextView mErrorTipsTextView = mContentRoot.findViewWithTag("error_tips");
            mErrorTipsTextView.setTextColor(mErrorTipsColor);
            @SuppressLint("UseCompatLoadingForDrawables") Drawable bgDrawable = getResources().getDrawable(
                    getResources().getIdentifier("bdspeech_close_v2", "drawable", getPackageName()));
            ImageButton mCancelBtn = mContentRoot.findViewWithTag("cancel_btn");
            mCancelBtn.setOnClickListener(mClickListener);
            mCancelBtn.setImageDrawable(bgDrawable);
            mHelpBtn = mContentRoot.findViewWithTag("help_btn");
            mHelpBtn.setOnClickListener(mClickListener);
            mHelpBtn.setImageDrawable(mHelpButtonBg);
            mErrorLayout = mContentRoot.findViewWithTag("error_reflect");
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mErrorLayout
                    .getLayoutParams();
            // mContentRoot.findViewWithTag("main_reflect").setId(0x7f0c0087);
            // mContentRoot.findViewWithTag("main_reflect").setBackgroundColor(Color.RED);
            layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.dialog_linear);
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.dialog_linear);

            mVoiceWaveView = mContentRoot.findViewWithTag("voicewave_view");
            mVoiceWaveView.setThemeStyle();
            mMainLayout = mContentRoot.findViewWithTag("main_reflect");
            mVoiceWaveView.setVisibility(INVISIBLE);
            mRecognizingView = mContentRoot.findViewWithTag("recognizing_reflect");
            mHelpView = mContentRoot.findViewWithTag("help_reflect");
            layoutParams = (RelativeLayout.LayoutParams) mHelpView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.dialog_linear);
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.dialog_linear);
            mTitle = mContentRoot.findViewWithTag("help_title");
            mTitle.setTextColor(mStateTipsColor);
            ListView suggestions = mContentRoot.findViewWithTag("suggestions_list");
            mTipsAdapter = new TipsAdapter(this);
            mTipsAdapter.setNotifyOnChange(true);
            mTipsAdapter.setTextColor(mStateTipsColor);
            suggestions.setAdapter(mTipsAdapter);
            mInputEdit = mContentRoot.findViewWithTag("partial_text");
            mInputEdit.setTextColor(mStateTipsColor);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(new View(this));
            ViewGroup.LayoutParams param = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addContentView(mContentRoot, param);
//            setContentView(mContentRoot);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        // 设置主题色调，不如亮蓝、暗红、亮绿等
        adjustThemeColor();
    }

    /**
     * 根据选定的主题，设置色调
     */
    private void adjustThemeColor() {
        float hue = -108;
        ColorMatrix cm = new ColorMatrix();
        ColorFilterGenerator.adjustColor(cm, 0, 0, 0, hue);
        ColorFilter filter = new ColorMatrixColorFilter(cm);
        mBg.setColorFilter(filter);
        mButtonBg.setColorFilter(filter);
        mLeftButtonBg.setColorFilter(filter);
        mRightButtonBg.setColorFilter(filter);
        mSDKProgressBar.setHsvFilter(filter);
        mVoiceWaveView.setHsvFilter(filter);
    }

    /**
     * 全屏显示提示列表
     */
    private void showSuggestions() {
        mErrorLayout.setVisibility(INVISIBLE);
        mMainLayout.setVisibility(VISIBLE);
        mRecognizingView.setVisibility(INVISIBLE);
        mHelpView.setVisibility(VISIBLE);
        mCompleteTextView.setText(getString(KEY_BTN_START));
        mCompleteTextView.setEnabled(true);
        mHelpBtn.setVisibility(INVISIBLE);
        mHandler.removeCallbacks(mShowSuggestionTip);
        cancelRecognition();
    }

    /**
     * 显示动效正文的提示
     */
    @SuppressLint("SetTextI18n")
    private void showSuggestionTips() {
        String tips = mTipsAdapter.getItem(mRandom.nextInt(mTipsAdapter.getCount()));
        mSuggestionTips.setText(mPrefix + tips);
        mSuggestionTips.setVisibility(VISIBLE);
    }

    @SuppressLint("NewApi")
    protected void internalOnStart() {
        mTipsAdapter.clear();
        String[] temp = getParams().getStringArray(PARAM_TIPS);
        if (temp != null) {
            mTipsAdapter.addAll(temp);
        }
        boolean showTips = false;
        if (mTipsAdapter.getCount() > 0) {
            mHelpBtn.setVisibility(VISIBLE);
            showTips = getParams().getBoolean(PARAM_SHOW_TIPS_ON_START, false);
        } else {
            mHelpBtn.setVisibility(INVISIBLE);
        }
        if (showTips) {
            showSuggestions();
        }
    }

    /**
     * 加载国际化字符串，{{@link #initView()}之后调用
     */
    private void loadI18N() {
        try {
            mLableRes = ResourceBundle.getBundle("BaiduASRDigitalDialog");
            mRetryTextView.setText(getString(KEY_BTN_RETRY));
            mTitle.setText(getString(KEY_TIPS_HELP_TITLE));
            mPrefix = getString(KEY_TIPS_PREFIX);
        } catch (MissingResourceException e) {
            Log.w(TAG, "loadI18N error", e);
        }
    }

    /**
     * 获取国际化字符串
     *
     * @return 资源不存在返回Null
     */
    private String getString(String key) {
        String label = null;
        if (mLableRes != null) {
            try {
                label = mLableRes.getString(key);
            } catch (Exception e) {
                Log.w(TAG, "get internationalization error key:" + key, e);
            }
        }
        return label;
    }

    /**
     * 初始化资源，图片、颜色
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initResources() {
        // 配色方案选择
        int buttonRecognizingBgName;
        final int buttonNormalBgName =
                getResources().getIdentifier("bdspeech_btn_normal", "drawable", getPackageName());
        final int buttonPressedBgName =
                getResources().getIdentifier("bdspeech_btn_pressed", "drawable", getPackageName());
        int leftButtonNormalBgName;
        int leftButtonPressedBgName;
        final int rightButtonNormalBgName =
                getResources().getIdentifier("bdspeech_right_normal", "drawable", getPackageName());
        final int rightButtonPressedBgName =
                getResources().getIdentifier("bdspeech_right_pressed", "drawable", getPackageName());
        int bgName;
        // 按下、不可用、其它状态颜色
        int[] colors = new int[3];
        // 按下、不可用、其它状态颜色
        int[] colorsReverse = new int[3];
        bgName =
                getResources().getIdentifier("bdspeech_digital_bg", "drawable", getPackageName());
        leftButtonNormalBgName =
                getResources().getIdentifier("bdspeech_left_normal", "drawable", getPackageName());
        leftButtonPressedBgName =
                getResources().getIdentifier("bdspeech_left_pressed", "drawable", getPackageName());

        buttonRecognizingBgName =
                getResources().getIdentifier("bdspeech_btn_recognizing", "drawable", getPackageName());

        colors[0] = 0xff474747;
        colors[1] = 0xffe8e8e8;
        colors[2] = 0xff474747;
        colorsReverse[0] = 0xffffffff;
        colorsReverse[1] = 0xffbebebe;
        colorsReverse[2] = 0xffffffff;

        mCopyRightColor = 0xffd7d7d7;
        mStateTipsColor = 0xff696969;
        mErrorTipsColor = 0xff6a6a6a;
        mHelpButtonBg.addState(new int[]{android.R.attr.state_pressed}, getResources().getDrawable(
                getResources().getIdentifier("bdspeech_help_pressed_light", "drawable", getPackageName())));
        mHelpButtonBg.addState(new int[]{}, getResources().getDrawable(
                getResources().getIdentifier("bdspeech_help_light", "drawable", getPackageName())));

        mBg = getResources().getDrawable(bgName);
        mButtonBg.addState(new int[]{
                android.R.attr.state_pressed, android.R.attr.state_enabled
        }, getResources().getDrawable(buttonPressedBgName));
        mButtonBg.addState(new int[]{
                -android.R.attr.state_enabled
        }, getResources().getDrawable(buttonRecognizingBgName));
        mButtonBg.addState(new int[]{},
                getResources().getDrawable(buttonNormalBgName));
        mLeftButtonBg.addState(new int[]{
                android.R.attr.state_pressed
        }, getResources().getDrawable(leftButtonPressedBgName));
        mLeftButtonBg.addState(new int[]{},
                getResources().getDrawable(leftButtonNormalBgName));
        mRightButtonBg.addState(new int[]{
                android.R.attr.state_pressed
        }, getResources().getDrawable(rightButtonPressedBgName));
        mRightButtonBg.addState(new int[]{},
                getResources().getDrawable(rightButtonNormalBgName));
        int[][] states = new int[3][];
        states[0] = new int[]{
                android.R.attr.state_pressed, android.R.attr.state_enabled
        };
        states[1] = new int[]{
                -android.R.attr.state_enabled
        };
        states[2] = new int[1];

        mButtonColor = new ColorStateList(states, colors);
        mButtonReverseColor = new ColorStateList(states, colorsReverse);
    }

    private void stopRecognizingAnimation() {
        mVoiceWaveView.resetAnimation();
    }

    private void startRecognizingAnimation() {
        mVoiceWaveView.startRecognizingAnimation();
    }

    @Override
    protected void onRecognitionStart() {
        barHandler.removeMessages(BAR_ONFINISH);
        barHandler.removeMessages(BAR_ONEND);
        step = 0;
        // 3秒不出识别结果，显示网络不稳定,15秒转到重试界面
        delayTime = 0;
        mInputEdit.setText("");
        mInputEdit.setVisibility(INVISIBLE);
        mVoiceWaveView.setVisibility(VISIBLE);
        mVoiceWaveView.startInitializingAnimation();
        mTipsTextView.setText(getString(KEY_TIPS_STATE_WAIT));
        mErrorLayout.setVisibility(INVISIBLE);
        mMainLayout.setVisibility(VISIBLE);
        mCompleteTextView.setText(getString(KEY_TIPS_STATE_INITIALIZING));
        mCompleteTextView.setEnabled(false);
        // mInputEdit.setVisibility(View.GONE);
        mTipsTextView.setVisibility(VISIBLE);
        mSDKProgressBar.setVisibility(INVISIBLE);
        mWaitNetTextView.setVisibility(INVISIBLE);

        mRecognizingView.setVisibility(VISIBLE);
        mHelpView.setVisibility(INVISIBLE);
        if (mTipsAdapter.getCount() > 0) {
            mHelpBtn.setVisibility(VISIBLE);
        }
        mSuggestionTips.setVisibility(GONE);
    }

    @Override
    protected void onPrepared() {
        mVoiceWaveView.startPreparingAnimation();
        if (TextUtils.isEmpty(mPrompt)) {
            mTipsTextView.setText(getString(KEY_TIPS_STATE_READY));
        } else {
            mTipsTextView.setText(mPrompt);
        }
        mCompleteTextView.setText(getString(KEY_BTN_DONE));
        mCompleteTextView.setEnabled(true);
        mHandler.removeCallbacks(mShowSuggestionTip);
        if (getParams().getBoolean(PARAM_SHOW_TIP, true) && mTipsAdapter.getCount() > 0) {
            mHandler.postDelayed(mShowSuggestionTip, SHOW_SUGGESTION_INTERVAL);
        }
    }

    @Override
    protected void onBeginningOfSpeech() {
        mTipsTextView.setText(getString(KEY_TIPS_STATE_LISTENING));
        mVoiceWaveView.startRecordingAnimation();
        mHandler.removeCallbacks(mShowSuggestionTip);
    }

    @Override
    protected void onVolumeChanged(float volume) {
        mVoiceWaveView.setCurrentDBLevelMeter(volume);
    }

    @Override
    protected void onEndOfSpeech() {
        mTipsTextView.setText(getString(KEY_TIPS_STATE_RECOGNIZING));
        mCompleteTextView.setText(getString(KEY_TIPS_STATE_RECOGNIZING));
        mSDKProgressBar.setVisibility(VISIBLE);

        barHandler.sendEmptyMessage(BAR_ONEND);
        mCompleteTextView.setEnabled(false);
        startRecognizingAnimation();
    }

    @Override
    protected void onFinish(int errorType, int errorCode) {
        mErrorCode = errorType;

        barHandler.removeMessages(BAR_ONEND);
        barHandler.sendEmptyMessage(BAR_ONFINISH);
        mWaitNetTextView.setVisibility(INVISIBLE);
        stopRecognizingAnimation();
        mVoiceWaveView.setVisibility(INVISIBLE);
    }

    @Override
    protected void onPartialResults(String[] results) {
        if (results != null) {
            if (results.length > 0) {
                if (mInputEdit.getVisibility() != VISIBLE) {
                    mInputEdit.setVisibility(VISIBLE);
                    mWaitNetTextView.setVisibility(INVISIBLE);
                    mTipsTextView.setVisibility(INVISIBLE);
                }
                mInputEdit.setText(results[0]);
                mInputEdit.setSelection(mInputEdit.getText().length());
                delayTime = 0;
            }
        }
    }
}