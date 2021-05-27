package com.tea.teahome.Control.params;

import android.content.SharedPreferences;

import com.baidu.speech.asr.SpeechConstant;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class NluRecogParams extends CommonRecogParams {
    public NluRecogParams() {
        super();
        stringParams.addAll(Arrays.asList(SpeechConstant.NLU, SpeechConstant.ASR_PUNCTUATION_MODE));
        intParams.addAll(Arrays.asList(SpeechConstant.DECODER, SpeechConstant.PROP));
        boolParams.addAll(Collections.singletonList("_nlu_online"));
        boolParams.addAll(Collections.singletonList(SpeechConstant.DISABLE_PUNCTUATION));
        // copyOfflineResource(context);
    }

    public Map<String, Object> fetch(SharedPreferences sp) {
        return super.fetch(sp);
    }
}
