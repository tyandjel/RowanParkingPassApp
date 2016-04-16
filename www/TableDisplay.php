<html>
   
   <head>
      <title>Rowan Parking</title>
   </head>
   <body>
      <style type = "text/css">
         body {
            font-family:Arial, Helvetica, sans-serif;
            font-size:14px;
         }
		 th,td{
			 border-width:0px 1px 1px 0px;
		 }
      </style>
      
 <a href="logout.php">Logout</a>


 <?php

require_once("common.php");

if (isset($_SESSION['username'])) {
    echo nl2br("\nWelcome to the member's area, " . $_SESSION['username'] . "!\n ");
} else {
    header("Location: login.php");
    die("Redirecting..");
}

//$query = "SELECT * FROM Requests Where status = 0 ORDER BY `start_date` ASC";
$query = "SELECT Requests.*, Driver.full_name FROM Requests,Driver Where status = 0 AND Requests.driver_id = Driver.driver_id ORDER BY `start_date` ASC";
try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute();

            echo "Query successful";
	    echo nl2br("\n\n\n");
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }


     $row = $stmt->fetchAll();
     //echo json_encode($row[0]);

     echo nl2br("\n\n\n");

     if($row)
     {

     }
     else
     {
	echo "No requests found";
	header($_SERVER['SERVER_PROTOCOL'] . ' 500 Internal Server Error', true, 500);
        die();

     }



     echo'<table border="1" ><th>Driver Name</th><th>Driver ID</th><th>Start Date</th><th>End Date</th><th>Request ID</th><th>View Job</th>';
     for($i = 0; $i < count($row); $i++) 
     {
	$res = $row[$i];
	echo'<tr><td>'.$res['full_name'].
		'</td><td>'.$res['driver_id'].
		'</td><td>'.$res['start_date'].
		'</td><td>'.$res['end_date'].
		'</td><td>'.$res['request_id'].
		"</td><td><a href='PassDetails.php?id=".$res['request_id']."'>View Job</td></a></tr>";
		//'</td><td><a href=PassDetails.php>View Job</a></td></tr>';
		//'</td><td><a href=PassDetails.php?id= '.$res['request_id']. '>View Job</td></a></tr>';
		
	//echo "<tr><td>" . $temp['full_name'] . "</td>";
    //echo "<td>" . $temp['driver_id'] . "</td>";
	//echo "<td>" . $temp['start_date'] . "</td>";
	//echo "<td>" . $temp['end_date'] . "</td>";
	//echo "<td>" . $temp['request_id'] . "</td>";
	//echo "<td><a href='PassDetails.php?id=".$temp['request_id']."'>View Job</td></a><\tr>";
	//echo nl2br("\n");
     }

        echo '</table>';




 ?>
 </body>
</html>

