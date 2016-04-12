<?php
require_once("common.php");
if($_SESSION['user'] && $_POST && $_POST[':make'] && $_POST[':model'] && $_POST[':license'] && $_POST[':state'] && $_POST[':color'] && $_POST[':year'] && ctype_digit($_POST[':state']) && ctype_digit($_POST[':color']) && ctype_digit($_POST[':year'])){
	if(!$_POST[':vehicle_id']){
	$query = "
            SELECT
            *
            FROM Vehicles
			WHERE
            make=:make and model=:model and license=:license and state=:state and color=:color and year=:year";
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
}
else{
	if($_POST['kill'] && $_POST['kill']=='1'){
		$query = "
            DELETE FROM Driver
			WHERE driver_id=:driver_id";
    $array_ids = array(':driver_id'=>$_POST[':driver_id']);
	try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        goto ERR;
    }
	die('{"FLAG":true}'); // delete success
	}else{
	$query = "
            SELECT
            *
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
	if($row){
		$query = "
            UPDATE Driver
            SET street=:street, city=:city, full_name=:full_name, zip=:zip
            WHERE driver_id=:driver_id";
    $array_ids = array(':street'=>$_POST[':street'],
	':city'=>$_POST[':city'],
	':full_name'=>$_POST[':full_name'],
	':zip'=>intval($_POST[':zip']),
	':driver_id'=>$_POST[':driver_id']);
	try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        goto ERR;
    }
	echo '{"FLAG":true}';
	}else{
		goto ERR;
	}
}//if kill else
}//vehicle id
}else{ //main
	goto ERR;
}
die();
ERR:
echo '{"FLAG":false}';
header("HTTP/1.1 500 Internal Server Error");