import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ZFileFilter extends FileFilter {
	private String extension = ".sdz", description = "Fichier Ardoise Mazique";
	public ZFileFilter(String ext, String descrip){
		this.extension = ext;
		this.description = descrip;
		}
	@Override
	public boolean accept(File f) {
		// TODO Auto-generated method stub
		return (f.isDirectory() ||f.getName().endsWith(this.extension));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return this.extension + " - " + this.description;
	}

}
