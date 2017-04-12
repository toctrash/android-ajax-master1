package iut.projet_android;

public class Image {
	private String url;
	private String titre;
	private String unscapedUrl;
	
	public Image(String url, String titre, String unscapedUrl){
		this.url = url;
		this.titre = titre;
		this.unscapedUrl = unscapedUrl;
	}

	public String getUrl() {
		return url;
	}

	public String getUnscapedUrl() {
		return unscapedUrl;
	}

	public void setUnscapedUrl(String unscapedUrl) {
		this.unscapedUrl = unscapedUrl;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
}
