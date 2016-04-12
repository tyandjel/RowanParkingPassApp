<?php
require_once("common.php");

die('{"FLAG":false,"ERR":0}'); # Not Logged in
die('{"FLAG":false,"ERR":1}'); # POST required
die('{"FLAG":false,"ERR":2}');# Check parameters
die('{"FLAG":false,"ERR":3}');# Database error
die('{"FLAG":false,"ERR":4}');# invalid start date
die('{"FLAG":false,"ERR":5}');# invalid end data
die('{"FLAG":false,"ERR":6}');# Date range error
die('{"FLAG":false,"ERR":7}');# No vehicle error
die('{"FLAG":false,"ERR":8}');# No driver error


if($_SESSION['user'] && $_POST && $_POST[':vehicle_id'] && $_POST[':driver_id'] && $_POST[':start_date'] && $_POST[':end_date']){
    die('{"FLAG":true,"id":1}');
	// check vehicle/driver exists
	$query = "
            SELECT
            1
            FROM Vehicles
			WHERE
            vehicle_id=:vehicle_id";
    $array_ids = array(':vehicle_id'=>$_POST[':vehicle_id']);
	try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        goto ERR;
    }
    $row  = $stmt->fetch();
	if(!$row){
		echo '{"FLAG":false,"ERR":7}';
		goto ERR;
	}
	$query = "
            SELECT
            1
            FROM Driver
			WHERE
            driver_id=:driver_id";
    $array_ids = array(':driver_id'=>$_POST[':driver_id']);
	try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        goto ERR;
    }
    $row  = $stmt->fetch();
	if(!$row){
		echo '{"FLAG":false,"ERR":8}';
		goto ERR;
	}
	// Check date conflicts
	// add request
	$query = "
            INSERT INTO Requests
            (vehicle_id,driver_id,user_id,status,start_date,end_date)
            VALUES
			(:vehicle_id,:driver_id,:user_id,:status,:start_date,:end_date)";
	$array_ids = array(':vehicle_id'=>$_POST[':vehicle_id'],
		':driver_id'=>$_POST[':driver_id'],
		':user_id'=>$_SESSION['user']['user_id'],
		':status'=>intval(0),
		':start_date'=>intval($_POST[':start_date']),
		':end_date'=>intval($_POST[':end_date']));
	try {
			// These two statements run the query against your database table.
			$stmt   = $db->prepare($query);
			$result = $stmt->execute($array_ids);
		}
		catch (PDOException $ex) {
			echo $ex;
			goto ERR;
		}
		$temp = $stmt->fetch(PDO::FETCH_ASSOC);
		echo '{"FLAG":true,"id":'+json_encode($temp)+'}';
}else{
	echo '{"FLAG":false,"ERR":2}';
	goto ERR;
}
die();
ERR:
header("HTTP/1.1 500 Internal Server Error");