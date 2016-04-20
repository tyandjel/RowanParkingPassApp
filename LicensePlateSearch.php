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
		 form{
			display: inline;
		}
		.table1 td, tr, th, table{
			border: outset;
	font-family:Arial, Helvetica, sans-serif;
            font-size:24px;
		}
		.table2 td, tr, th, table{
			border: none;
			font-family:Arial, Helvetica, sans-serif;
            font-size:24px;
		}
 
      </style>
      
   </head>
<a href="logout.php">Logout</a>
<a href="TableDisplay.php">Home</a>
<a href="LicensePlateSearch.php">Search</a>
<a href="Ticket.php">Ticket</a>
<a href="TicketTable.php">Display Tickets</a><br><br><br>

<form action="" method="post">
<table class = "table2">
<tr>
<td>Plate Number: </td><td><input type="text" name="plate" placeholder="No Spaces"></td>
<tr></tr>
<td>Date: </td><td><input type="text" name="date" placeholder="YYYY/mm/dd"></td>
</tr>
</table>
    <input type="submit" value="Submit" />
    <input type="hidden" name="submit_button_pressed" value="1" />
</form>
<br><br><br>


</html>

<?php
{
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
	   case -32768:
	   $color_name = 'Orange';
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

	if (!isset($_SESSION['username'])) {
    	header("Location: login.php");
		die("Redirecting..");
	}
	
	
	if(isset($_POST['submit_button_pressed']))
	{
		//$plate = $_POST['plate'];
		$date = $_POST['date'];
		if($date == '' || $date == null)
		{
			$date = date('Y/m/d');
		}
		
		
		echo 'DATE SEARCHED: '.$date.'<br>';
		
		$query = "SELECT Requests.* FROM Requests left join Vehicles ON (Vehicles.vehicle_id = Requests.vehicle_id) WHERE Vehicles.license LIKE :plate AND Requests.status = 1 AND Requests.start_date <= STR_TO_DATE(:today, '%Y/%m/%d') AND Requests.end_date >= STR_TO_DATE(:today, '%Y/%m/%d') ORDER by time_stamp DESC";
		
		
		$query_params = array(':plate' => "%".$_POST['plate']."%", ':today' => $date);

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
   
   
   
   
   echo 'Plate Searched: '.$_POST['plate'].'<br>';
	echo '<table class = "table1 bgcolor = "#F5DAB7"">';
	echo '<tr><td><b>Start Date</b></td><td><b>End Date</b></td><td><b>Make</b></td><td><b>Model</b></td><td><b>License</b></td><td><b>Color</b></td></tr>';

    for($i = 0; $i < count($row); $i++) 
    {
		$temp = $row[$i];
		$query = "SELECT * from Vehicles WHERE vehicle_id = :id";
		
		
	$query_params = array(':id' => $temp['vehicle_id']);

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
	$vehicle =  $stmt->fetchAll();
	$vehicle = $vehicle[0];
   
		echo '<tr>';
		echo '<td>'.$temp['start_date'].'</td><td>'.$temp['end_date'].'</td><td>'.$vehicle['make'].'</td><td>'.$vehicle['model'].'</td><td>'.$vehicle['license'].'</td><td>'.getColor($vehicle['color']).'</td>';
		echo '</tr>';
	}
		echo '</table>';
		
	}

?>