public class Inproceedings extends Publication {
	private String booktitle;
	private String pages;
	private String crossref;
	
	public Inproceedings(){
		super();
	}

	public String getBooktitle() {
		return booktitle;
	}

	public String getPages() {
		return pages;
	}
	
	public String getCrossRef() {
		return crossref;
	}
	
	public void setBooktitle(String booktitle) {
		this.booktitle = booktitle;
	}
	
	public void setPages(String pages) {
		this.pages = pages;
	}

	public void setCrossRef(String crossref) {
		this.crossref = crossref;
	}
	
	public String toString(){
		return this.getPubId()+this.getPubKey()+this.getTitle()+this.getYear()+this.getBooktitle()+this.getPages()+this.getCrossRef();
	}
	
}
