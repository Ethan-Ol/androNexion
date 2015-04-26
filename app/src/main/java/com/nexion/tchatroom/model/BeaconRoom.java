package com.nexion.tchatroom.model;

import java.util.List;

/**
 * Created by DarzuL on 26/04/2015.
 */
public class BeaconRoom extends Room {

    private final List<Beacon> beacons;

    public BeaconRoom(int id, String name, List<Beacon> beacons) {
        super(id, name);
        this.beacons = beacons;
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }
}
