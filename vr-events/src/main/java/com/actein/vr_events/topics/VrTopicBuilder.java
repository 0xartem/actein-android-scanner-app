package com.actein.vr_events.topics;

import com.actein.transport.mqtt.Topics;

public class VrTopicBuilder
{
    public VrTopicBuilder setToGameAll()
    {
        mTopicBuilder = new StringBuilder(VrTopics.VR_PC_GAME_ALL);
        return this;
    }

    public VrTopicBuilder setToGameOn()
    {
        mTopicBuilder = new StringBuilder(VrTopics.VR_PC_TURN_GAME_ON);
        return this;
    }

    public VrTopicBuilder setToGameOff()
    {
        mTopicBuilder = new StringBuilder(VrTopics.VR_PC_TURN_GAME_OFF);
        return this;
    }

    public VrTopicBuilder setToGameStatus()
    {
        mTopicBuilder = new StringBuilder(VrTopics.VR_PC_GAME_STATUS);
        return this;
    }

    public VrTopicBuilder setAllBooths()
    {
        setBoothIdOrWildcard("+");
        return this;
    }

    public VrTopicBuilder setBoothId(Integer boothId)
    {
        setBoothIdOrWildcard(boothId.toString());
        return this;
    }

    public String build()
    {
        return mTopicBuilder.toString();
    }

    private void setBoothIdOrWildcard(String value)
    {
        int boothIdIdx = mTopicBuilder.indexOf(Topics.BOOTH_ID);
        if (boothIdIdx != -1)
        {
            mTopicBuilder.replace(boothIdIdx, boothIdIdx + Topics.BOOTH_ID.length(), value);
        }
    }

    private StringBuilder mTopicBuilder = new StringBuilder();
}
