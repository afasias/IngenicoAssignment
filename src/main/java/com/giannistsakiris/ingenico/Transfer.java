package com.giannistsakiris.ingenico;

import java.util.Date;

public class Transfer {

	private int id;
	private Date date;
	private int sourceAccountId;
	private int destinationAccountId;
	private double amount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getSourceAccountId() {
		return sourceAccountId;
	}

	public void setSourceAccountId(int sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}

	public int getDestinationAccountId() {
		return destinationAccountId;
	}

	public void setDestinationAccountId(int destinationAccountId) {
		this.destinationAccountId = destinationAccountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
