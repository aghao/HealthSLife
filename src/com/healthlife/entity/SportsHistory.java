package com.healthlife.entity;

import java.util.List;

public class SportsHistory {

	public SportsHistory() {
		// TODO �Զ����ɵĹ��캯�����
	}
	
	private int accountID;
	private List<Sports> history;
	
	protected int getAccountID() {
		return accountID;
	}
	protected void setAccountID(int accountID) {
		this.accountID = accountID;
	}
	protected List<Sports> getHistory() {
		return history;
	}
	protected void setHistory(List<Sports> history) {
		this.history = history;
	}

}
