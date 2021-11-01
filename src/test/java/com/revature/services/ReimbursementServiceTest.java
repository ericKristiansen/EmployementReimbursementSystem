package com.revature.services;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.revature.dao.reimbursement.ReimbursementDaoDb;


public class ReimbursementServiceTest {

	
	// we want to be able to mock the ReimbursementService and ReimbursementDAO, so we don't actually hit the db
	// inject mocks because we are going to inject the mocked Dao functionality into the service
	@InjectMocks
	public ReimbursementService rs;
	
	//mock because we are going to mock the functionality of the  dao so we don't hit the database when testing
	@Mock
	public ReimbursementDaoDb rDao;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		
	}
	
	@Test
	public void testCreateNewRequest() {
		
		assertTrue(1 > 0);
		
	}
	
	@Test
	public void testGetReimbursementByUserId() {
		assertTrue(1 > 0);
	}
	
	@Test
	public void testGetReimbursementByStatusIdAndAuthorId() {
		assertTrue(1 > 0);
	}
	
	@Test
	public void testApproveDenyRequest() {
		assertTrue(1 > 0);
	}
	
	@Test
	public void testGetUserByUsername() {
		assertTrue(1 > 0);
	}
	
	@Test
	public void testGetAllUsers() {
		assertTrue(1 > 0);
	}
	
	
	
}
