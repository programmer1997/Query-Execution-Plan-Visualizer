public class Publication {
	private long pubId;
	private String pubKey;
	private String title;
	private String year;
	
	public Publication(){}

	public long getPubId() {
		return this.pubId;
	}

	public String getPubKey() {
		return this.pubKey;
	}

	public String getTitle() {
		return this.title;
	}

	public String getYear() {
		return year;
	}
	
	public void setPubId(long pubId) {
		this.pubId = pubId;
	}
	
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String toString(){
		return this.pubId+ this.pubKey+this.title+this.year;
	}

}

