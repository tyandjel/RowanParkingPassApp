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
<tr><th colspan = 2>Ticket Writer</th></tr>
<tr>
<td>Plate Number: </td><td><input type="text" name="plate" placeholder="No Spaces"></td>
<tr></tr>
<td>Current Date: </td><td><input type="text" name="date" placeholder="YYYY/mm/dd"></td>
</tr>
</table>
    <input type="submit" value="Submit" />
    <input type="hidden" name="submit_button_pressed" value="1" />
</form>
<br><br><br>


</html>

<?php

require_once("common.php");

	if (!isset($_SESSION['username'])) {
    	header("Location: login.php");
		die("Redirecting..");
	}
	
	
	if(isset($_POST['submit_button_pressed']))
	{
		$plate = $_POST['plate'];
		$date = $_POST['date'];
		$image = '';
		if($date == '' || $date == null || $plate == '' || $plate == null)
		{
			echo '<script language="javascript">';
			echo 'alert("Please input a license and date")';
			echo '</script>';
		}
		else
		{
		
		$query = "INSERT INTO Tickets (license, offense_date, image_path) values (:plate, STR_TO_DATE(:date, '%Y/%m/%d'), :image)";
		
		
		$query_params = array(':plate' => $plate, ':date' => $date, ':image' => $image);

   try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
			
			echo '<script language="javascript">';
			echo 'alert("Ticket Successfully Created");';
			echo 'window.location = "../TicketTable.php";';
			echo '</script>';
			//header("Location: TicketTable.php");
			//die("Redirecting..");
			
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            echo '<script language="javascript">';
			echo 'alert("Enter all information and format the date YYYY/mm/dd")';
			echo '</script>';
        }
   //$row = $stmt->fetchAll();
   
   
		
	}
	}

?>