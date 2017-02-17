package com.actein.mvp.model;

import java.util.Comparator;

public final class VrStationComparators
{
    private VrStationComparators()
    {
    }

    public static Comparator<VrStation> getBoothIdComparator()
    {
        return new BoothIdComparator();
    }

    public static Comparator<VrStation> getEquipmentComparator()
    {
        return new EquipmentComparator();
    }

    public static Comparator<VrStation> getStartStopComparator()
    {
        return new StartStopComparator();
    }

    public static Comparator<VrStation> getOnlineStatusComparator()
    {
        return new OnlineStatusComparator();
    }

    public static Comparator<VrStation> getExperienceComparator()
    {
        return new ExperienceComparator();
    }

    public static Comparator<VrStation> getTimeLeftComparator()
    {
        return new TimeLeftComparator();
    }

    private static class BoothIdComparator implements Comparator<VrStation>
    {
        @Override
        public int compare(final VrStation left, final VrStation right)
        {
            return left.getBoothId() - right.getBoothId();
        }
    }

    private static class EquipmentComparator implements Comparator<VrStation>
    {
        @Override
        public int compare(final VrStation left, final VrStation right)
        {
            return left.getEquipment().compareTo(right.getEquipment());
        }
    }

    private static class StartStopComparator implements Comparator<VrStation>
    {
        @Override
        public int compare(final VrStation left, final VrStation right)
        {
            return left.getRunningIcon() - right.getRunningIcon();
        }
    }

    private static class OnlineStatusComparator implements Comparator<VrStation>
    {
        @Override
        public int compare(final VrStation left, final VrStation right)
        {
            return left.getOnlineIcon() - right.getOnlineIcon();
        }
    }

    private static class ExperienceComparator implements Comparator<VrStation>
    {
        @Override
        public int compare(final VrStation left, final VrStation right)
        {
            return left.getExperience().compareTo(right.getExperience());
        }
    }

    private static class TimeLeftComparator implements Comparator<VrStation>
    {
        @Override
        public int compare(final VrStation left, final VrStation right)
        {
            if (left.getTime() < right.getTime())
                return -1;
            else if (left.getTime() > right.getTime())
                return 1;
            return 0;
        }
    }
}
