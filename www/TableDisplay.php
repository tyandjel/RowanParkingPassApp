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

$query = "SELECT * FROM Requests Where status = 0 ORDER BY `time_stamp` LIMIT 20";

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
     //echo json_encode($row[0]);

     //
     if($row)
     {

     }
     else
     {
	echo "No requests found";
	header($_SERVER['SERVER_PROTOCOL'] . ' 500 Internal Server Error', true, 500);
        die();

     }



     echo '<table bgcolor = "#F5DAB7">';
	echo '<th colspan = "5"><b>Unprocessed Requests</b></th>';
	 echo '<tr><td><b>User Name</b></td><td><b>Start Date</b></td><td><b>End Date</b></td><td><b>Request ID</b></td><td><b>Request Link</b></td></tr>';
     for($i = 0; $i < count($row); $i++) 
     {
	$temp = $row[$i];
	
	$query = "SELECT * FROM User WHERE user_id = :id";
	$query_params = array(':id' => $temp['user_id']);

	try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
	    //echo "Query Success Requests";
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }
	$user = $stmt->fetchAll();
	$user = $user[0];
	echo '<tr>';
    echo "<td>" . $user['user_name'] . "</td>";
	echo "<td>" . $temp['start_date'] . "</td>";
	echo "<td>" . $temp['end_date'] . "</td>";
	echo "<td>" . $temp['request_id'] . "</td>";
	echo "<td><a href='PassDetails.php?id=".$temp['request_id']."'>View Request</td></a></td>";
	echo '</tr>';
     }

     echo "</table>";











$query = "SELECT * FROM Requests Where status != 0 ORDER BY `time_stamp` DESC";

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
     //echo json_encode($row[0]);

     //
     if($row)
     {

     }
     else
     {
	echo "No requests found";
	//header($_SERVER['SERVER_PROTOCOL'] . ' 500 Internal Server Error', true, 500);
        die();

     }



     echo '<table bgcolor = "#F5DAB7">';
	echo '<th colspan = "6"><b>Processed Requests</b></th>';
	 echo '<tr><td><b>User Name</b></td><td><b>Start Date</b></td><td><b>End Date</b></td><td><b>Request ID</b></td><td><b>Current Status</b></td><td><b>Request Link</b></td></tr>';
     for($i = 0; $i < count($row); $i++) 
     {
	$temp = $row[$i];
	
	$query = "SELECT * FROM User WHERE user_id = :id";
	$query_params = array(':id' => $temp['user_id']);

	try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
	    //echo "Query Success Requests";
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }
	$user = $stmt->fetchAll();
	$user = $user[0];

	if($temp['status'] == 1)
	{
		$status = 'Approved';
	}
	else
	{
		$status = 'Denied';
	}
	
	echo '<tr>';
    echo "<td>" . $user['user_name'] . "</td>";
	echo "<td>" . $temp['start_date'] . "</td>";
	echo "<td>" . $temp['end_date'] . "</td>";
	echo "<td>" . $temp['request_id'] . "</td>";
	echo "<td>" . $status . "</td>";
	echo "<td><a href='PassDetails.php?id=".$temp['request_id']."'>View Request</td></a></td>";
	echo '</tr>';
     }

     echo "</table>";

?>

