package personel.photo;

import java.io.Serializable;

public class Image implements Serializable {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
