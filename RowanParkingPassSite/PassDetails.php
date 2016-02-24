PassDetails.php

<?php
   include (‘Config.php’);
   
   $result = mysqli_query($conn,"SELECT request_id, full_name, street, city, zip, year, make, model, license, start_date, end_date
      FROM Driver, Requests, Vehicles WHERE request_id = '".$_GET['id']."' and request_type = 'tba' and 
      Requests.vehicle_id = Vehicles.vehicle_id and Requests.driver_id = Driver.driver_id ORDER BY `CreatedTime` DESC");

   $passdetails = mysqli_fetch_assoc($result);

   echo 'Pass ID : '.$passdetails['request_id'].'<br>';
   echo 'Driver Name: '.$passdetails['full_name'].'<br>';
   echo 'Location: '.$passdetails['street'].'<br>';
   echo 'Location: '.$passdetails['city'].'<br>';
   echo 'Location: '.$passdetails['zip'].'<br>';
   echo Vehicle Year: '.$passdetails['year'].'<br>';
   echo Vehicle Make: '.$passdetails['make'].'<br>';
   echo Vehicle Model: '.$passdetails['model'].'<br>';
   echo License Plate: '.$passdetails['license'].'<br>';
   echo Start Date: '.$passdetails['start_date'].'<br>';
   echo End Date: '.$passdetails['end_date'].'<br>';


<form action="" method="post">
    <input type="submit" value="Accept" />
    <input type="hidden" name="accept_button_pressed" value="1" />
</form>

<form action="" method="post">
    <input type="submit" value="Deny" />
    <input type="hidden" name="deny_button_pressed" value="1" />
</form>

if(isset($_POST['accept_button_pressed']))
{
    $sql = “UPDATE Requests “. “SET request_type=accepted”.“WHERE request_id = $passdetails[‘request_id’]”;
    $retval = mysql_query($sql, $db);
    $to      = 'nobody@example.com';
    $subject = 'the subject';
    $message = 'hello here’s your nice new shiny pass.';
    $headers = 'From: webmaster@example.com' . "\r\n" .
        'Reply-To: webmaster@example.com' . "\r\n" .
        'X-Mailer: PHP/' . phpversion();

    mail($to, $subject, $message, $headers);

    echo 'Pass Accepted. Email Sent.';
    header(‘refresh:5; Location: TableDisplay’);
    exit; 
}

if(isset($_POST['deny_button_pressed']))
{
    $sql = “UPDATE Requests “. “SET request_type=denied”.“WHERE request_id = $passdetails[‘request_id’]”;
    $retval = mysql_query($sql, $db);

    $to      = 'nobody@example.com';
    $subject = 'the subject';
    $message = 'Your parking pass for bla bla date has been denied.';
    $headers = 'From: webmaster@example.com' . "\r\n" .
        'Reply-To: webmaster@example.com' . "\r\n" .
        'X-Mailer: PHP/' . phpversion();

    mail($to, $subject, $message, $headers);
    echo 'Pass Denied. Email Sent.';
    header(‘refresh:5; Location: TableDisplay’);
    exit;
}
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
