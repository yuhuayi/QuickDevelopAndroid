package com.bean;

import java.io.Serializable;

/**
 * Created by victor on 2014/10/15 0015.
 */
public class WheelViewBean implements Serializable {
    private static final long serialVersionUID = 1 << 2L;
    public String id;
    public String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
