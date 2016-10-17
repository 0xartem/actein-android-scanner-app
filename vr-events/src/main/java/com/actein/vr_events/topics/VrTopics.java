package com.actein.vr_events.topics;

import static com.actein.transport.mqtt.Topics.BOOTH_ID;

class VrTopics
{
    static final String VR_PC_GAME_ALL = "factory/booths/" + BOOTH_ID + "/pc/vr/game/#";
    static final String VR_PC_TURN_GAME_ON = "factory/booths/" + BOOTH_ID + "/pc/vr/game/on";
    static final String VR_PC_TURN_GAME_OFF = "factory/booths/" + BOOTH_ID + "/pc/vr/game/off";

    static final String VR_PC_GAME_STATUS = "factory/booths/" + BOOTH_ID + "/pc/vr/game/status";
}
