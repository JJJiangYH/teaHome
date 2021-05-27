package com.tea.teahome.UI;

import com.tea.teahome.Control.recog.MyRecognizer;
import com.tea.teahome.Control.recog.listener.ChainRecogListener;

import java.util.Map;

public class DigitalDialogInput {
    private final MyRecognizer myRecognizer;

    private final ChainRecogListener listener;
    private final Map<String, Object> startParams;

    public DigitalDialogInput(MyRecognizer myRecognizer, ChainRecogListener listener, Map<String, Object> startParams) {
        if (myRecognizer == null) {
            throw new NullPointerException("myRecogizer param is null");
        }
        if (listener == null) {
            throw new NullPointerException("listener param is null");
        }
        if (startParams == null) {
            throw new NullPointerException("startParams param is null");
        }

        this.myRecognizer = myRecognizer;
        this.listener = listener;
        this.startParams = startParams;
    }

    public MyRecognizer getMyRecognizer() {
        return myRecognizer;
    }

    public ChainRecogListener getListener() {
        return listener;
    }

    public Map<String, Object> getStartParams() {
        return startParams;
    }
}
