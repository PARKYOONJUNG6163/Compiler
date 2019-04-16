public class Variable {
	private String type;
	private String value;
	private String base;
	private String offset;
	private String size;
	
	public Variable(String type,String value,String base, String offset, String size){
		this.type = type;
		this.value = value;
		this.base = base;
		this.offset = offset;
		this.size = size;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	
}
