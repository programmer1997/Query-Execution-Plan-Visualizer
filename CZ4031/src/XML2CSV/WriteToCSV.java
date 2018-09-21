import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class WriteToCSV {
	private ArrayList<Authored> authoredList;
	private long authorId;
	private long pubId;
	private File file;
	private FileWriter fileWriter;
	private ArrayList<Author> authorList = new ArrayList<Author>();
	private Hashtable[] authorTable = new Hashtable[53];
	private final String authorPath = "Author";
	private final String articlePath = "Article";
	private final String bookPath = "Book";
	private final String incollectionPath = "Incollection";
	private final String inproceedingsPath = "Inproceedings";
	private final String proceedingsPath = "Proceedings";
	private final String publicationPath = "Publication";
	private final String authoredPath = "Authored";
	private final String phdthesisPath = "Phdthesis";
	
	public WriteToCSV(ArrayList<Authored> authoredList){
		this.authoredList = authoredList;
		setPubId();
	}
	
	public long getAuthorId(){
		return ++authorId;
	}
	public long getPubId(){
		return ++pubId;
	}
	
	public void getFile(String path){
		path = "CSV/"+path;
		path += ".csv";
		file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Unable to create file :"+path);
			}
		}
		try {
			fileWriter = new FileWriter(file,true);
		} catch (IOException e) {
			System.out.println("FileWriter error :"+path);
		}
	}
	public void setPubId(){
		int i;
		for(i=0;i<this.authoredList.size();i++){
			authoredList.get(i).getPublication().setPubId(getPubId());
		}
		System.out.println("Set Pub ID done:"+i);
	}
	
	private void clean(String message){
		try {
			fileWriter.flush();
			fileWriter.close();
			System.gc();
			System.out.println(message);
		} catch (IOException e1) {}
	}
	
	private String hasComma(String string){
		if(string == null){
			return "";
		}
		else{
		if (string.contains(",")){
			string = string.replaceAll(",", "~");
		}
		if (string.contains("\"")){
			string = string.replaceAll("\"", "-");
		}
		return string;
		}
	}
	
	public void writeToPublication(){
		Publication publication;
		getFile(publicationPath);
		for(int i=0;i<this.authoredList.size();i++){
			publication = this.authoredList.get(i).getPublication();
				try {
					fileWriter.append(publication.getPubId()+",");
					fileWriter.append(hasComma(publication.getPubKey())+",");
					fileWriter.append(hasComma(publication.getTitle())+",");
					fileWriter.append(hasComma(publication.getYear()));
					fileWriter.append("\r\n");
				} catch (IOException e) {
					System.out.println("Error at Publication:"+e+","+publication.getPubId());
					System.exit(0);
				}
		}
		clean("Publication done");
	}
	
	public void writeToBook(){
		Publication publication;
		getFile(bookPath);
		for(int i=0;i<this.authoredList.size();i++){
			publication = this.authoredList.get(i).getPublication();
			if(publication instanceof Book){
				try {
					fileWriter.append(publication.getPubId()+",");
					fileWriter.append(hasComma(((Book) publication).getBooktitle())+",");
					fileWriter.append(hasComma(((Book) publication).getSeries())+",");
					fileWriter.append(hasComma(((Book) publication).getPublisher()));
					fileWriter.append("\r\n");
				} catch (IOException e) {
					System.out.println("Error at Book:"+e+","+publication.getPubId());
					System.exit(0);
				}
		}
		}
		clean("Book done");
	}
	public void writeToArticle(){
		Publication publication;
		getFile(articlePath);
		for(int i=0;i<this.authoredList.size();i++){
			publication = this.authoredList.get(i).getPublication();
			if(publication instanceof Article){
				try {
					fileWriter.append(publication.getPubId()+",");
					fileWriter.append(hasComma(((Article) publication).getJournal())+",");
					fileWriter.append(hasComma(((Article) publication).getPages())+",");
					fileWriter.append(hasComma(((Article) publication).getVolume()));
					
					fileWriter.append("\r\n");
				} catch (IOException e) {
					System.out.println("Error at Article:"+e+","+publication.getPubId());
					System.exit(0);
				}
		}
		}
		clean("Article done");
	}

	public void writeToProceedings(){
		Publication publication;
		getFile(proceedingsPath);
		for (int i=0;i<this.authoredList.size();i++){
			publication = this.authoredList.get(i).getPublication();
			if (publication instanceof Proceedings){
				try{
					fileWriter.append(publication.getPubId()+",");
					fileWriter.append(hasComma(((Proceedings) publication).getIsbn()) + ",");
					fileWriter.append(hasComma(((Proceedings) publication).getBooktitle()) + ",");
					fileWriter.append(hasComma(((Proceedings) publication).getPublisher()));
					fileWriter.append("\r\n");
				} catch (IOException e){
					System.out.println("Error at Proceedings: " + e +","+publication.getPubId());
					System.exit(0);
				}
			}
		}
		clean("Proceedings done");
	}

	public void writeToPhdthesis(){
		Publication publication;
		getFile(phdthesisPath);
		for (int i=0;i<this.authoredList.size();i++){
			publication = this.authoredList.get(i).getPublication();
			if (publication instanceof Phdthesis){
				try{
					fileWriter.append(publication.getPubId()+",");
					fileWriter.append(hasComma(((Phdthesis) publication).getSchool()));
					fileWriter.append("\r\n");
				} catch (IOException e){
					System.out.println("Error at PHD Thesis:" + e +","+publication.getPubId());
					System.exit(0);
				}
			}
		}
		clean("PHD Thesis done");
	}
	
	public void writeToIncollection(){
		Publication publication;
		getFile(incollectionPath);
		for(int i=0;i<this.authoredList.size();i++){
			publication = this.authoredList.get(i).getPublication();
			if(publication instanceof Incollection){
				try {
					fileWriter.append(publication.getPubId()+",");
					fileWriter.append(hasComma(((Incollection) publication).getBookTitle())+",");
					fileWriter.append(hasComma(((Incollection) publication).getPages())+",");
					fileWriter.append(hasComma(((Incollection) publication).getcrossRef()));
					fileWriter.append("\r\n");
				} catch (IOException e) {
					System.out.println("Error at Incollection:"+e+","+publication.getPubId());
					System.exit(0);
				}
		}
		}
		clean("Incollection done");
	}
	
	public void writeToInproceedings(){
		Publication publication;
		getFile(inproceedingsPath);
		for(int i=0;i<this.authoredList.size();i++){
			publication = this.authoredList.get(i).getPublication();
			if(publication instanceof Inproceedings){
				try {
					fileWriter.append(publication.getPubId()+",");
					fileWriter.append(hasComma(((Inproceedings) publication).getBooktitle())+",");
					fileWriter.append(hasComma(((Inproceedings) publication).getPages())+",");
					fileWriter.append(hasComma(((Inproceedings) publication).getCrossRef()));
					fileWriter.append("\r\n");
				} catch (IOException e) {
					System.out.println("Error at Inproceedings:"+e+","+publication.getPubId());
					System.exit(0);
				}
		}
		}
		clean("Inproceedings done");
	}
	public void writeToAuthor(){
		
		ArrayList<Author> authorList;
		HashSet<String> set;
		ArrayList<String> authorStringArray = new ArrayList<String>();
		Author author;
		
		for(int i=0;i<this.authoredList.size();i++){
			authorList = this.authoredList.get(i).getAuthorList();
			for(int j=0;j<authorList.size();j++){
				author = authorList.get(j);
				authorStringArray.add(author.getAuthorName());
			}
		}
		set = new HashSet<String>(authorStringArray);
		authorStringArray = new ArrayList<String>(set);

		for(int i=0;i<authorStringArray.size();i++){
			author = new Author(i,authorStringArray.get(i));
			this.authorList.add(author);
		}

		
		getFile(authorPath);
		for(int i=0;i<this.authorList.size();i++){
			author = this.authorList.get(i);
			try {
				fileWriter.append(author.getAuthorID()+",");
				fileWriter.append(hasComma(author.getAuthorName()));
				fileWriter.append("\r\n");
			} catch (IOException e) {
				System.out.println("Error at Author:"+e+","+author.getAuthorID());
				System.exit(0);
			}
		}
		clean("Author done");
	}
	
	public void writeToAuthored(){
		Author author;
		Authored authored;
		Publication publication;
		HashSet set;
		ArrayList<Author> authorList;
		ArrayList<String> authoredString = new ArrayList<String>();
		long authorID;

		for(int i=0;i<authorTable.length;i++){
			authorTable[i] = new Hashtable();
		}
		for(int i=0;i<this.authorList.size();i++){
			author = this.authorList.get(i);
			splitHash(author);
		}
		getFile(authoredPath);
		for(int i=0;i<authoredList.size();i++){
			authored = authoredList.get(i);
			publication = authored.getPublication();
			authorList = authored.getAuthorList();
			for(int j=0;j<authorList.size();j++){
				author = authorList.get(j);
				authorID = getHash(author);
				if(authorID > 0){
					authoredString.add(authorID+","+publication.getPubId());
				}
			}
		}
		set = new HashSet<String>(authoredString);
		authoredString = new ArrayList<String>(set);
		set = new HashSet<String>(authoredString);
		authoredString = new ArrayList<String>(set);
		for(int i=0;i<authoredString.size();i++){
			try{
				fileWriter.append(authoredString.get(i));
				fileWriter.append("\r\n");
			}
			catch(Exception e){
				System.out.println("Error at Authored:"+e);
			}
		}
		clean("Authored done");
	}
	
		public int split(String authorName){
			char a = authorName.charAt(0);
			int b = (int)a-64;
			if(b<0 || b>58){
				return 0;
			}
			else if(b>26 && b<33){
				return 0;
			}
			else if(b>=33){
				return b-6;
			}
			else{
				return b;
			}
			
		}
		
		public void splitHash(Author author){
			int value = split(author.getAuthorName());
			authorTable[value].put(author.getAuthorName(), author.getAuthorID());
		}
		
		public long getHash(Author author){
			long authorID=0;
			int value = split(author.getAuthorName());
			try{
				authorID = ((long)authorTable[value].get(author.getAuthorName()));
			}catch(Exception e){
				return 0;
			}
			return authorID;
		}
}

