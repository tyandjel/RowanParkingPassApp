<html>
   
   <head>
      <title>Rowan Parking</title>
	  <body bgcolor = "#FAEBD7">
	  </body>
      
      <style type = "text/css">
         body {
            font-family:Arial, Helvetica, sans-serif;
            font-size:24px;
			
         }
		 table, th, td {
    border: outset;
	font-family:Arial, Helvetica, sans-serif;
            font-size:24px;
}
		form{
			display: inline;
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
{
function getState($state)
{
	$state_name = '';
	switch($state)
	{
	   case 0:
		$state_name = 'AK';
		break;
		  
	   case 1:
		$state_name = 'AL';
		break;   
	   case 2:
		$state_name = 'AR';
		break;   
	   case 3:
		$state_name = 'AZ';
		break;   
	   case 4:
		$state_name = 'CA';
		break;   
	   case 5:
		$state_name = 'CO';
		break;   
	   case 6:
		$state_name = 'CT';
		break;   
	   case 7:
		$state_name = 'DE';
		break;   
	   case 8:
		$state_name = 'FL';
		break;   
	   case 9:
		$state_name = 'GA';
		break;   
	   case 10:
		$state_name = 'HI';
		break;   
	   case 11:
		$state_name = 'IA';
		break;   
	   case 12:
		$state_name = 'ID';
		break;   
	   case 13:
		$state_name = 'IL';
		break;   
	   case 14:
		$state_name = 'IN';
		break;   
	   case 15:
		$state_name = 'KS';
		break;   
	   case 16:
		$state_name = 'KY';
		break;   
	   case 17:
		$state_name = 'LA';
		break;   
	   case 18:
		$state_name = 'MA';
		break;   
	   case 19:
		$state_name = 'MD';
		break;   
	   case 20:
		$state_name = 'ME';
		break;   
	   case 21:
		$state_name = 'MI';
		break;   
	   case 22:
		$state_name = 'MN';
		break;   
	   case 23:
		$state_name = 'MO';
		break;   
	   case 24:
		$state_name = 'MS';
		break;   
	   case 25:
		$state_name = 'MT';
		break;   
	   case 26:
		$state_name = 'NC';
		break;   
	   case 27:
		$state_name = 'ND';
		break;   
	   case 28:
		$state_name = 'NE';
		break;   
	   case 29:
		$state_name = 'NH';
		break;   
	   case 30:
		$state_name = 'NJ';
		break;   
	   case 31:
		$state_name = 'NM';
		break;   
	   case 32:
		$state_name = 'NV';
		break;   
	   case 33:
		$state_name = 'NY';
		break;   
	   case 34:
		$state_name = 'OH';
		break;   
	   case 35:
		$state_name = 'OK';
		break;   
	   case 36:
		$state_name = 'OR';
		break;   
	   case 37:
		$state_name = 'PA';
		break;   
	   case 38:
		$state_name = 'RI';
		break;   
	   case 39:
		$state_name = 'SC';
		break;   
	   case 40:
		$state_name = 'SD';
		break;   
	   case 41:
		$state_name = 'TN';
		break;   
	   case 42:
		$state_name = 'TX';
		break;   
	   case 43:
		$state_name = 'UT';
		break;   
	   case 44:
		$state_name = 'VA';
		break;   
	   case 45:
		$state_name = 'VT';
		break;   
	   case 46:
		$state_name = 'WA';
		break;   
	   case 47:
		$state_name = 'WI';
		break;   
	   case 48:
		$state_name = 'WV';
		break;   
	   case 49:
		$state_name = 'WY';
		break;  
		default:
		$state_name = 'Not found';
		break;
	}
	return $state_name;
}
function getColor($color)
{
	$color_name = '';
	switch($color)
	{
		
	   case -65536:
	   $color_name = 'Red';
	   break;
	   case -16640:
	   $color_name = 'Light Orange';
	   break;
	   case -256:
	   $color_name = 'Yellow';
	   break;
	   case -10496:
	   $color_name = 'Gold';
	   break;
	   case -4194560:
	   $color_name = 'Light Green';
	   break;
	   case -16711936:
	   $color_name = 'Green';
	   break;
	   case -16217592:
	   $color_name = 'Dark Green';
	   break;
	   case -16711681:
	   $color_name = 'Light Blue';
	   break;
	   case -16728065:
	   $color_name = 'Blue';
	   break;
	   case -16776961:
	   $color_name = 'Dark Blue';
	   break;
	   case -4259585:
	   $color_name = 'Purple';
	   break;
	   case -65281:
	   $color_name = 'Violet';
	   break;
	   case -65408:
	   $color_name = 'Hot Pink';
	   break;
	   case -65472:
	   $color_name = 'Pink';
	   break;
	   case -2968436:
	   $color_name = 'Tan';
	   break;
	   case -12904693:
	   $color_name = 'Brown';
	   break;
	   case -2565928:
	   $color_name = 'Silver';
	   break;
	   case -8092540:
	   $color_name = 'Grey';
	   break;
	   case -16777216:
	   $color_name = 'Black';
	   break;
	   case -1:
	   $color_name = 'White';
	   break;
	   default:
	   $color_name = 'Not found';
	   break;
	}
	return $color_name;
}
	
	
}

   require_once("common.php");


if (isset($_SESSION['username'])) {
    echo nl2br("\n\n\n");
} else {
    header("Location: login.php");
    die("Redirecting..");
}



   $query = "SELECT * FROM Requests WHERE request_id = :id";
   $query_params = array(':id' => $_GET['id']);

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
   $row = $stmt->fetchAll();
   //echo json_encode($row[0]);
   //$result = mysqli_query($conn,"SELECT * FROM passTable WHERE pass_id = '".$_GET['id']."' ORDER BY `CreatedTime` DESC");

   $passdetails = $row[0];

   if($passdetails == null)
   {
       header("Location: TableDisplay.php");
       die("Redirecting..");
   }

   $query = "SELECT * FROM Vehicles WHERE vehicle_id = :id";   
   $query_params = array(':id' => $passdetails['vehicle_id']);

   try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
	    //echo "Query Success Vehicle";
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }
   $row = $stmt->fetchAll();
   //echo json_encode($row[0]);
   //$result = mysqli_query($conn,"SELECT * FROM passTable WHERE pass_id = '".$_GET['id']."' ORDER BY `CreatedTime` DESC");

   $vehicle = $row[0];
   
   $query = "SELECT * FROM Driver WHERE driver_id = :id";
   $query_params = array(':id' => $passdetails['driver_id']);

   try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
	    //echo "Query Success Driver";
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }
   $row = $stmt->fetchAll();
   //echo json_encode($row[0]);
   //$result = mysqli_query($conn,"SELECT * FROM passTable WHERE pass_id = '".$_GET['id']."' ORDER BY `CreatedTime` DESC");

   $driver = $row[0];
   
   
   $query = "SELECT * FROM User WHERE user_id = :id";
   $query_params = array(':id' => $passdetails['user_id']);

   try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
	    //echo "Query Success User";
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }
   $row = $stmt->fetchAll();
   //echo json_encode($row[0]);
   //$result = mysqli_query($conn,"SELECT * FROM passTable WHERE pass_id = '".$_GET['id']."' ORDER BY `CreatedTime` DESC");

   $users = $row[0];
   $state_name = getState($driver['state']);
   $color_name = getColor($vehicle['color']);
   
   $zip = ''.$driver['zip'];
   $l = 5 - strlen($zip);
   
   while($l > 0)
   {
	   $zip = '0'.$zip;
	   $l = $l - 1;
   }

   echo '
   <table bgcolor = "#F5DAB7">
   <th colspan = "2">
   '.'Pass Details'.'
   </th>
   <tr>
    <td>User</td><td>'.$users['user_name'].'</td>
	</tr>
	<tr>
    <td>Pass ID</td><td>'.$passdetails['request_id'].'</td>
    </tr>
	<tr>
	<td>Driver Name</td><td>'.$driver['full_name'].'</td>
    </tr>
	<tr>
	<td>Location</td><td>'.$driver['street'].' '.$driver['city'].', '.$state_name.' '.$zip.'</td>
    </tr>
	<tr>
	<td>Vehicle Year</td><td>'.$vehicle['year'].'</td>
    </tr>
	<tr>
	<td>Vehicle Make</td><td>'.$vehicle['make'].'</td>
    </tr>
	<tr>
	<td>Vehicle Model</td><td>'.$vehicle['model'].'</td>
    </tr>
	<tr>
	<td>Vehicle Color</td><td>'.$color_name.'</td>
    </tr>
	<tr>
	<td>License Plate</td><td>'.$vehicle['license'].'</td>
    </tr>
	<tr>
	<td>Start Date</td><td>'.$passdetails['start_date'].'</td>
    </tr>
	<tr>
	<td>End Date</td><td>'.$passdetails['end_date'].'</td>
	</tr>
	</table><br>';





