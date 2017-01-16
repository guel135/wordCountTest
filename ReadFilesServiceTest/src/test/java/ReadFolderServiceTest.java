

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import org.junit.Test;

import com.agtinternational.entities.Directory;
import com.agtinternational.service.ReadFolderService;
import com.agtinternational.service.impl.ReadFolderServiceImpl;

public class ReadFolderServiceTest {
	
	ReadFolderService service;

	@Test (expected=NoSuchFileException.class)
	public void readNotExistingDirectory() throws IOException {
		service=new ReadFolderServiceImpl();
		service.read("/dog/miau","txt", 100, 100);
	}
	@Test 
	public void readExistingDirectoryWithTxtExtension() throws IOException {
		service=new ReadFolderServiceImpl();
		
		
		File file=new File("resources");
		String absolutePath=file.getAbsolutePath();
		System.out.println(absolutePath);
		System.out.println(file.toString());
		
		Directory directory=service.read(absolutePath,"txt", 500, 100);
		System.out.println(directory);
		
	}

}
