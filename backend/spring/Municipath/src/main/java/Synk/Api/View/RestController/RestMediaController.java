package Synk.Api.View.RestController;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import Synk.Api.View.File.FileHandler;

@RestController
public class RestMediaController {
    
	private FileHandler fh;
	
	public RestMediaController() {
		this.fh = new FileHandler();
	}
    
	@GetMapping(value="/api/v1/media")
	public ResponseEntity<Object> getMedia(@RequestHeader(name="paths") List<String> paths) {
		List<Resource> list = paths.stream().map(s -> this.fh.getFile(s)).toList();
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}
	
	@PostMapping(value="/api/v1/path")
	public ResponseEntity<Object> getPath(@RequestBody List<MultipartFile> files) {
		List<String> list = files.stream().map(f -> this.fh.getName(f)).toList();
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}
	
}
