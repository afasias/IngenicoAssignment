package com.giannistsakiris.ingenico.tests;

import com.giannistsakiris.ingenico.Account;
import com.giannistsakiris.ingenico.Context;

import junit.framework.TestCase;

public class AccountTest extends TestCase {

	public void testAccountCreation() {
		Account account = new Account();
		account.setName("Giannis Tsakiris");
		account.setBalance(100.0d);
		Context.getInstance().create(account);
		assertTrue("account received an id", account.getId() > 0);

		account = Context.getInstance().getAccountById(account.getId());
		assertNotNull("account could be found by id", account);
		assertEquals("retrieved account has correct name", "Giannis Tsakiris", account.getName());
		assertEquals("retrieved account has correct balance", 100.0d, account.getBalance());
	}

}
