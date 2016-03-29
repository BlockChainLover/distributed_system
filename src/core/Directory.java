package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystemException;
import java.nio.file.NotDirectoryException;

public class Directory {
	
	private File root;
	
	private long burden = 0;
	
	public Directory(String path) throws NotDirectoryException{
		root = new File(path);
		if(root.isFile())
			throw new NotDirectoryException("");
		else if(!root.exists())
			root.mkdir();
	}
	
	public boolean putFile(String path, File file) throws IOException{
		File dest = new File(root, path);
		return copyFile(file.getAbsolutePath(), dest.getAbsolutePath());
	}
	
	public boolean createDir(String path){
		File dir = new File(root, path);
		return dir.mkdirs();
	}
	
	public boolean deleteFile(String path) throws FileSystemException{
		File file = new File(root,path);
		boolean res = file.delete();
		if(!res)
			throw new FileSystemException("Can't delete : "+file.getName());
		burden =  root.getTotalSpace();
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
	    burden =  root.getTotalSpace();
		return true;
	}
	
	public boolean copyDirectory(String source, String destination){
		return true;
	}
	
	public boolean moveFile(String source, String dest) throws IOException{
		boolean tmp = copyFile(source, dest);
		return tmp && deleteFile(source);
	}
	
	public boolean moveDirectory(String source, String dest) {
		return true;
	}
	
	public long getBurden(){
		return burden;
	}
}
