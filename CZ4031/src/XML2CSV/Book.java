public class Book extends Publication {
	private String booktitle;
	private String series;
	private String publisher;
	
	public Book(){
		super();
	}
	
	public String getBooktitle() {
		return this.booktitle;
	}
	
	public String getSeries() {
		return this.series;
	}
	
	public String getPublisher() {
		return this.publisher;
	}
	
	public void setBooktitle(String booktitle) {
		this.booktitle = booktitle;
	}
	
	public void setSeries(String series) {
		this.series = series;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public String toString(){
		return this.getPubId()+this.getPubKey()+this.getTitle()+this.getYear()+this.getBooktitle()+this.getSeries()+this.getPublisher();
	}
	
}
