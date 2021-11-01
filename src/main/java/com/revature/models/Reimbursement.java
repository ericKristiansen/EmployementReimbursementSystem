package com.revature.models;

import java.sql.Blob;
import java.sql.Timestamp;


public class Reimbursement {

	private int id;
	private int amount;
	private Timestamp submittedTimestamp;
	private Timestamp resolvedTimestamp;
	private String description;
	private Blob recieptBlob;
	private int authorId;
	private int resolverId;
	private int reimbStatusId;
	private int typeId;
	
	public Reimbursement() {
		this.id = -1;
	}

	public Reimbursement(int id, int amount, Timestamp submittedTimestamp, Timestamp resolvedTimestamp,
			String description, Blob recieptBlob, int authorId, int resolverId, int reimbStatusId, int typeId) {
		super();
		this.id = id;
		this.amount = amount;
		this.submittedTimestamp = submittedTimestamp;
		this.resolvedTimestamp = resolvedTimestamp;
		this.description = description;
		this.recieptBlob = recieptBlob;
		this.authorId = authorId;
		this.resolverId = resolverId;
		this.reimbStatusId = reimbStatusId;
		this.typeId = typeId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Timestamp getSubmittedTimestamp() {
		return submittedTimestamp;
	}

	public void setSubmittedTimestamp(Timestamp submittedTimestamp) {
		this.submittedTimestamp = submittedTimestamp;
	}

	public Timestamp getResolvedTimestamp() {
		return resolvedTimestamp;
	}

	public void setResolvedTimestamp(Timestamp resolvedTimestamp) {
		this.resolvedTimestamp = resolvedTimestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Blob getRecieptBlob() {
		return recieptBlob;
	}

	public void setRecieptBlob(Blob recieptBlob) {
		this.recieptBlob = recieptBlob;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getResolverId() {
		return resolverId;
	}

	public void setResolverId(int resolverId) {
		this.resolverId = resolverId;
	}

	public int getReimbStatusId() {
		return reimbStatusId;
	}

	public void setReimbStatusId(int reimbStatusId) {
		this.reimbStatusId = reimbStatusId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

}
