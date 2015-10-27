/*
 * Name: Juan Carlo Cervera
 * Course: CNT 4714 Summer 2013
 * Assignment Title: Program 2 - MySQL Database Application
 * Date: June 21, 2013
 * 
 * Class: Project2Win
 */
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

import com.mysql.jdbc.Connection;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Project2Win extends JFrame 
{
   // JDBC driver, database URL, username and password
   static String Driver;
   static String Database;
   static String Username;
   static String Password;
   
   // default query retrieves all data from bikes table
   static final String DefQuery = "SELECT * FROM bikes";
   static String Query = DefQuery;
   
   private ResultSetTableModel tableModel;
   private JTextArea queryArea;
   private JLabel EntDataInfo;
   private JTextField UserTxt;
   private JTextField PassTxt;
   private Connection connection;
   private JTable resultTable; 
   private JScrollPane scrollPane_1;
   // create ResultSetTableModel and GUI
   public Project2Win() 
   {   
      super( "Project 2 Window" );
        
      getContentPane().setLayout(null);
	 
     //Enter Database information prompt label
	 EntDataInfo = new JLabel();
	 EntDataInfo.setSize(146, 32);
	 EntDataInfo.setLocation(20,11);
	 EntDataInfo.setText("Enter Database Information");
	 getContentPane().add(EntDataInfo);
	 
	 // set up JButton for submitting queries
	 JButton submitButton = new JButton( "Execute SQL Command" );
	 submitButton.setBounds(764, 178, 163, 23);
	 getContentPane().add( submitButton );
	 
	 //Clears the text in the query text area
	 JButton clearButton = new JButton("Clear Command");
	 clearButton.addMouseListener(new MouseAdapter() {
	 	@Override
	 	public void mouseClicked(MouseEvent e) {
	 		queryArea.setText("");
	 	}
	 });
	 clearButton.setBounds(532, 178, 163, 23);
	 getContentPane().add( clearButton );	
	 
	 //JDBC Driver prompt for the driver drop down menu
	 JLabel lblJdbcDriver = new JLabel("JDBC Driver");
	 lblJdbcDriver.setBounds(20, 46, 62, 14);
	 getContentPane().add(lblJdbcDriver);
	 
	 //Database URL prompt for the database menu
	 JLabel lblDatabaseUrl = new JLabel("Database URL");
	 lblDatabaseUrl.setBounds(20, 71, 82, 14);
	 getContentPane().add(lblDatabaseUrl);
	 
	 //Username prompt for the Username text field
	 JLabel lblUsername = new JLabel("Username");
	 lblUsername.setBounds(20, 96, 62, 14);
	 getContentPane().add(lblUsername);
	 
	 //Password prompt for the Password text field
	 JLabel lblPassword = new JLabel("Password");
	 lblPassword.setBounds(20, 121, 62, 14);
	 getContentPane().add(lblPassword);
	 
	 //Drop down menu for the list of drivers
	 final JComboBox drivers = new JComboBox();
	 drivers.setModel(new DefaultComboBoxModel(new String[] {"com.mysql.jdbc.Driver", "com.ibm,db2.jdbc.netDB2Driver", "oracle.jdbc.driver.OracleDriver", "com.jdbc.odbc.jdbcOdbcDriver"}));
	 drivers.setBounds(110, 43, 182, 20);
	 getContentPane().add(drivers);
	 
	 //Drop down menu for the list of database URLs
	 final JComboBox dataUrl = new JComboBox();
	 dataUrl.setModel(new DefaultComboBoxModel(new String[] {"jdbc:mysql://localhost:3310/project2", "jdbc:mysql://localhost:3306/bikedb", "jdbc:mysql://localhost:3306/project2"}));
	 dataUrl.setBounds(112, 68, 180, 20);
	 getContentPane().add(dataUrl);
	 
	 //Text field to input username
	 UserTxt = new JTextField();
	 UserTxt.setBounds(110, 93, 182, 20);
	 getContentPane().add(UserTxt);
	 UserTxt.setColumns(10);
	 
	 //Text field to input password
	 PassTxt = new JTextField();
	 PassTxt.setBounds(110, 118, 182, 20);
	 getContentPane().add(PassTxt);
	 PassTxt.setColumns(10);
	 
	 //Label that signifies whether the application is connected to a URL or not
	 final JLabel lblNotConnectedNow = new JLabel("Not Connected Now");
	 lblNotConnectedNow.setBounds(20, 187, 277, 14);
	 getContentPane().add(lblNotConnectedNow);
	 
	 // set up JTextArea in which user types queries
	 queryArea = new JTextArea(3, 100);
	 queryArea.setBounds(307, 44, 617, 123);
	 getContentPane().add(queryArea);
	 queryArea.setWrapStyleWord( true );
	 queryArea.setLineWrap( true );
	 
	 //Label prompting the area to input the SQL command
	 JLabel lblEnterSqlCommand = new JLabel("Enter SQL Command");
	 lblEnterSqlCommand.setBounds(307, 20, 130, 14);
	 getContentPane().add(lblEnterSqlCommand);
	 
	 //Button that takes in the driver, database url, username, and password and
	 //creates a connection to the driver and database.
	 JButton connectButton = new JButton( "Connect to Database ");
	 connectButton.setBounds(307, 178, 163, 23);
	 getContentPane().add( connectButton );
	 connectButton.addMouseListener(new MouseAdapter() {
	 	@Override
	 	public void mouseClicked(MouseEvent arg0) {
	 		//gathers information
	 		Username = UserTxt.getText();
			Password = PassTxt.getText();
			Driver = (String) drivers.getSelectedItem();
			Database = (String) dataUrl.getSelectedItem();
			
			try {
				// Load the JDBC driver
			       Class.forName(Driver);
			       lblNotConnectedNow.setText("Driver loaded");
			       
				// Establish a connection
				   connection = (Connection) DriverManager.getConnection(Database, Username, Password);	
				   lblNotConnectedNow.setText("Connect to "+connection);
				   
				  //create the table model 
				   tableModel = new ResultSetTableModel( Driver, connection, DefQuery);
				   
				   //Put the created table model on the window
				     resultTable = new JTable( tableModel );
					 scrollPane_1 = new JScrollPane( resultTable );
					 scrollPane_1.setBounds(20, 212, 972, 206);
					 getContentPane().add( scrollPane_1 );

				}
				catch(ClassNotFoundException e1){
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	 	}
	 });

	 // create event listener for submitButton
	 submitButton.addActionListener( 
	 
	    new ActionListener() 
	    {
	       // pass query to table model
	       public void actionPerformed( ActionEvent event )
	       {
	          // perform a new query
	          try 
	          {
	        	  //Get the SQL command from the query text area
	        	  Query = queryArea.getText();
	        	  //If it's a select command, use the query function
	        	 if(Query.charAt(0) == 's' || Query.charAt(0) == 'S'){
	                 tableModel.setQuery( Query );
	        	 }
	        	 //other update
	        	 else{
	        		 tableModel.setUpdate( Query ); 
	        	 }
	          } // end try
	          catch ( SQLException sqlException ) 
	          {
	             JOptionPane.showMessageDialog( null, 
	                sqlException.getMessage(), "Database error", 
	                JOptionPane.ERROR_MESSAGE );
	             
	             // try to recover from invalid user query 
	             // by executing default query
	             try 
	             {
	                tableModel.setQuery( DefQuery );
	                queryArea.setText( DefQuery );
	             } // end try
	             catch ( SQLException sqlException2 ) 
	             {
	                JOptionPane.showMessageDialog( null, 
	                   sqlException2.getMessage(), "Database error", 
	                   JOptionPane.ERROR_MESSAGE );
	 
	                // ensure database connection is closed
	                tableModel.disconnectFromDatabase();
	 
	                System.exit( 1 ); // terminate application
	             } // end inner catch                   
	          } // end outer catch
	       } // end actionPerformed
	    }  // end ActionListener inner class          
	 ); // end call to addActionListener
	 
	 //Button used to clear the table
	 JButton clrResultBtn = new JButton("Clear Result Table");
	 clrResultBtn.addMouseListener(new MouseAdapter() {
	 	@Override
	 	public void mouseClicked(MouseEvent e) {
	 		tableModel.removeAllRows();
	 	}
	 });
	 clrResultBtn.setBounds(10, 429, 163, 23);
	 getContentPane().add( clrResultBtn );
		

	 setSize( 1020, 504 ); // set window size
	 setVisible( true ); // display window  
      
      // dispose of window when user quits application (this overrides
      // the default of HIDE_ON_CLOSE)
      setDefaultCloseOperation( DISPOSE_ON_CLOSE );
      
      // ensure database connection is closed when user quits application
      addWindowListener(
      
         new WindowAdapter() 
         {
            // disconnect from database and exit when window has closed
            public void windowClosed( WindowEvent event )
            {
               tableModel.disconnectFromDatabase();
               System.exit( 0 );
            } // end method windowClosed
         } // end WindowAdapter inner class
      ); // end call to addWindowListener
   } // end DisplayQueryResults constructor
   
   // execute application
   public static void main( String args[] ) 
   {
      new Project2Win();     
   } // end main
} // end class DisplayQueryResults