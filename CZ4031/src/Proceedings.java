public class Proceedings extends Publication {
	private String isbn;
	private String booktitle;
	private String publisher;

	public Proceedings(){
		super();
	}

	public String getIsbn() {
		return isbn;
	}

	public String getBooktitle() {
		return booktitle;
	}

	public String getPublisher() {
		return publisher;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public void setBooktitle(String booktitle) {
		this.booktitle = booktitle;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public String toString(){
		return this.getPubId()+this.getPubKey()+this.getTitle()+this.getYear()+this.getIsbn()+this.getBooktitle()+this.getPublisher();
	}
}
