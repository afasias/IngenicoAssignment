package com.giannistsakiris.ingenico.rest;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.giannistsakiris.ingenico.Context;
import com.giannistsakiris.ingenico.Transfer;
import com.giannistsakiris.ingenico.exceptions.AccountNotFoundException;
import com.giannistsakiris.ingenico.exceptions.InsufficientBalanceException;
import com.sun.jersey.api.NotFoundException;

@Path("/Transfers")
public class TransferResource {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Transfer transfer) throws InsufficientBalanceException, AccountNotFoundException {
		Context.getInstance().makeTransfer(transfer);
		return Response.created(URI.create(Integer.toString(transfer.getId()))).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Transfer get(@PathParam("id") int transferId) {
		Transfer transfer = Context.getInstance().getTransferById(transferId);
		if (transfer != null) {
			return transfer;
		}
		throw new NotFoundException();
	}
}
