package util;
//This class is for a specific user who can access the system
public class User {
	  public static User currentUser;
	  private String username ;
	  private String password ;
	  private String firstname ;
	  private String lastname ;
	  private String email ;
	  private int numofgenerate;
	    public User(String username, String password, String firstname, String lastname, String email,int numofgenerate){
	        this.username=username;
	        this.password=password;
	        this.firstname=firstname;
	        this.lastname=lastname;
	        this.email=email;
	        this.numofgenerate=numofgenerate;
	    }
	    public int getnumofgenerate() {
	    	return numofgenerate;
	    }

	    public void setnumofgenerate(int numofgenerate) {
	    	 this.numofgenerate=numofgenerate;
	    }
	    
	    public String getUsername() {
	        return username;
	    }
	    public String getPassword() {
	        return password;
	    }

	    public String getFirstName() {
	        return firstname;
	    }

	    public String getLastName() {
	        return lastname;
	    }

	    public String getEmail() {
	        return email;
	    }

}
