package in.phish.stream.phishin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Show implements Comparable<Show> {
    public int id;
    public String date;
    public int duration;
    public boolean incomplete;
    public boolean missing;
    public boolean sbd;
    public boolean remastered;
    public int tour_id;
    public int venue_id;
    public int likes_count;
    public String venue_name;
    public String location;
    
	@Override
	public int compareTo(Show show) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);		
		try {
			Date them = fmt.parse(show.date);
			Date me   = fmt.parse(date);
			return me.compareTo(them);
			
		} catch (ParseException e) {}
		return 0;
	}
}
