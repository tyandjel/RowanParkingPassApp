PassDetails.php

<?php
   include (‘Config.php’);
   
   $result = mysqli_query($conn,"SELECT * FROM passTable WHERE pass_id = '".$_GET['id']."' ORDER BY `CreatedTime` DESC");

   $passdetails = mysqli_fetch_assoc($result);

   echo 'Pass ID : '.$passdetails['pass_id'].'<br>';
   echo 'Driver Name: '.$passdetails['driver_name'].'<br>';
   echo 'Location: '.$passdetails['location'].'<br>';
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
    $sql = “UPDATE passTable “. “SET pass_type=accepted”.“WHERE pass_id = $passdetails[‘id’]”;
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
    $sql = “UPDATE passTable “. “SET pass_type=denied”.“WHERE pass_id = $passdetails[‘id’]”;
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
