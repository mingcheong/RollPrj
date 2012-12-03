package gov.nbcs.rp.common;

import java.io.File;
import java.io.FilenameFilter;

public class FileExtnameFilter implements FilenameFilter
{
    String ext;
    public FileExtnameFilter(String ext)
    {
        this.ext=ext;        
    }        

	public boolean accept(File dir, String name) { 
		return name.endsWith(ext); 
	}
}