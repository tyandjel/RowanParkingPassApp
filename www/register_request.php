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
//die();
// if vehicle exists send id and check unction table for user association
	$query = "
            SELECT
            *
            FROM Vehicles
			WHERE
            make=:make and model=:model and license=:license and state=:state and color=:color and year=:year";
    $array_ids = array(':make'=>$_POST[':make'],
	':model'=>$_POST[':model'],
	':license'=>$_POST[':license'],
	':state'=>($_POST[':state']),
	':color'=>($_POST[':color']),
	':year'=>($_POST[':year']));
	try {
        // These two statements run the query against your database table.
        
        $stmt   = $db->prepare($query);
		
        $result = $stmt->execute($array_ids);
		
    }
    catch (PDOException $ex) {
        goto ERR;
    }
    $row  = $stmt->fetch();
	if($row){
		echo '{"FLAG":true,"id":'.$row['vehicle_id'].'}';
	}else{
		$query = "
            INSERT INTO Vehicles
            (make,model,license,state,color,year)
            VALUES
			(:make,:model,:license,:state,:color,:year)";
		$array_ids = array(':make'=>$_POST[':make'],
		':model'=>$_POST[':model'],
		':license'=>$_POST[':license'],
		':state'=>intval($_POST[':state']),
		':color'=>intval($_POST[':color']),
		':year'=>intval($_POST[':year']));
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
		// insert grab key and send it back
	}
}else{
	goto ERR;
}
die();
ERR:
echo '{"FLAG":false}';
header("HTTP/1.1 500 Internal Server Error");