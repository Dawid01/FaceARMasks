package com.szczepaniak.covidmask;

import com.google.ar.core.exceptions.NotYetAvailableException;

public abstract class VideButtonListner {

    abstract void takePhoto() throws NotYetAvailableException;
    abstract void startRecording();
    abstract void stopRecording();
}
