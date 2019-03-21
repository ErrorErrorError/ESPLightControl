package com.errorerrorerror.esplightcontrol.Async;
//This is for async
public class SetSwitchClass {
    String connectivity;
    long id;

    Boolean bool;

    public SetSwitchClass(String connectivity, long id) {
        this.connectivity = connectivity;
        this.id = id;
    }

    public SetSwitchClass(Boolean bool, long id)
    {
        this.id = id;
        this.bool = bool;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public long getId() {
        return id;
    }

    public Boolean getBool() {
        return bool;
    }
}
