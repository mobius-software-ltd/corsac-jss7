package org.restcomm.protocols.ss7.m3ua;

public class RoutingKey 
{
	private Integer dpc;
	private Integer opc;
	private Integer si;
	
	public RoutingKey(Integer dpc,Integer opc,Integer si)
	{
		this.dpc=dpc;
		this.opc=opc;
		this.si=si;
	}

	public Integer getDpc() {
		return dpc;
	}

	public Integer getOpc() {
		return opc;
	}

	public Integer getSi() {
		return si;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dpc == null) ? 0 : dpc.hashCode());
		result = prime * result + ((opc == null) ? 0 : opc.hashCode());
		result = prime * result + ((si == null) ? 0 : si.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoutingKey other = (RoutingKey) obj;
		if (dpc == null) {
			if (other.dpc != null)
				return false;
		} else if (!dpc.equals(other.dpc))
			return false;
		if (opc == null) {
			if (other.opc != null)
				return false;
		} else if (!opc.equals(other.opc))
			return false;
		if (si == null) {
			if (other.si != null)
				return false;
		} else if (!si.equals(other.si))
			return false;
		return true;
	}	
}
