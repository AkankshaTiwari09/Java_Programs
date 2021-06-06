import java.io.BufferedReader;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Conference {
	ArrayList<Talk> ListOfAllTalks;
	//ArrayList<Integer> time;
	ArrayList<ArrayList<Talk>> PossibleMorningSessions;
	ArrayList<ArrayList<Talk>> PossibleEveningSessions;
	int totalTalks;
	
	
	Conference()
	{
		totalTalks=0;
		ListOfAllTalks=new ArrayList<Talk>();
		//time=new ArrayList<Integer>();
		PossibleMorningSessions= new ArrayList<ArrayList<Talk>>();
		PossibleEveningSessions= new ArrayList<ArrayList<Talk>>();
	}

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s;
        Conference obj= new Conference();
        while((s = br.readLine()) != null)
        {
        	s=s.trim();
        	if(s.equalsIgnoreCase("exit"))
        		break;
        	obj.totalTalks+=1;
        	obj.add_talk(s);
        }
        //for(int i=0;i<obj.totalTalks;i++){
        	//System.out.println(obj.ListOfAllTalks.get(i));
        //}
        int totalPossibleDays=obj.get_session_time(obj.ListOfAllTalks)/(60*6);
        obj.get_all_possible_morning_sessions(totalPossibleDays);
    	obj.get_all_possible_evening_sessions(totalPossibleDays);
    	obj.schedule_talks();
	}
	
	public void add_talk(String s)
	{
		int time=0;
		int split_index=s.lastIndexOf(" ");
		//if(split_index==-1){
			//System.out.println("Invalid Input");
			//return;
		//}
		String time_sub=s.substring(split_index+1);
		String name;
		try{
			int ind=s.lastIndexOf("-");
			name=s.substring(0,ind)+s.substring(split_index+1);
		}
		catch(Exception e){
			name=s;
		}
		
		
		if(time_sub.endsWith("min")){
			int index=time_sub.indexOf("min");
			time=Integer.parseInt(time_sub.substring(0, index));
		}
		else if(time_sub.endsWith("lightning")){
			int index=time_sub.indexOf("lightning");
			String lightning=time_sub.substring(0, index);
			if(lightning.equals("")){
				time=5;
			}
			else
				time=Integer.parseInt(lightning)*5;
		}
		ListOfAllTalks.add(new Talk(name,time));
		
	}
	
	public void get_all_possible_morning_sessions(int totalPossibleDays){
		//for morning session lower and upper limit will be same
		int lowerSessionLimit=180;
		int UpperSessionLimit=lowerSessionLimit;
		PossibleMorningSessions=get_all_possible_combinations(lowerSessionLimit, UpperSessionLimit,totalPossibleDays);
		remove_session_talks(PossibleMorningSessions);
	}
	
	public void get_all_possible_evening_sessions(int totalPossibleDays){
		int lowerSessionLimit=180;
		int UpperSessionLimit=240;
		PossibleEveningSessions=get_all_possible_combinations(lowerSessionLimit, UpperSessionLimit,totalPossibleDays);
		remove_session_talks(PossibleEveningSessions);
		
		//if talks are left then add them in evening sessions to occupy 4Pm to 5PM slot(Before Networking event).
		if(!ListOfAllTalks.isEmpty()){
			ArrayList<Talk> newAddedTalks= new ArrayList<Talk>();
			for(ArrayList<Talk> session: PossibleEveningSessions){
				int sessionTime=get_session_time(session);
				for(Talk talk:ListOfAllTalks){
					int time=talk.get_time_duration();
					if(time + sessionTime<= UpperSessionLimit){
						session.add(talk);
						talk.set_is_added(true);
						//ListOfAllTalks.remove(talk);
						newAddedTalks.add(talk);
					}
				}
				ListOfAllTalks.removeAll(newAddedTalks);
				if(ListOfAllTalks.isEmpty())
					break;
			}
		}
	}
	
	public int get_session_time(ArrayList<Talk> session){
		int sessionTime=0;
		for(Talk talk: session){
			sessionTime+=talk.get_time_duration();			
		}
		return sessionTime;
	}
	
	
	//Calculate all possible combinations possible for a slot
	public ArrayList<ArrayList<Talk>> get_all_possible_combinations(int lowerSessionLimit, int UpperSessionLimit, int totalPossibleDays){
		ArrayList<ArrayList<Talk>> AllPossibleComb= new ArrayList<ArrayList<Talk>>();
		int count=0;
		for(int i=0;i<totalTalks;i++){
			int j=i;
			int t_time=0;
			boolean valid=false;
			ArrayList<Talk> PossibleComb= new ArrayList<Talk>();
			
			while(j!=totalTalks){
				Talk talk= ListOfAllTalks.get(j);
				j++;
				if(talk.check_is_added())
					continue;
				int time=talk.get_time_duration();
				if(time>UpperSessionLimit || time+t_time>UpperSessionLimit)
					continue;
				PossibleComb.add(talk);
				t_time+=time;
				
				if(t_time>=lowerSessionLimit && t_time<= UpperSessionLimit){
					valid=true;
					break;
				}
				
			}
			
			if(valid){
				AllPossibleComb.add(PossibleComb);
				count+=1;
				for(Talk talk: PossibleComb){
					talk.set_is_added(true);
				}
			}
			if (count== totalPossibleDays)
				break;
		}
		return AllPossibleComb;
	}
	
	public void remove_session_talks(ArrayList<ArrayList<Talk>> PossibleMorningSessions){
		for(ArrayList<Talk> session : PossibleMorningSessions){
			ListOfAllTalks.removeAll(session);
			totalTalks=ListOfAllTalks.size();
		}
	}
	
	//Prints the output by scheduling each talk a time.
	public void schedule_talks(){
		Calendar calendar= Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mma ");
		//calendar.set(Calendar.HOUR_OF_DAY,9);
		//calendar.set(Calendar.MINUTE,0);
		//calendar.set(Calendar.SECOND,9);
		
		int totalPossibleDays=PossibleMorningSessions.size();
		for(int track=0;track<totalPossibleDays;track++)
		{
			calendar.set(Calendar.HOUR_OF_DAY,9);
			calendar.set(Calendar.MINUTE,0);
			String scheduledTime= formatter.format(calendar.getTime());
			System.out.println("Track "+(track+1)+":");
			ArrayList<Talk> morningSession=PossibleMorningSessions.get(track);
			for(Talk talk: morningSession){
				talk.set_talk_scheduled_time(scheduledTime);
				System.out.println(scheduledTime+" "+talk.get_name());
				scheduledTime= get_updated_time(calendar,talk.get_time_duration());
			}
			
			System.out.println(scheduledTime+" Lunch");
			scheduledTime=get_updated_time(calendar, 60);
			
			ArrayList<Talk> eveningSession=PossibleEveningSessions.get(track);
			for(Talk talk: eveningSession){
				talk.set_talk_scheduled_time(scheduledTime);
				System.out.println(scheduledTime+" "+talk.get_name());
				scheduledTime= get_updated_time(calendar,talk.get_time_duration());
			}
			if(calendar.get(Calendar.HOUR_OF_DAY)<16){
				System.out.println("04:00PM Networking Event");
			}
			else{
				System.out.println(scheduledTime+" Networking Event");
			}
			System.out.println();
		}
	}
	
	public String get_updated_time(Calendar calendar, int time){
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mma ");
		calendar.add(Calendar.MINUTE, time);
		return formatter.format(calendar.getTime());
	}
}

//Talk class represent each individual talk and holds details of each talk. We can get and set values of each property

class Talk{
	String name;
	int timeDuration;
	boolean isAdded=false;
	String talkScheduledTime;
	
	public Talk(String name, int timeDuration)
	{
		this.name=name;
		this.timeDuration=timeDuration;
	}
	
	public String get_name(){
		return name;
	}
	
	public int get_time_duration(){
		return timeDuration;
	}
	
	public String get_talk_scheduled_time(){
		return talkScheduledTime;
	}
	
	public boolean check_is_added(){
		return isAdded;
	}
	
	public void set_name(String name){
		this.name=name;
	}
	
	public void set_time_duration(int timeDuration){
		this.timeDuration=timeDuration;
	}
	
	public void set_is_added(boolean isAdded){
		this.isAdded=isAdded;
	}
	
	public void set_talk_scheduled_time(String talkScheduledTime){
		this.talkScheduledTime=talkScheduledTime;
	}
}



