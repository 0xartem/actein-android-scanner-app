package com.actein.transport.mqtt.policies;

enum QualityOfService
{
    AT_MOST_ONCE_DELIVERY_GUARANTEE,
    AT_LEAST_ONCE_DELIVERY_GUARANTEE,
    EXACTLY_ONCE_DELIVERY_GUARANTEE;

    public static int convertToMqttValues(QualityOfService qualityOfService)
    {
        switch (qualityOfService)
        {
        case AT_MOST_ONCE_DELIVERY_GUARANTEE:
            return 0;
        case AT_LEAST_ONCE_DELIVERY_GUARANTEE:
            return 1;
        case EXACTLY_ONCE_DELIVERY_GUARANTEE:
            return 2;
        default:
            throw new UnsupportedOperationException("Unknown quality of service type");
        }
    }
}
