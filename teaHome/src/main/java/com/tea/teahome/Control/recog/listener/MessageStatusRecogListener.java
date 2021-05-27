package com.tea.teahome.Control.recog.listener;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tea.teahome.Control.recog.RecogResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fujiayi on 2017/6/16.
 */
public class MessageStatusRecogListener extends StatusRecogListener {
    private static final String TAG = "MesStatusRecogListener";
    private final Handler handler;

    public MessageStatusRecogListener(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onAsrReady() {
        super.onAsrReady();
        sendStatusMessage(null);
    }

    @Override
    public void onAsrBegin() {
        super.onAsrBegin();
        sendStatusMessage(null);
    }

    @Override
    public void onAsrEnd() {
        super.onAsrEnd();
        sendMessage();
    }

    @Override
    public void onAsrPartialResult(String[] results, RecogResult recogResult) {
        super.onAsrPartialResult(results, recogResult);
        sendStatusMessage(results[0]);
    }

    @Override
    public void onAsrFinalResult(String[] results, RecogResult recogResult) {
        super.onAsrFinalResult(results, recogResult);
        String message = results[0];
        sendStatusMessage(message);
        sendMessage(message, status, true);
    }

    @Override
    public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage, RecogResult recogResult) {
        super.onAsrFinishError(errorCode, subErrorCode,
                descMessage, recogResult);
        String message = "";
        switch (subErrorCode) {
            case 7001:
                message = "没有声音，请重新尝试。";
                break;
            case 2100:
                message = "网络不可用，请重新尝试。";
                break;
        }
        sendStatusMessage(message);
        sendMessage(message, status, true);
    }

    @Override
    public void onAsrOnlineNluResult(String nluResult) {
        super.onAsrOnlineNluResult(nluResult);
        if (!nluResult.isEmpty()) {
            JSONObject json;
            try {
                json = new JSONObject(nluResult);
                sendStatusMessage(
                        json.optString("parsed_text"));
                Log.e("reg", json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAsrFinish(RecogResult recogResult) {
        super.onAsrFinish(recogResult);
        sendStatusMessage(null);
    }

    /**
     * 长语音识别结束
     */
    @Override
    public void onAsrLongFinish() {
        super.onAsrLongFinish();
        sendStatusMessage("长语音识别结束。");
    }

    /**
     * 使用离线命令词时，有该回调说明离线语法资源加载成功
     */
    @Override
    public void onOfflineLoaded() {
        sendStatusMessage("离线资源加载成功。没有此回调可能离线语法功能不能使用。");
    }

    /**
     * 使用离线命令词时，有该回调说明离线语法资源加载成功
     */
    @Override
    public void onOfflineUnLoaded() {
        sendStatusMessage("离线资源卸载成功。");
    }

    @Override
    public void onAsrExit() {
        super.onAsrExit();
        sendStatusMessage(null);
    }

    private void sendStatusMessage(String message) {
        sendMessage(message, status);
    }

    private void sendMessage() {
        sendMessage(null, WHAT_MESSAGE_STATUS);
    }

    private void sendMessage(String message, int what) {
        sendMessage(message, what, false);
    }

    private void sendMessage(String message, int what, boolean highlight) {
        if (handler == null) {
            Log.i(TAG, message);
            return;
        }
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = status;
        if (highlight) {
            msg.arg2 = 1;
        }
        if (message == null) {
            msg.obj = "";
        } else {
            msg.obj = message + "\n";
        }
        handler.sendMessage(msg);
    }
}
