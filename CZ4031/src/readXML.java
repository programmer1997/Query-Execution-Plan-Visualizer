import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class readXML extends DefaultHandler{
	private ArrayList<Author> AuthorList = new ArrayList<Author>();
	private Publication publication;
	private Author author;
	private Authored authored;
	private ArrayList<Authored> authoredList;
	private int pubId,authorID;
	private ArrayList<Author> authorList;
	private WriteToCSV writeToCSV;
	private String tempStr; 
	
public ArrayList<Authored> getXml(){
  try {
	  authoredList = new ArrayList<Authored>();
	  pubId = 0;
	  authorID = 0;
	  
	  // obtain and configure a SAX based parser
	  SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

	  // obtain object for SAX parser
	  SAXParser saxParser = saxParserFactory.newSAXParser();

	  // default handler for SAX handler class
	  // all three methods are written in handler's body
	  DefaultHandler defaultHandler = new DefaultHandler(){
    
	  boolean articleTag = false;
	  boolean authorTag = false;
	  boolean bookTag = false;
	  boolean inproceedingsTag = false;	  
	  boolean incollectionTag = false;  
      boolean proceedingsTag = false;
      boolean phdthesisTag = false;

	  String elementTag = "";
	  
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	tempStr = "";
    	
    	if(qName.compareToIgnoreCase("book") == 0){
    		bookTag = true;
    		authored = new Authored();
    		authorList = new ArrayList<Author>();
    		publication = new Book();
    		publication.setPubId(generatePublicationId());
    		publication.setPubKey(attributes.getValue("key"));
    	}
    	
    	else if(qName.compareToIgnoreCase("article")==0){
    		articleTag = true;
    		authored = new Authored();
    		authorList = new ArrayList<Author>();
    		publication = new Article();
    		publication.setPubId(generatePublicationId());
    		publication.setPubKey(attributes.getValue("key"));
    	}
    	
    	else if(qName.compareToIgnoreCase("inproceedings")==0){
    		inproceedingsTag = true;
    		authored = new Authored();
    		authorList = new ArrayList<Author>();
    		publication = new Inproceedings();
    		publication.setPubId(generatePublicationId());
    		publication.setPubKey(attributes.getValue("key"));
    	}
    	
		else if(qName.compareToIgnoreCase("proceedings")==0){
    		proceedingsTag = true;
    		authored = new Authored();
    		authorList = new ArrayList<Author>();
    		publication = new Proceedings();
    		publication.setPubId(generatePublicationId());
    		publication.setPubKey(attributes.getValue("key"));
    	}
    	
    	else if(qName.compareToIgnoreCase("incollection")==0){
    		authored = new Authored();
    		authorList = new ArrayList<Author>();
    		publication = new Incollection();
    		publication.setPubId(generatePublicationId());
    		publication.setPubKey(attributes.getValue("key"));
    		incollectionTag = true;
    	}
		
		else if(qName.compareToIgnoreCase("phdthesis")==0){
    		authored = new Authored();
    		authorList = new ArrayList<Author>();
    		publication = new Phdthesis();
    		publication.setPubId(generatePublicationId());
    		publication.setPubKey(attributes.getValue("key"));
    		phdthesisTag = true;
    	}
    	elementTag = qName;
    }

    public int generatePublicationId(){
    	return ++pubId;
    }
    public int generateAuthorId(){
    	return ++authorID;
    }

    public void characters(char ch[], int start, int length) throws SAXException {
    	tempStr += new String(ch, start, length);
    	if(bookTag){
    		if(elementTag.compareToIgnoreCase("title")==0){
    			publication.setTitle(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("year")==0){
    			publication.setYear(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("author")==0){
    			authorTag = true;
    		}
    		else if(elementTag.compareToIgnoreCase("publisher")==0){
    			((Book)publication).setPublisher(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("series")==0){
    			((Book)publication).setSeries(tempStr);
    		}
			else if(elementTag.compareToIgnoreCase("booktitle")==0){
    			((Book)publication).setBooktitle(tempStr);
    		}
    	}
    	else if(articleTag){
    		if(elementTag.compareToIgnoreCase("title")==0){
    			publication.setTitle(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("year")==0){
    			publication.setYear(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("author")==0){
    			authorTag = true;
    		}
    		else if(elementTag.compareToIgnoreCase("journal")==0){
    			((Article)publication).setJournal(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("pages")==0){
    			((Article)publication).setPages(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("volume")==0){
    			((Article)publication).setVolume(tempStr);
    		}    		
    	}
    	else if(inproceedingsTag){
    		if(elementTag.compareToIgnoreCase("title")==0){
    			publication.setTitle(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("year")==0){
    			publication.setYear(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("author")==0){
    			authorTag = true;
    		}
    		else if(elementTag.compareToIgnoreCase("booktitle")==0){
    			((Inproceedings)publication).setBooktitle(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("pages")==0){
    			((Inproceedings)publication).setPages(tempStr);
    		}
			else if(elementTag.compareToIgnoreCase("crossref")==0){
    			((Inproceedings)publication).setCrossRef(tempStr);
    		}
    	}
    	else if(incollectionTag){
    		if(elementTag.compareToIgnoreCase("title")==0){
    			publication.setTitle(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("year")==0){
    			publication.setYear(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("author")==0){
    			authorTag = true;
    		}
    		else if(elementTag.compareToIgnoreCase("booktitle")==0){
    			((Incollection)publication).setBooktitle(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("pages")==0){
    			((Incollection)publication).setPages(tempStr);
    		}
    		else if(elementTag.compareToIgnoreCase("crossref")==0){
    			((Incollection)publication).setcrossRef(tempStr);
    		}
    	}
        else if(proceedingsTag){
            if(elementTag.compareToIgnoreCase("title")==0){
                publication.setTitle(tempStr);
            }
            else if(elementTag.compareToIgnoreCase("year") ==0){
                publication.setYear(tempStr);
            }
            else if(elementTag.compareToIgnoreCase("author")==0){
                authorTag = true;
            }
            else if(elementTag.compareToIgnoreCase("booktitle")==0){
                ((Proceedings)publication).setBooktitle(tempStr);
            }
            else if(elementTag.compareToIgnoreCase("isbn")==0){
                ((Proceedings)publication).setIsbn(tempStr);
            }
            else if(elementTag.compareToIgnoreCase("publisher")==0){
                ((Proceedings)publication).setPublisher(tempStr);
            }
        }
        else if (phdthesisTag){
            if(elementTag.compareToIgnoreCase("title")==0){
                publication.setTitle(tempStr);
            }
            else if(elementTag.compareToIgnoreCase("year") ==0){
                publication.setYear(tempStr);
            }
            else if(elementTag.compareToIgnoreCase("author")==0){
            	authorTag = true;
            }
            else if(elementTag.compareToIgnoreCase("school")==0){
                ((Phdthesis)publication).setSchool(tempStr);
            }
        }
    }

    public void endElement(String uri, String localName, String qName)
      throws SAXException {
    	if(qName.compareToIgnoreCase("book") == 0){
    		bookTag = false;
    		authored.setAuthorList(authorList);
    		authored.setPublication(publication);
    		authoredList.add(authored);
    	}
    	else if(qName.compareToIgnoreCase("article")==0){
    		articleTag = false;
    		authored.setAuthorList(authorList);
    		authored.setPublication(publication);
    		authoredList.add(authored);
    	}
    	else if(qName.compareToIgnoreCase("inproceedings")==0){
    		inproceedingsTag = false;
    		authored.setAuthorList(authorList);
    		authored.setPublication(publication);
    		authoredList.add(authored);
    	}
		else if(qName.compareToIgnoreCase("proceedings")==0){
    		proceedingsTag = false;
    		authored.setAuthorList(authorList);
    		authored.setPublication(publication);
    		authoredList.add(authored);
    	}
    	else if(qName.compareToIgnoreCase("incollection")==0){
    		incollectionTag = false;
    		authored.setAuthorList(authorList);
    		authored.setPublication(publication);
    		authoredList.add(authored);
    	}
        else if(qName.compareToIgnoreCase("phdthesis")==0){
            phdthesisTag = false;
            authored.setAuthorList(authorList);
            authored.setPublication(publication);
            authoredList.add(authored);
        }
    	if (authorTag==true){
    		authorTag = false;
    		author = new Author(tempStr);
            authorList.add(author);
    	}

    }
    
    public void endDocument() throws SAXException{
    	System.out.println("AuthoredList size: "+authoredList.size());
    	writeToCSV = new WriteToCSV(authoredList);
    	writeToCSV.writeToPublication();
    	writeToCSV.writeToArticle();
    	writeToCSV.writeToBook();
    	writeToCSV.writeToIncollection();
    	writeToCSV.writeToInproceedings();
        writeToCSV.writeToProceedings();
        writeToCSV.writeToPhdthesis();
    	writeToCSV.writeToAuthor();
    	writeToCSV.writeToAuthored();
    }
};
   saxParser.parse("XML/dblp.xml", defaultHandler);
  } catch (Exception e) {
   e.printStackTrace();
  }
  return authoredList;
 }
}
