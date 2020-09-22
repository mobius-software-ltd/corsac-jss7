package com.mobius.software.telco.protocols.ss7.asn;

import java.util.concurrent.ConcurrentHashMap;

public interface ASNDecodeHandler 
{
	public void postProcessElement(Object element,ConcurrentHashMap<Integer,Object> data);
	
	public void preProcessElement(Object element,ConcurrentHashMap<Integer,Object> data);	    
}