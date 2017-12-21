package com.giannistsakiris.ingenico;

import com.giannistsakiris.ingenico.exceptions.InsufficientBalanceException;

public class Account {

	private int id;
	private String name;
	private volatile double balance;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getBalance() {
		return balance;
	}

	public synchronized void setBalance(double balance) {
		if (balance < 0) {
			throw new IllegalArgumentException("balance: " + balance);
		}
		this.balance = balance;
	}

	public synchronized void withdraw(double amount) throws InsufficientBalanceException {
		if (amount < 0) {
			throw new IllegalArgumentException("amount: " + amount);
		}
		if (balance < amount) {
			throw new InsufficientBalanceException();
		}
		balance -= amount;
	}

	public synchronized void deposit(double amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount: " + amount);
		}
		balance += amount;
	}
}
