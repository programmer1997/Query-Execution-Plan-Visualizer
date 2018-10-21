
public class Author {
	
	private String name;
	private long authorID;
	public long getAuthorID() {
		return this.authorID;
	}

	public void setAuthorID(long authorID) {
		this.authorID = authorID;
	}

	public Author(){
		name = null;
	}
	
	public Author(String name){
		this.name = name;
	}
	public Author(long authorID,String name){
		this.authorID = authorID;
		this.name = name;
	}

	public String getAuthorName() {
		return this.name;
	}

	public void setAuthorName(String name) {
		this.name = name;
	}
	
}
