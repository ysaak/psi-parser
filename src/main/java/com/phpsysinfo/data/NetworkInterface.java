package com.phpsysinfo.data;

import lombok.Data;

@Data
public class NetworkInterface {
	private final String name;
	private final long rxBytes;
	private final long txBytes;
	private final int errors;
	private final int drops;
	private final String information;
}
