package com.actein.zxing.model;

public enum EquipmentType
{
    UNKNOWN,
    HTC_VIVE,
    HTC_VIVE_WITH_SUBPACK,
    PLAYSTATION_VR;

    public static EquipmentType convertToEquipmentType(String eventType)
    {
        if (eventType.equals("HTC Vive"))
        {
            return HTC_VIVE;
        }
        else if (eventType.equals("HTC Vive with Subpack"))
        {
            return HTC_VIVE_WITH_SUBPACK;
        }
        else if (eventType.equals("Playstation VR"))
        {
            return PLAYSTATION_VR;
        }
        return UNKNOWN;
    }
}
