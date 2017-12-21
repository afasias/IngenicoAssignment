package com.giannistsakiris.ingenico.tests;

import java.util.ArrayList;
import java.util.List;

import com.giannistsakiris.ingenico.Account;
import com.giannistsakiris.ingenico.Context;
import com.giannistsakiris.ingenico.Transfer;
import com.giannistsakiris.ingenico.exceptions.AccountNotFoundException;
import com.giannistsakiris.ingenico.exceptions.InsufficientBalanceException;

import junit.framework.TestCase;

public class TransferTest extends TestCase {

	private Account srcAccount;
	private Account dstAccount;

	public TransferTest() {
		srcAccount = new Account();
		srcAccount.setName("Source Account");
		srcAccount.setBalance(10000.0d);
		Context.getInstance().create(srcAccount);
		dstAccount = new Account();
		dstAccount.setName("Source Account");
		dstAccount.setBalance(10000.0d);
		Context.getInstance().create(dstAccount);
	}

	public void testValidTransfer() throws InsufficientBalanceException, AccountNotFoundException {
		Transfer transfer = new Transfer();
		transfer.setSourceAccountId(srcAccount.getId());
		transfer.setDestinationAccountId(dstAccount.getId());
		transfer.setAmount(25.0d);
		Context.getInstance().makeTransfer(transfer);
		assertEquals("source account has correct balance", 9975.0d, srcAccount.getBalance());
		assertEquals("destination account has correct balance", 10025.0d, dstAccount.getBalance());
	}

	public void testOverdrawn() throws AccountNotFoundException {
		Transfer transfer = new Transfer();
		transfer.setSourceAccountId(srcAccount.getId());
		transfer.setDestinationAccountId(dstAccount.getId());
		transfer.setAmount(12500.0d);
		try {
			Context.getInstance().makeTransfer(transfer);
			fail("should have thrown InsufficientBalanceException");
		} catch (InsufficientBalanceException e) {
			//
		}
		assertEquals("source account has correct balance", 10000.0d, srcAccount.getBalance());
		assertEquals("destination account has correct balance", 10000.0d, dstAccount.getBalance());
	}

	public void testSameAccount() throws AccountNotFoundException {
		Transfer transfer = new Transfer();
		transfer.setSourceAccountId(srcAccount.getId());
		transfer.setDestinationAccountId(srcAccount.getId());
		transfer.setAmount(10.0d);
		try {
			Context.getInstance().makeTransfer(transfer);
			fail("should have thrown RuntimeException");
		} catch (Exception e) {
			//
		}
	}

	public void testConcurrency() throws AccountNotFoundException {

		Transfer transfer = new Transfer();
		transfer.setSourceAccountId(srcAccount.getId());
		transfer.setDestinationAccountId(dstAccount.getId());
		transfer.setAmount(1.0d);

		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Context.getInstance().makeTransfer(transfer);
					} catch (Exception e) {
						//
					}
				}
			});
			threads.add(thread);
		}
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				return;
			}
		}

		assertEquals("source account has correct balance", 0.0d, srcAccount.getBalance());
		assertEquals("destination account has correct balance", 20000.0d, dstAccount.getBalance());
	}
}
