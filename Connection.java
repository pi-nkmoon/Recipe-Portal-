//PASSWORD to enter admin: pass@0987
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
public class Connection {
	public static void main (String[] args) {
		Scanner sc= new Scanner(System.in);
		int a = 0;
		while(a!=4) {
			System.out.println("Select 1. Admin");
			System.out.println("Select 2. Chef");
			System.out.println("Select 3. Viewer");
			System.out.println("Select 4. Exit");
			a = sc.nextInt();
			if(a==1) {Admin();}
			if(a == 2) {chef();}
			else if(a == 3) {Viewer();}
			else if(a == 4) {System.out.println("Thank you for visiting our Cooking Recipe Portal.");}
		}
		//chefDAO dao = new chefDAO();
		//chef c1 = dao.getChef(4);
		//System.out.println(c1.chefName);
	}
	
	public static void Admin() {
		@SuppressWarnings("resource")
		Scanner sc= new Scanner(System.in);
		String pass = "pass@0987";
		System.out.println("Enter the password (Wrong password will send you back to main menu): ");
		String password = sc.nextLine();
		int a = 0;
		int b=0;
		chefDAO dao = new chefDAO();
		while(true) {
			if(!pass.equals(password)) {return;}
			System.out.println("Select 1. Add Chef");
			System.out.println("Select 2. Fire chef");
			System.out.println("Select 3. To return to main menu");
			a = sc.nextInt();
			if(a == 1) {dao.AddChef();}
			else if (a == 2) {dao.RemoveChef();}
			else if(a == 3) {return;}
		}
	}
	
public static void Viewer() {
		@SuppressWarnings("resource")
		Scanner sc= new Scanner(System.in);
		int a = 0;
		int b=0;
		while(true) {
			System.out.println("Select 1. View all recipes");
			System.out.println("Select 2. Choose a recipe and view its ingredients");
			System.out.println("Select 3. Choose a chef and view his recipes");
			System.out.println("Select 4. View all above average chef");
			System.out.println("Select 5. To return to main menu");
			a = sc.nextInt();
			if (a==1) {
				recipeDAO dao = new recipeDAO();
				dao.allRecipes();
			}
			else if(a == 2) {
				recipeDAO dao = new recipeDAO();
				dao.allRecipes();
				System.out.println("Enter the recipeID whose ingredients you want to see : ");
				b = sc.nextInt();
				recipe r1 = dao.getRecipe(b);
				System.out.println(r1.recipeName + " " + r1.ingredients);
			}
			else if (a==3) {
				chefDAO da = new chefDAO();
				da.allchef();
				System.out.println("Enter the chefID whose recipes you want : ");
				b=sc.nextInt();
				recipeDAO dao = new recipeDAO();
				dao.chefRecipes(b);
			}
			else if (a==4) {
				chefDAO da = new chefDAO();
				da.aboveAvgChef();
			}
			else if(a == 5) {return;}
		}
	}
public static void chef() {
	Scanner sc= new Scanner(System.in);
	int a = 0;
	int b=0;
	System.out.println("Type your chefID :");
	a = sc.nextInt();
	chefDAO dao = new chefDAO();
	chef c1 = dao.getChef(a);
	while(true) {
		System.out.println("Select 1. ADD a Recipe");
		System.out.println("Select 2. Remove a Recipe");
		System.out.println("Select 3. Update Ingredients of a Recipe");
		System.out.println("Select 4. Return to main Menu");
		System.out.println();
		a = sc.nextInt();
		if(a==1) {dao.AddRecipe(c1);}
		else if( a==2 ) {dao.DeleteRecipe(c1);}
		else if(a==3) {dao.UpdateIngredients(c1);}
		else if(a==4) {return;}
	}
}
}
class chefDAO{
	public void RemoveChef() {
		try {
			Scanner sc= new Scanner(System.in);
			String query = "select chefID , chefName from chef";
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()) {
				System.out.println(rs.getString(1) + ". " + rs.getString(2));
			}
			System.out.println();
			
			System.out.println("Enter the chefID you want to remove : ");
			int a = sc.nextInt();
			String query1 = "delete from chef where chefID = " + a;
			String query2 = "drop trigger Rater" + a;
			st.executeUpdate(query1);
			st.executeUpdate(query2);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	public void AddChef() {
		try {
		Scanner sc= new Scanner(System.in);
		System.out.println("Enter the Name of the chef : ");
		String name = sc.nextLine();
		Class.forName("com.mysql.jdbc.Driver");
		java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
		String query1 = "select count(chefID) from chef";
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query1);
		rs.next();
		int x = rs.getInt(1);
		x++;
		String query2 = "insert into chef values (" + x + ",'"+name+"',0)";
		st.executeUpdate(query2);
		String query3 ="create trigger Rater"+ x 
				+ "\r\nafter \r\n"
				+ "update\r\n"
				+ "on recipe\r\n"
				+ "for each row \r\n"
				+ "begin\r\n"
				+ "set @likes:=(select sum(likes) from (select * from chef natural join recipe) as newtable where chefID=" + x +" group by chefName);\r\n"
				+ "set @dislikes:= (select sum(dislikes) from (select * from chef natural join recipe) as newtable where chefID="+ x +" group by chefName);\r\n"
				+ "update chef\r\n"
				+ "SET chefRating = @likes*5/(@likes + @dislikes)\r\n"
				+ "where chefID ="+ x +";\r\n"
				+ "end;\r\n";
		st.execute(query3);
		
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	public chef getChef(int chefID) {
		chef c = new chef();
		c.chefID = chefID;
		try {
			String query = "select chefName from chef where chefID = " + chefID;
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			rs.next();
			String name = rs.getString(1);
			c.chefName = name;
			return c;
			} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public void allchef() {
		try {
			String query = "select chefID , chefName from chef";
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()) {
				System.out.println(rs.getString(1) + ". " + rs.getString(2));
			}
			System.out.println();
			} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void aboveAvgChef() {
		try {
			String query = "select chefID , chefName , chefRating from chef where chefRating >= 2.5";
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()) {
				System.out.println(rs.getString(1) + ". " + rs.getString(2) + " Rating-> " + rs.getDouble(3));
			}
			System.out.println();
			} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void AddRecipe(chef c) {
		try {
			Scanner sc= new Scanner(System.in);
			int a=0;
			System.out.println("Enter the name of the recipe : ");
			String recipeName = sc.nextLine();
			
			String query1 = "";
			String query2 = "select count(recipeID) from recipe";
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query2);
			
			rs.next();
			int x = rs.getInt(1);
			x++;
			String query = "insert into recipe values ("+ x + "," + c.chefID + ",'" + recipeName + "',0,0)";
			st.executeUpdate(query);
			while(true) {
				String ingredient = "";
				System.out.println("Enter 1 to add ingredient and 2 when done : ");
				a=sc.nextInt();
				sc.nextLine();
				if(a==1) {
					System.out.println("Enter the name of the ingredient : ");
					ingredient = sc.nextLine();
					query1 = "insert into linker values (" + c.chefID +","+ x +",'"+ ingredient +"')";
					st.executeUpdate(query1);
				}
				else if(a==2) {break;}
			}
			System.out.println();
			} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void DeleteRecipe(chef c) {
		try {
			Scanner sc= new Scanner(System.in);
			
			while(true) {
			int a=0;
			int i = 0;
			int k=0;
			String query1 = "select recipeID , recipeName from recipe natural join chef where chefID = " + c.chefID;
			String query2 = "select count(recipeID) from recipe";
			
			int[] arr = new int[100];
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query1);
			
			String recipe = "";
			System.out.println("Choose a recipe to delete, from the list shown below(Entering any other no. will return you to the chef menu)");
			while(rs.next()) {
				System.out.println(rs.getInt(1) + ". " + rs.getString(2));
				arr[i] = rs.getInt(1);
				i++;
			}
			//System.out.println(arr[i-1]);
			ResultSet ra = st.executeQuery(query2);
			ra.next();
			int x = ra.getInt(1);
			//System.out.println(x);
			a = sc.nextInt();
			String query3 = "delete from recipe where recipeID = " + a;
			while(true) {
				if(k<i && a == arr[k]) {
			      st.executeUpdate(query3);
			      break;
			    }
				else if(k==i || a<arr[0] || a>arr[i-1]){break;}
				else {k++;}
			}
			if(a == arr[k]) {
				String query4 = "update recipe set recipeID = recipeID-1 where recipeID > " + a;
				st.executeUpdate(query4);
			      break;
			    }
			else {break;}
			}
			
			System.out.println();
			} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void UpdateIngredients(chef c) {
		Scanner sc= new Scanner(System.in);
		try {
		System.out.println("Choose the recipe by entering its recipeID : ");
		String query1 = "select recipeID , recipeName from recipe natural join chef where chefID = " + c.chefID;
		Class.forName("com.mysql.jdbc.Driver");
		java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query1);
		while(rs.next()) {
			System.out.println(rs.getInt(1) + ". " + rs.getString(2));
		}
		int a =sc.nextInt();
		
		recipeDAO r = new recipeDAO();
		int recipeID = r.getARecipe(a);
		System.out.println("Press 1 to Add ingredient OR Press 2 to remove ingredient OR Press any other number to return :");
		a =sc.nextInt();
		if(a == 1) {
				sc.nextLine();
				System.out.println("Enter the name of the ingredient : ");
				String ingredient = sc.nextLine();
				
				
				String query = "insert into linker values ("+ c.chefID + "," + recipeID + ",'" + ingredient + "')";
				st.executeUpdate(query);
				System.out.println();
				
		}
		else if(a == 2) {
				sc.nextLine();
				System.out.println("Enter the name of the ingredient you want to delete : ");
				String ingredient = sc.nextLine();
				
				String query = "delete from linker where ingredient = " + "'" +ingredient +"'";
				st.executeUpdate(query);
				System.out.println();
		}
		else {return;}
	}
	catch (Exception e) {
		System.out.println(e);
	}
}}
class recipeDAO{
	public recipe getRecipe(int recipeID) {
		recipe r = new recipe();
		r.recipeID = recipeID;
		try {
			String query = "select distinct recipeName , ingredient , likes , dislikes from recipe natural join linker where recipeID = " + recipeID;
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			String name = "";
			String ingredient = "";
			
			while(rs.next()) {
				name = rs.getString(1) + " ->";
				ingredient += rs.getString(2) + ",";
			}
			StringBuffer sb = new StringBuffer(ingredient);
			sb.delete(ingredient.length() - 1, ingredient.length());
			r.recipeName = name;
			r.ingredients = sb.toString();
			System.out.println(r.recipeName + " " + r.ingredients);
			System.out.println();
			Scanner sc= new Scanner(System.in);
			System.out.println("Type 1 : To like recipe");
			System.out.println("Type 2 : To dislike recipe");
			ResultSet ra = st.executeQuery(query);
			ra.next();
			int x = ra.getInt(3);
		    int y = ra.getInt(4);
		    r.likes = x;
		    r.dislikes = y;
		    //System.out.println(x);
			int a = sc.nextInt();
			String query1 = "";
			if(a==1) {r.likes++;
			query1 = "update recipe set likes = likes +1 where recipeID = " + recipeID;}
			else if(a==2) {r.dislikes++;
			query1 = "update recipe set dislikes = dislikes +1 where recipeID = " + recipeID;}
			st.executeUpdate(query1);
			
			return r;
			} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public int getARecipe(int recipeID) {
		recipe r = new recipe();
		r.recipeID = recipeID;
		try {
			String query = "select distinct recipeName , ingredient , likes , dislikes from recipe natural join linker where recipeID = " + recipeID;
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			String name = "";
			String ingredient = "";
			
			while(rs.next()) {
				name = rs.getString(1) + " ->";
				ingredient += rs.getString(2) + ",";
			}
			StringBuffer sb = new StringBuffer(ingredient);
			sb.delete(ingredient.length() - 1, ingredient.length());
			r.recipeName = name;
			r.ingredients = sb.toString();
			System.out.println(r.recipeName + " " + r.ingredients);
			System.out.println();
			
			return r.recipeID;
			} catch (Exception e) {
			System.out.println(e);
		}
		return -1;
	}
	
	public void allRecipes() {
		try {
			String query = "select recipeID , recipeName , likes , dislikes from recipe";
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()) {
				System.out.println(rs.getString(1) + ". "+ rs.getString(2) + "\n\tlikes(" + rs.getInt(3) + ") dislikes (" + rs.getInt(4)+")");
			}
			System.out.println();
			} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void chefRecipes(int chefID) {
		try {
			String query = "select recipeID , recipeName from chef natural join recipe where chefID = " + chefID;
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookingRecipePortal","root","");//Enter the password of your SQL Workbench
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()) {
				System.out.println(" " + rs.getString(2));
			}
			System.out.println();
			} catch (Exception e) {
			System.out.println(e);
		}
	}
}

class chef{
	int chefID;
	String chefName;
	int chefRating;
}

class recipe{
	int recipeID;
	String recipeName;
	int likes;
	int dislikes;
	String ingredients;
}

class linker{
	int chefID;
	int recipeID;
	String ingredients;
}