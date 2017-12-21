package com.giannistsakiris.ingenico.rest;

import java.net.URI;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.giannistsakiris.ingenico.Account;
import com.giannistsakiris.ingenico.Context;
import com.sun.jersey.api.NotFoundException;

@Path("/Accounts")
public class AccountResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Account> getAll() {
		return Context.getInstance().getAllAccounts();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Account account) {
		Context.getInstance().create(account);
		return Response.created(URI.create(Integer.toString(account.getId()))).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Account get(@PathParam("id") int accountId) {
		Account account = Context.getInstance().getAccountById(accountId);
		if (account != null) {
			return account;
		}
		throw new NotFoundException();
	}
}
