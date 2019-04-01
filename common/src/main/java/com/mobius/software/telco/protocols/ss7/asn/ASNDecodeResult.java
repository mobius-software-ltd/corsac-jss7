package com.mobius.software.telco.protocols.ss7.asn;

public class ASNDecodeResult 
{
	private Object result;
	private Boolean hadErrors;
	
	public ASNDecodeResult(Object result,Boolean hadErrors) {
		this.result=result;
		this.hadErrors=hadErrors;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Boolean getHadErrors() {
		return hadErrors;
	}

	public void setHadErrors(Boolean hadErrors) {
		this.hadErrors = hadErrors;
	}		
}
