package com.actein.zxing.model;

public enum EquipmentType
{
    UNKNOWN,
    HTC_VIVE,
    HTC_VIVE_WITH_SUBPACK,
    PLAYSTATION_VR;

    public static EquipmentType convertToEquipmentType(String eventType)
    {
        if (eventType.trim().toLowerCase().contains("htc vive"))
        {
            if (eventType.trim().toLowerCase().contains("subpac"))
            {
                return HTC_VIVE_WITH_SUBPACK;
            }
            else
            {
                return HTC_VIVE;
            }
        }
        else if (eventType.trim().toLowerCase().contains("playstation"))
        {
            return PLAYSTATION_VR;
        }
        return UNKNOWN;
    }
}
