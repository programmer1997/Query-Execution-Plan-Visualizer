public class Phdthesis extends Publication {
	private String school;
	
	public Phdthesis(){
		super();
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	
	public String toString(){
		return this.getPubId()+this.getPubKey()+this.getTitle()+this.getYear()+this.getSchool();
	}
}
