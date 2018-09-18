
import java.util.ArrayList;

public class Authored {
	private ArrayList<Author> authorList;
	private Publication publication;
	
	public Authored(){
		super();
	}

	public ArrayList<Author> getAuthorList() {
		return this.authorList;
	}

	public Publication getPublication() {
		return publication;
	}
	
	public String getAuthorListString(){
		String authorString = "";
		for(int i=0;i<this.getAuthorList().size();i++){
			authorString += this.getAuthorList().get(i).getAuthorName();
		}
		return authorString;
	}
	
	public void setAuthorList(ArrayList<Author> authorList) {
		this.authorList = authorList;
	}
	
	public void setPublication(Publication publication) {
		this.publication = publication;
	}
	
	public String toString(){
		return this.getPublication().toString()+this.getAuthorListString();
	}
	
}
