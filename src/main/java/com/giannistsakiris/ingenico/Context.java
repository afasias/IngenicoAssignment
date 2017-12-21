package com.giannistsakiris.ingenico;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.giannistsakiris.ingenico.exceptions.AccountNotFoundException;
import com.giannistsakiris.ingenico.exceptions.InsufficientBalanceException;

public class Context {

	private static Context instance;

	private AtomicInteger nextAccountId = new AtomicInteger(1);
	private Map<Integer, Account> accounts = new ConcurrentHashMap<>();

	private AtomicInteger nextTransferId = new AtomicInteger(1);
	private Map<Integer, Transfer> transfers = new ConcurrentHashMap<>();

	private Context() {
	}

	public static Context getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (Context.class) {
			if (instance != null) {
				return instance;
			}
			return instance = new Context();
		}
	}

	public void create(Account account) {
		account.setId(nextAccountId.getAndIncrement());
		accounts.put(account.getId(), account);
	}

	public Collection<Account> getAllAccounts() {
		return accounts.values();
	}

	public Account getAccountById(int accountId) {
		return accounts.get(accountId);
	}

	public void makeTransfer(Transfer transfer) throws InsufficientBalanceException, AccountNotFoundException {

		Account sourceAccount = accounts.get(transfer.getSourceAccountId());
		if (sourceAccount == null) {
			throw new AccountNotFoundException();
		}
		
		if (sourceAccount.getId() == transfer.getDestinationAccountId()) {
			throw new RuntimeException("source and destination accounts must be different");
		}

		Account destinationAccount = accounts.get(transfer.getDestinationAccountId());
		if (destinationAccount == null) {
			throw new AccountNotFoundException();
		}

		// first withdraw the amount from the source account
		sourceAccount.withdraw(transfer.getAmount());

		// then try to deposit the amount into the destination account
		try {
			destinationAccount.deposit(transfer.getAmount());
		} catch (Exception e) {
			// if the deposit fails, deposit the amount back into source account
			sourceAccount.deposit(transfer.getAmount());
			throw e;
		}

		// if everything has gone well, then register the transfer
		transfer.setId(nextTransferId.getAndIncrement());
		transfer.setDate(new Date());
		transfers.put(transfer.getId(), transfer);
	}

	public Transfer getTransferById(int transferId) {
		return transfers.get(transferId);
	}
}
