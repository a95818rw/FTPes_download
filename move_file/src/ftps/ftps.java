package ftps;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;
 
/**
 * A program demonstrates how to upload files from local computer to a remote
 * FTP server using Apache Commons Net API.
 * @author www.codejava.net
 */
public class ftps {
 
	public static void main(String[] args) {
		
		Logger logger = Logger.getLogger(ftps.class.getName());
		FileHandler fileHandler;
		
		Date date = new Date();
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yy.MM.dd_hh.mm.ss");	   
		String str_date = bartDateFormat.format(date);
		
		String ip;
		int port;
		String user;
		String pw;
		String target;
		String downloadpath;
		String filenameExtension;
		String[] extensions = null;
		boolean folderIsNull = true;
		
		Options options = new Options();
		options.addOption("ip", true, "ip");
		options.addOption("port", true, "port");
		options.addOption("user", true, "user");
		options.addOption("pw", true, "pw");
		options.addOption("target", true, "target");
		options.addOption("downloadpath", true, "path");
		options.addOption("filenameExtension", true, "filenameExtension");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;
		
		FTPSClient ftpsClient = new FTPSClient("TLS", true);
		ftpsClient.setControlEncoding("UTF-8");

		try {
			fileHandler = new FileHandler("D:/temp/SystemOut_" + str_date + ".log");
			fileHandler.setLevel(Level.INFO);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();  
			fileHandler.setFormatter(formatter);

			try {
				cmd = parser.parse( options, args);
				ip = cmd.getOptionValue("ip");
				port = Integer.parseInt(cmd.getOptionValue("port"));
				user = cmd.getOptionValue("user");
				pw = cmd.getOptionValue("pw");
				target = cmd.getOptionValue("target");
				downloadpath = cmd.getOptionValue("downloadpath");
				filenameExtension = cmd.getOptionValue("filenameExtension");
				extensions = filenameExtension.split(";");
				
				if(
					target.contains("\\") || target.contains("|") || target.contains("\"") ||
					target.contains("<") || target.contains(":") || target.contains(">") ||
					target.contains("*") || target.contains("?")
				) logger.severe("The target is illegal.");
				
				Path p = Paths.get(downloadpath);
				if (Files.exists(p)) {
				} else {
					Files.createDirectory(p);
				}
				
				ftpsClient.connect(ip, port);
				ftpsClient.execPBSZ(0);
				ftpsClient.execPROT("P");
				
				ftpsClient.login(user, pw);
				if(ftpsClient.getReplyCode() == 530) {
					logger.severe("Login or password incorrect!");
				} else {
					ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
					
					ftpsClient.changeWorkingDirectory(target);
					if(ftpsClient.getReplyCode() == 550) {
						logger.severe("CWD failed. :directory not found.");
					} else {
						FTPFile[] files = ftpsClient.listFiles();
						ArrayList<String> files_name = new ArrayList<String>();
						
						logger.info("Start to download file.");
						
						for (FTPFile ftpFile : files) {
							for(String extension : extensions) {
								if(ftpFile.isFile() && StringUtils.substringAfterLast(ftpFile.getName(), ".").equals(extension)) {
									logger.info("downloading file : " + ftpFile.getName());
									File downloadFile = new File(downloadpath + "/" + ftpFile.getName());
									files_name.add(ftpFile.getName());
									ftpFile.setName(target + "/" + ftpFile.getName());
									String remoteFile1 = ftpFile.getName();
									OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
									ftpsClient.retrieveFile(remoteFile1, outputStream);
									outputStream.close();
									folderIsNull = false;
								}
							}
						}
						
						if(folderIsNull == true) {
							logger.warning("The folder is null.");
						}
						
						logger.info("Finish~");
					}
					
				}

				
			} catch (Exception e) {
				logger.severe(e.toString());
			} finally {
				try {
					if (ftpsClient.isConnected()) {
						ftpsClient.logout();
						ftpsClient.disconnect();
					}
				} catch (IOException ex) {
					logger.severe(ex.toString());
				}
			}
			
		} catch (Exception e) {
			logger.severe(e.toString());
		}
	}
}


//bin txt