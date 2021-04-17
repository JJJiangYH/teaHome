package com.tea.teahome.Control.params;

import android.content.SharedPreferences;

import com.baidu.speech.asr.SpeechConstant;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by fujiayi on 2017/6/24.
 */

public class NluRecogParams extends CommonRecogParams {
    private static final String TAG = "NluRecogParams";

    public NluRecogParams() {
        super();
        stringParams.addAll(Arrays.asList(SpeechConstant.NLU, SpeechConstant.ASR_PUNCTUATION_MODE));
        intParams.addAll(Arrays.asList(SpeechConstant.DECODER, SpeechConstant.PROP));
        boolParams.addAll(Arrays.asList("_nlu_online"));
        boolParams.addAll(Arrays.asList(SpeechConstant.DISABLE_PUNCTUATION));
        // copyOfflineResource(context);
    }

    public Map<String, Object> fetch(SharedPreferences sp) {
        Map<String, Object> map = super.fetch(sp);
        return map;
    }
}
