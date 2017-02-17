package com.actein.transport.mqtt;

public class Topics
{
    public static final String FACTORY = "factory";
    public static final String FACTORY_ALL = "factory/#";

    public static final String BOOTH_ID = "boothId";
    public static final String EMB_DEVICE_ID = "emb_dev_id";

    public static final String PC_ONLINE_STATUS = "factory/booths/" + BOOTH_ID + "/pc/status";
    public static final String EMB_DEVICE_ONLINE_STATUS = "factory/embDevice/" + EMB_DEVICE_ID + "/status";
}
