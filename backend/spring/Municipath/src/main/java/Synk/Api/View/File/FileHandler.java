package Synk.Api.View.File;


import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

public class FileHandler {
	
	private final String url = "./files/";
	
	public String getName(MultipartFile file) {
		String name = getActualName() + getExtension(file);
		File newFile = new File(url + name);
		try {
			newFile.createNewFile();
			FileOutputStream fileOut = new FileOutputStream(newFile);
			fileOut.write(file.getBytes());
			fileOut.close();
			return name;
		} catch (Exception e) {
			return name;
		}
	}
	
	public Resource getFile(String name) {
	    try {
	        Path file = Paths.get(url + name);
	        Resource resource = new UrlResource(file.toUri());
	        if (resource != null && resource.exists() && resource.isReadable()) {
                return resource;
            }
            return null;
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	private String getActualName() {
		return "" + new Date().getTime();
	}
	
	private String getExtension(MultipartFile file) {
		String [] data = file.getOriginalFilename().split("\\.");
		return data.length > 1 ? "." + data[data.length -1] : "";
	}
	
}
