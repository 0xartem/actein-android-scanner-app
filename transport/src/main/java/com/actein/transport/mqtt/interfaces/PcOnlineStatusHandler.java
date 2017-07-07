package com.actein.transport.mqtt.interfaces;

import com.actein.transport.mqtt.OnlineStatusProtos;

public interface PcOnlineStatusHandler
{
    void onPcOnlineStatusChanged(int boothId, OnlineStatusProtos.OnlineStatus status);
}
