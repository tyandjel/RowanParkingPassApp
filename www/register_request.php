<?php
require_once("common.php");

//die('{"FLAG":false,"ERR":0}'); # Not Logged in
//die('{"FLAG":false,"ERR":1}'); # POST required
//die('{"FLAG":false,"ERR":2}');# Check parameters
//die('{"FLAG":false,"ERR":3}');# Database error
//die('{"FLAG":false,"ERR":4}');# invalid start date
//die('{"FLAG":false,"ERR":5}');# invalid end data
//die('{"FLAG":false,"ERR":6}');# Date range error
//die('{"FLAG":false,"ERR":7}');# No vehicle error
//die('{"FLAG":false,"ERR":8}');# No driver error

//echo '{"FLAG":false,"ERR":3}';# Database error

if(!$_SESSION['user'] ){
die('{"FLAG":false,"ERR":0}');
}

$vehicle = $_POST[':vehicle'];
$driver = $_POST[':driver'];
$start_date = $_POST[':start'];
$end_date = $_POST[':end'];
if($_POST[':vehicle'] && empty($vehicle)){
die('{"FLAG":false,"ERR":2,"PARAM":"vehicle"}');
}
if($_POST[':driver'] && empty($driver)){
die('{"FLAG":false,"ERR":2,"PARAM":"driver"}');
}


$vehicle = json_decode($_POST[':vehicle']);
$driver = json_decode($_POST[':driver']);
$vehicle_id = $vehicle->{'vehicle_id'};
$driver_id = $driver->{'driver_id'};

//die('{"PARAM":"'.urlencode($_POST[':driver']).'"}');

if($vehicle_id < 0){
    $query     = "
        INSERT INTO Vehicles
        (make,model,license,state,color,year)
        VALUES
		(:make,:model,:license,:state,:color,:year)";
    $array_ids = array(
        ':make' => $vehicle->{'make'},
        ':model' => $vehicle->{'model'},
        ':license' => $vehicle->{'license'},
        ':state' => intval($vehicle->{'state'}),
        ':color' => intval($vehicle->{'color'}),
        ':year' => intval($vehicle->{'year'})
    );
    try {
        // These two statements run the query against your database table.
        $stmt      = $db->prepare($query);
        $result    = $stmt->execute($array_ids);
        $return_id = $db->lastInsertId();
    }
    catch (PDOException $ex) {
        die('{"FLAG":false,"ERR":3,\"PDO_MSG\":"' . $ex->getMessage() . '"}'); # Database error
    }
    $vehicle_id = $return_id;
}else{
    // check vehicle/driver exists
    $query = "
            SELECT
            1
            FROM Vehicles
		    WHERE
            vehicle_id=:vehicle_id";
    $array_ids = array(':vehicle_id'=>$vehicle_id);
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        
        $result = $stmt->execute($array_ids);
        
    }
    catch (PDOException $ex) {
        die('{"FLAG":false,"ERR":3,\"PDO_MSG\":"' . $ex->getMessage() . '"}'); # Database error
	    goto ERR;
    }
    $row  = $stmt->fetch();
    if(!$row){
	    echo '{"FLAG":false,"ERR":7,"post":"'.urlencode(json_encode($vehicle_id)).'"}';
	    goto ERR;
    }
}
if($driver_id < 0){
    $query     = "
        INSERT INTO Driver
        (street,city,state,full_name,zip)
        VALUES
	    (:street,:city,:state,:full_name,:zip)";
    $array_ids = array(
        ':street' => $driver->{'street'},
        ':city' => $driver->{'city'},
        ':state' => $driver->{'state'},
        ':full_name' => $driver->{'full_name'},
        ':zip' => $driver->{'zip'}
    );
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
        $return_id   = $db->lastInsertId();
    }
    catch (PDOException $ex) {
        die('{"FLAG":false,"ERR":3,"PDO_ERR":"'.$ex->getMessage().'"}');# Database error
    }
    $driver_id = $return_id;
}else{
    $query = "
            SELECT
            1
            FROM Driver
		    WHERE
            driver_id=:driver_id";
    $array_ids = array(':driver_id'=>$driver_id);
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        die('{"FLAG":false,"ERR":3,"ERR_DB":"'.$ex->getMessage().'"}');
	    goto ERR;
    }

    $row  = $stmt->fetch();
    if(!$row){
	    echo '{"FLAG":false,"ERR":8,"GAVEME":"'.json_encode($driver).'"}';
	    goto ERR;
    }
}

$query = "
		SELECT
		1
		FROM Requests
		where start_date >= STR_TO_DATE(:startd,'%m/%d/%Y') and start_date <= STR_TO_DATE(:endd,'%m/%d/%Y') and end_date >= STR_TO_DATE(:startd,'%m/%d/%Y') and end_date <= STR_TO_DATE(:endd,'%m/%d/%Y') and vehicle_id=:vehicle_id and driver_id=:driver_id";
$array_ids = array(':startd'=>$start_date,
                   ':endd' => $end_date,
                    ':vehicle_id'=>$vehicle_id,
                    ':driver_id'=>$driver_id);
try {
	// These two statements run the query against your database table.
	$stmt   = $db->prepare($query);
	$result = $stmt->execute($array_ids);
}
catch (PDOException $ex) {
	die('{"FLAG":false,"ERR":3}'.$ex->getMessage());# Database error
}
$row  = $stmt->fetch();

if($row){
    die('{"FLAG":false,"ERR":6}');
}

// Check date conflicts
// add request
$query = "
        INSERT INTO Requests
        (vehicle_id,driver_id,user_id,status,start_date,end_date)
        VALUES
		(:vehicle_id,:driver_id,:user_id,:status,STR_TO_DATE(:start_date,'%m/%d/%Y'),STR_TO_DATE(:end_date,'%m/%d/%Y'))";
$array_ids = array(':vehicle_id'=>$vehicle_id,
	':driver_id'=>$driver_id,
	':user_id'=>$_SESSION['user']['user_id'],
	':status'=>intval(0),
	':start_date'=>$start_date,
	':end_date'=>$end_date);
try {
	// These two statements run the query against your database table.
	$stmt   = $db->prepare($query);
	$result = $stmt->execute($array_ids);
    $return_id   = $db->lastInsertId();
}
catch (PDOException $ex) {
	die('{"FLAG":false,"ERR":3,"ERR_DB":"'.$ex->getMessage().'"}');
}
die('{"FLAG":true,"id":'.json_encode($return_id).'}');

ERR:
die();