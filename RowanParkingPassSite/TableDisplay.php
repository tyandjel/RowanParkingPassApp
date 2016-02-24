<?php

$result = mysqli_query($conn,"SELECT full_name, make, start_date, end_date FROM Driver, Requests, Vehicles
   Where request_state = ‘tba’ and Requests.driver_id = Driver.driver_id and Requests.vehicle_id = Vehicles.vehicle_id
   ORDER BY `CreatedTime` DESC");

echo "<table border='0' cellpadding='0' cellspacing='0' class='table-fill'> 
<tr>
<th width='250px' position='fixed'>Driver</th> 
<th width='150px'>Make</th>
<th width='100px'>Start Date</th>
<th>End Date</th>
<th>View Pass</th>
</tr>";


while($row = mysqli_fetch_array($result) ) {
echo "<tr>";
echo "<td>" . $row['full_name'] . "</td>";
echo "<td>" . $row['make'] . "</td>";
echo "<td>" . $row['start_date'] . "</td>";
echo "<td>" . $row['end_date'] . "</td>";
echo "<td><a href='PassDetails.php?id=".$row['pass_id']."'>View Job</td>";

echo "</tr>";
}
echo “</table>”;
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
