package com.actein.scanner.booth;


import java.util.ArrayList;
import java.util.List;

public class VRBoothsInfo {

    public VRBoothsInfo(CharSequence commonText) {
        for (Integer i = 1; i <= boothsCount; i++) {
            boothsList.add(commonText + " " + i.toString());
        }
    }

    public List<CharSequence> getBoothsNames() {
        return boothsList;
    }

    private static final int boothsCount = 16;
    private List<CharSequence> boothsList = new ArrayList<>();

}
