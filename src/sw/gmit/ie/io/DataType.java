package sw.gmit.ie.io;

public class DataType {

	public Parser parserType(String dataType, String file) throws Exception {
		if (dataType == null) {
			return null;
		} 
		else if (dataType.equalsIgnoreCase("file")) {
			return new FileParser(file);
		}
		else if(dataType.equalsIgnoreCase("url")){
			return new URLParser(file);
		}
		return null;
	}
}
