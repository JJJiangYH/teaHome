package com.tea.teahome.Control.recog;

public interface IStatus {
    int STATUS_NONE = 2;

    int STATUS_READY = 3;
    int STATUS_SPEAKING = 4;
    int STATUS_RECOGNITION = 5;

    int STATUS_FINISHED = 6;
    int STATUS_LONG_SPEECH_FINISHED = 7;

    int WHAT_MESSAGE_STATUS = 9001;
}
