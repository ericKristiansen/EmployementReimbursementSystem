package com.revature.models.display;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.imageio.ImageIO;

import com.revature.models.Reimbursement;

public class ReimbursementDisplay {

	
	public ReimbursementDisplay(Reimbursement r)
	{
		this.recieptImage = blobToImage(r.getRecieptBlob());
		
	}
	
	private int reimbId;
	private double amount;
	private Timestamp submittedTimestamp;
	private Timestamp resolvedTimestamp;
	private String description;
	private BufferedImage recieptImage;
	private int authorId;
	private int resolverId;
	private int reimbStatusId;
	private int typeId;
	
	
	
	private BufferedImage blobToImage(Blob blob) {
		InputStream in = null;
		BufferedImage tmp = null;
		try {
			in = blob.getBinaryStream();
			tmp = ImageIO.read(in);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return tmp;
	}
	
	
	
	
}
