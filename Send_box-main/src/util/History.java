package util;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
//this class for getting and setting the details of history table
public class History {
	  private String username ;
	  private String ID ;
	  private String type ;
	  private Time hour ;
	  private Date date ;
	  private float time ;
	  private int speed;

	    public History(String ID, String username, String type, Date date,Time hour, float time,int speed){
	        this.username=username;
	        this.ID=ID;
	        this.type=type;
	        this.date=date;
	        this.hour=hour;
	        this.time=time;
	        this.speed=speed;
	    }

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getID() {
			return ID;
		}

		public void setID(String iD) {
			ID = iD;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Time gethour() {
			return hour;
		}

		public void sethour(Time hour) {
			this.hour = hour;
		}
		public Date getDate() {
			return date;
		}
		public float getTime() {
			return time;
		}

		public void setTime(float time) {
			this.time = time;
		}

		public int getSpeed() {
			return speed;
		}

		public void setSpeed(int speed) {
			this.speed = speed;
		}
		public String toString() {
			return (ID+","+username+","+type+","+date+","+time+","+speed);
		}

}
