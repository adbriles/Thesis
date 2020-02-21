package thesis.dfs.sharedClasses;

//My own pair just for strings since the lab computers won't import apache commons.
public class PairStrings {
	String key;
	String value;
	
	public PairStrings(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public PairStrings() {
		key = "";
		value = "";
	}

	public String getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	
	public String getKey() {
		return key;
	}
	
}
