package com.revature.dao.reimbursement;

import java.sql.Blob;
import java.sql.Timestamp;

public enum REIMBURSEMENT {

	//Reimbursement(int id, double amount, Timestamp submittedTimestamp, Timestamp resolvedTimestamp,
	//String description, Blob recieptBlob, int authorId, int resolverId, int reimbStatusId, int typeId)
	
	ID(1), AMOUNT(2), SUBMITTED_TIMESTAMP(3), RESOLVED_TIMESTAMP(4), DESCRIPTION(5), RECIEPT_BLOB(6), 
	AUTHOR_ID(7), RESOLVER_ID(8), STATUS_ID(9), TYPE_ID(10);

    private final int value;

    private REIMBURSEMENT(int value) {
        this.value = value;
    }

    public int value() { return this.value;}
}
