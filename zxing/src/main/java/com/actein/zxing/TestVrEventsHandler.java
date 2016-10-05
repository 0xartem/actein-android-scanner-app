package com.actein.zxing;

import android.content.Context;
import android.widget.Toast;

import com.actein.vr_events.VrGameOffProtos;
import com.actein.vr_events.VrGameOnProtos;
import com.actein.vr_events.interfaces.VrEventsHandler;

public class TestVrEventsHandler implements VrEventsHandler
{
    public TestVrEventsHandler(Context appContext)
    {
        mAppContext = appContext;
    }
    public void handleVrGameOnEvent(VrGameOnProtos.VrGameOnEvent event)
    {
        Toast.makeText(mAppContext, "On event", Toast.LENGTH_LONG).show();
    }

    public void handleVrGameOffEvent(VrGameOffProtos.VrGameOffEvent event)
    {
        Toast.makeText(mAppContext, "Off event", Toast.LENGTH_LONG).show();
    }

    private Context mAppContext;
}
