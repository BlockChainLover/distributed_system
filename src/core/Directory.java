package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystemException;

import javax.annotation.processing.FilerException;

public class Directory {
	private File root;
	
	public Directory(String path){
		root = new File(path);
	}
	
	public boolean putFile(String path, File file){
		
		return true;
	}
	
	public boolean deleteFile(String path) throws FileSystemException{
		File file = new File(root,path);
		boolean res = file.delete();
		if(!res)
			throw new FileSystemException("Can't delete : "+file.getName());
		return true;
	}
	
	public boolean deleteDirectory(String path) throws FileSystemException{
		File dir = new File(path);
		if(dir.isDirectory())
		{
			for(File file : dir.listFiles()){
				if(!deleteDirectory(file.getAbsolutePath()))
					throw new FileSystemException("Can't delete : "+file.getName());
			}
			deleteFile(dir.getAbsolutePath());
			
			return true;
		}
		else 
		{
			return deleteFile(path);
		}
	}
	
	public boolean copyFile(String source, String destination) throws IOException{
		File src = new File(root, source);
		File dest = new File(root, destination);		
		
		InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(src);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
		return true;
	}
	
	public boolean moveFile(String source, String dest) throws IOException{
		boolean tmp = copyFile(source, dest);
		return tmp && deleteFile(source);
	}
}
