<?php
require_once("common.php");

function extract_ID_ARRAY($obj_rows,$col_name){
    $returnMe = array();
    foreach ($obj_rows as $k => $row){
        $returnMe[] = $row->{$col_name};
    }
    return $returnMe;
}

$WAWA = $_POST["json_obj"];
if(!empty($_SESSION['user'])){
    if( empty($WAWA)){
        die('{"FLAG":false,"ERR":3,"ERR_MSG":"wawa"}');
    }
    $row = [];
    $P_OBJ = json_decode($WAWA);
    $array_ids = extract_ID_ARRAY($P_OBJ,'driver_id');
    array_unshift($array_ids,-1);
    $qMarks = str_repeat('?,', count($array_ids));
    $qMarks = rtrim($qMarks,",");
	$array_ids[] = $_SESSION['user']['user_id']; // This will be users ID
    $query = "
            SELECT
            *
            FROM Driver
			LEFT JOIN UserDrivers ON (UserDrivers.driver_id = Driver.driver_id)
			WHERE Driver.driver_id NOT IN($qMarks) AND UserDrivers.user_id = ?";
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
		die('{"FLAG":false,"ERR":3}'.$ex->getMessage());# Database error
    }
    $row  = $stmt->fetchAll();
    //die(json_encode($array_ids));
    foreach ($P_OBJ as $k => $Trow){
		$query = "
            INSERT INTO Driver
            (driver_id,street,state,city,full_name,zip)
            VALUES(:driver_id,:street,:state,:city,:full_name,:zip)
            ON DUPLICATE KEY UPDATE
            driver_id=:driver_id,street=:street,city=:city,full_name=:full_name,zip=:zip";
        $array_ids = array(':driver_id'=>$Trow->{'driver_id'},':street'=>$Trow->{'street'},':city'=>$Trow->{'city'},
                           ':full_name'=>$Trow->{'full_name'},':zip'=>$Trow->{'zip'},':state'=>$Trow->{'state'});
        // IF id =0 this forces auto increment. DO NO LET!!
        try {
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($array_ids);
        }
        catch (PDOException $ex) {
            die('{"FLAG":false,"ERR":3}'.$ex->getMessage());# Database error
            goto ERR;
        }
		
		$query = "
            INSERT INTO UserDrivers
            (driver_id,user_id)
            VALUES(:driver_id,:user_id)
            ON DUPLICATE KEY UPDATE
            driver_id=:driver_id,user_id=:user_id";
		$array_ids = array(':driver_id'=>$Trow->{'driver_id'},':user_id'=>$_SESSION['user']['user_id']);
        // IF id =0 this forces auto increment. DO NO LET!!
        try {
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($array_ids);
        }
        catch (PDOException $ex) {
            die('{"FLAG":false,"ERR":3}'.$ex->getMessage());# Database error
            goto ERR;
        }
    }
	
    die('{"JSONS":"'.urlencode(json_encode($row)).'"}');
}else{
    print '{"FLAG":false,"ERR":0}'; # Not Logged in
    goto ERR;
}
die();
ERR:
die();
//header("HTTP/1.1 500 Internal Server Error");