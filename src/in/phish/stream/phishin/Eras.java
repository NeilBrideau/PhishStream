package in.phish.stream.phishin;

import com.google.gson.annotations.SerializedName;

/** Eras response. 
 * Had to do this a little funny.
 * @author corn
 */
public class Eras {
	@SerializedName("1.0") 
	public String[] one;
	@SerializedName("2.0") 
	public String[] two;
	@SerializedName("3.0") 
	public String[] three;
	@SerializedName("4.0") 
	public String[] four;
}