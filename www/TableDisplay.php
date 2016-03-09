<?php

require_once("common.php");

if (isset($_SESSION['username'])) {
    echo nl2br("Welcome to the member's area, " . $_SESSION['username'] . "!\n ");
} else {
    header("Location: login.php");
    die("Redirecting..");
}

$query = "SELECT * FROM Requests Where status = 0 ORDER BY `time_stamp` DESC";

try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute();

            echo "Query successful";
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }


     $row = $stmt->fetchAll();
     echo json_encode($row[0]);


     if($row)
     {

     }
     else
     {
	echo "No requests found";
	header($_SERVER['SERVER_PROTOCOL'] . ' 500 Internal Server Error', true, 500);
        die();

     }



     echo "</tr>";
     for($i = 0; $i < count($row); $i++) 
     {
	echo "test";
        echo "<td>" . $row['driver_id'] . "</td>";
	echo "<td>" . $row['start_date'] . "</td>";
	echo "<td>" . $row['end_date'] . "</td>";
	echo "<td><a href='PassDetails.php?id=".$row['pass_id']."'>View Job</td>";
     }

        echo "</tr>";
}
echo “</table>”;*/
?>
<html>
   
   <head>
      <title>Rowan Parking</title>
      
      <style type = "text/css">
         body {
            font-family:Arial, Helvetica, sans-serif;
            font-size:14px;
         }
 
      </style>
      
   </head>
<a href="logout.php">Logout</a>
</html>
