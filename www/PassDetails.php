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
   //PassDetails.php
   require_once("common.php");
   //include (‘Config.php’);

if (isset($_SESSION['username'])) {
    echo nl2br("\nWelcome to the member's area, " . $_SESSION['username'] . "!\n ");
} else {
    header("Location: login.php");
    die("Redirecting..");
}


   $id = $_GET['id'];


   $query = "SELECT Requests.*, Driver.*, Vehicles.*, User.* FROM Requests, Driver, Vehicles, User WHERE request_id = :id And 
				Requests.driver_id = Driver.driver_id And Requests.vehicle_id = Vehicles.vehicle_id And Requests.user_id = User.user_id";
   $query_params = array(':id' => $_GET['id']);

   try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
	    echo "Query Success";
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }
   $row = $stmt->fetchAll();
   //echo json_encode($row[0]);
   //$result = mysqli_query($conn,"SELECT * FROM passTable WHERE pass_id = '".$_GET['id']."' ORDER BY `CreatedTime` DESC");

   $passdetails = $row[0];

   if($passdetails['status'] != 0 || $passdetails == null)
   {
       header("Location: TableDisplay.php");
       die("Redirecting..");
   }

   echo '<br>';
   
		
	echo'<table border="1" >
	<tr><td><th>Request ID</th><td>'.$passdetails['request_id'] . ' </td></tr>
	<tr><td><th>User Account</th><td>'.$passdetails['user_name'] . ' </td></tr>
	<tr><td><th>Pass Start Date</th><td>'.$passdetails['start_date'] . ' </td></tr>
	<tr><td><th>Pass End Date</th><td>'.$passdetails['end_date'] . ' </td></tr>
	<tr><td><th>Driver Name</th><td>'.$passdetails['full_name'] . ' </td></tr>
	<tr><td><th>Driver Street</th><td>'.$passdetails['street'] . ' </td></tr>
	<tr><td><th>Driver City</th><td>'.$passdetails['city'] . ' </td></tr>
	<tr><td><th>Driver State</th><td>'.$passdetails['Driver.state'] . ' </td></tr>
	<tr><td><th>Driver Zip</th><td>'.$passdetails['zip'] . ' </td></tr>
	<tr><td><th>Vehicle Color</th><td>'.$passdetails['color'] . ' </td></tr>
	<tr><td><th>Vehicle Year</th><td>'.$passdetails['year'] . ' </td></tr>
	<tr><td><th>Vehicle Make</th><td>'.$passdetails['make'] . ' </td></tr>
	<tr><td><th>Vehicle Model</th><td>'.$passdetails['model'] . ' </td></tr>
	<tr><td><th>Vehicle State</th><td>'.$passdetails['Vehicles.state'] . ' </td></tr>
	<tr><td><th>Vehicle License</th><td>'.$passdetails['license'] . ' </td></tr>';

        echo '</table>';
$full_email = $passdetails['user_name'] . "@students.rowan.edu";
if(isset($_POST['accept_button_pressed']))
{
    echo "Approve";
    $status = 1;
    $query = "UPDATE Requests ". "SET status=$status "."WHERE request_id = :id";
    $query_params = array(':id' => $_GET['id']);

    try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
			//echo "Query Success";
			echo nl2br("\n");
			/*$retval = mysql_query($sql, $db);
			$to      = $full_email;
			$subject = 'Rowan Parking Pass';
			$message = 'hello here’s your nice new shiny pass.';
			$headers = 'From: webmaster@example.com' . "\r\n" .
			'Reply-To: webmaster@example.com' . "\r\n" .
			'X-Mailer: PHP/' . phpversion();
			mail($to, $subject, $message, $headers);
			echo 'Pass Accepted. Email Sent.';
			header(‘refresh:5; Location: TableDisplay’);
			exit; */
			
			//echo 'Pass Accepted. Email Sent.';
			//echo "<script>setTimeout(\"location.href = 'TableDisplay.php';\",3000);</script>";
			//sleep(3);
			//header("Location: TableDisplay.php");
			//exit;
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }


    header("Location: TableDisplay.php");
    die("Redirecting..");
}

if(isset($_POST['deny_button_pressed']))
{
    echo "DENY";
    $status = 2;
    $query = "UPDATE Requests ". "SET status=$status "."WHERE request_id = :id";
    $query_params = array(':id' => $_GET['id']);

    try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
			echo "Query Success";
			
			/*$retval = mysql_query($sql, $db);
			$to      = $full_email;
			$subject = 'Rowan Parking Pass';
			$message = 'Your pass has been denied';
			$headers = 'From: webmaster@example.com' . "\r\n" .
			'Reply-To: webmaster@example.com' . "\r\n" .
			'X-Mailer: PHP/' . phpversion();
			mail($to, $subject, $message, $headers);
			echo 'Pass Accepted. Email Sent.';
			header(‘refresh:5; Location: TableDisplay’);
			exit; */
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }

     header("Location: TableDisplay.php");
    die("Redirecting..");

}
?>
</body>
</html>
<html>
<form action="" method="post">
    <input type="submit" value="Accept" />
    <input type="hidden" name="accept_button_pressed" value="1" />
</form>

<form action="" method="post">
    <input type="submit" value="Deny" />
    <input type="hidden" name="deny_button_pressed" value="1" />
</form>
</html>
