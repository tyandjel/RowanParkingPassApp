<html>
   
   <head>
      <title>Rowan Parking</title>
       <body bgcolor = "#FAEBD7">
	  </body>
      <style type = "text/css">
         body 	{
            font-family:Arial, Helvetica, sans-serif;
            font-size:24px;
				}
		 table, th, td {
			border: outset;
			font-family:Arial, Helvetica, sans-serif;
            font-size:24px;
			}
		
 
      </style>
      
   </head>
<a href="logout.php">Logout</a>
<a href="TableDisplay.php">Home</a>
<a href="LicensePlateSearch.php">Search</a>
<a href="Ticket.php">Ticket</a>
<a href="TicketTable.php">Display Tickets</a>
</html>



<?php

require_once("common.php");

if (isset($_SESSION['username'])) {
    //echo nl2br("\nWelcome to the member's area, " . $_SESSION['username'] . "!\n ");
} else {
    header("Location: login.php");
    die("Redirecting..");
}

$query = "SELECT * FROM Tickets ORDER BY time_stamp DESC";

try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute();

            //	echo "Query successful";
	    echo nl2br("\n\n\n");
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }


     $row = $stmt->fetchAll();



     echo '<table bgcolor = "#F5DAB7">';
	echo '<th colspan = "3"><b>Tickets</b></th>';
	 echo '<tr><td><b>License</b></td><td><b>Offense Date</b></td><td><b>ID</b></td></tr>';
     for($i = 0; $i < count($row); $i++) 
     {
	$temp = $row[$i];
	
	echo '<tr>';
    echo "<td>" . $temp['license'] . "</td>";
	echo "<td>" . $temp['offense_date'] . "</td>";
	echo "<td>" . $temp['ticket_id'] . "</td>";
	echo '</tr>';
     }

     echo "</table>";




?>