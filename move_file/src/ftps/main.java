package ftps;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPSClient;

public class main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FTPSClient ftpClient = new FTPSClient();
		try {
			ftpClient.connect("10.253.46.177");
			ftpClient.login("client1", "c1234567890");
			System.out.print("connect");
		
			
			
		
			ftpClient.logout();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
