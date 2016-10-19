package com.actein.zxing.qr;

import android.content.Context;
import android.widget.Toast;

import com.actein.vr_events.VrGameOffProtos;
import com.actein.vr_events.VrGameOnProtos;
import com.actein.vr_events.VrGameStatusProtos;
import com.actein.vr_events.interfaces.VrEventsHandler;

public class TestVrEventsHandler implements VrEventsHandler
{
    public TestVrEventsHandler(Context appContext)
    {
        mAppContext = appContext;
    }
    public void handleVrGameOnEvent(VrGameOnProtos.VrGameOnEvent event)
    {
        Toast.makeText(mAppContext, "The On event received", Toast.LENGTH_SHORT).show();
    }

    public void handleVrGameOffEvent(VrGameOffProtos.VrGameOffEvent event)
    {
        Toast.makeText(mAppContext, "The Off event received", Toast.LENGTH_SHORT).show();
    }

    public void handleVrGameStatusEvent(VrGameStatusProtos.VrGameStatusEvent event)
    {
        Toast.makeText(mAppContext,
                       "Status event received: " + event.getStatus().toString(),
                       Toast.LENGTH_SHORT).show();
    }

    private Context mAppContext;
}
