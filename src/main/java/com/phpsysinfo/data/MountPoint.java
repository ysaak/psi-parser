package com.phpsysinfo.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MountPoint {

	private final String name;

	private final long used;
	private final long total;
	private final int percent;

	private final String fsType;
	private final String mountPoint;
	private final int iNodes;
	private final List<String> options = new ArrayList<String>();

	public void addOption(String option) {
	  options.add(option);
	}
}
