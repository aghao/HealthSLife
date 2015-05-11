package com.healthlife.entity;

import java.util.ArrayList;

public class IdRefer {
	



	public long localId;
	public long remoteId;
	
	public IdRefer(long localId,long remoteId)
	{
		this.localId=localId;
		this.remoteId=remoteId;
	}
	


	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}
	
	
	
}
