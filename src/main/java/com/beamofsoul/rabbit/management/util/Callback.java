package com.beamofsoul.rabbit.management.util;

@FunctionalInterface
public interface Callback {
	
	public void doCallback(Object... objects); 
}
