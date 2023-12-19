package Synk.Api.Model;

public class Group {
	

	private String id;
	private String title;
    private boolean sorted;
    
    public Group() {}
    
    public Group(String id, String title, boolean sorted) {
		super();
		this.id = id;
		this.title = title;
		this.sorted = sorted;
	}
	
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isSorted() {
		return sorted;
	}
	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}
}
