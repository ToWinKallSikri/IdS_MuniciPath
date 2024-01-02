package Synk.Api.Controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

public class FileHandler {
	
	private final String url = "./files/";
	
	public String getName(MultipartFile file) throws IOException {
		String name = getActualName();
		File newFile = new File(url + name);
		newFile.createNewFile();
		FileOutputStream fileOut = new FileOutputStream(newFile);
		fileOut.write(file.getBytes());
		fileOut.close();
		return name;
	}
	
	public Resource getFile(String name) throws MalformedURLException {
		Path path = Paths.get(url + name);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) 
        	return resource; 
		else return null;
	}
	
	private String getActualName() {
		return "" + new Date().getTime();
	}
	
	
	
}