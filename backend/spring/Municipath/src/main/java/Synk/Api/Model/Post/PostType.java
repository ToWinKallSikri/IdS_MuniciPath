package Synk.Api.Model.Post;


public enum PostType {
	SOCIAL,
	INSTITUTIONAL,
	HEALTHandWELLNESS,
	TOURISTIC,
	EVENT,
	CONTEST;
	
	public static PostType safeValueOf(String data) {
		try {
			return valueOf(data.toUpperCase());
		} catch(Exception e) {
			return PostType.TOURISTIC;
		}
	}
}