if(isset($_POST['accept_button_pressed']))
{
    //echo "Approve";
    $status = 1;
    $query = "UPDATE Requests ". "SET status=$status "."WHERE request_id = :id";
    $query_params = array(':id' => $_GET['id']);

    try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
	    //echo "Query Success";
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }

    $to      = $users['user_name'].'@students.rowan.edu';
    $subject = 'Parking Pass';
    $message = 'Your pass has been approved.';
    

    mail($to, $subject, $message);

    header("Location: TableDisplay.php");
    die("Redirecting..");



}

if(isset($_POST['deny_button_pressed']))
{
    $status = 2;
    $query = "UPDATE Requests ". "SET status=$status "."WHERE request_id = :id";
    $query_params = array(':id' => $_GET['id']);

    try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
	    //echo "Query Success";
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }

     


    $to      = $users['user_name'].'@students.rowan.edu';
    $subject = 'Parking Pass';
    $message = 'Your pass has been DENIED.';
    

    mail($to, $subject, $message);
	
	header("Location: TableDisplay.php");
    die("Redirecting..");
}
if(isset($_POST['return_button_pressed']))
{
	header("Location: TableDisplay.php");
    die("Redirecting..");
}
?>
<html>
<form action="" method="post">
    <input type="submit" value="Accept" />
    <input type="hidden" name="accept_button_pressed" value="1" />
</form>

<form action="" method="post">
    <input type="submit" value="Deny" />
    <input type="hidden" name="deny_button_pressed" value="1" />
</form>

<form action="" method="post">
    <input type="submit" value="Return" />
    <input type="hidden" name="return_button_pressed" value="1" />
</form>
</html>