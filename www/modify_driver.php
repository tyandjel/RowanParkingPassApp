<?php
require_once("common.php");
if($_SESSION['user'] && $_POST && $_POST[':street'] && $_POST[':city'] && $_POST[':full_name'] && $_POST[':zip'] && ctype_digit($_POST[':zip'])){
	if(!$_POST[':driver_id']){
	$query = "
            SELECT
            *
            FROM Driver
			WHERE
            street=:street and city=:city and full_name=:full_name and zip=:zip";
    $array_ids = array(':street'=>$_POST[':street'],
	':city'=>$_POST[':city'],
	':full_name'=>$_POST[':full_name'],
	':zip'=>intval($_POST[':zip']));
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
		echo '{"FLAG":true,"id":'.$row['driver_id'].'}';
	}else{
		$query = "
            INSERT INTO Driver
            (street,city,full_name,zip)
            VALUES
			(:street,:city,:full_name,:zip)";
		$array_ids = array(':street'=>$_POST[':street'],
	':city'=>$_POST[':city'],
	':full_name'=>$_POST[':full_name'],
	':zip'=>intval($_POST[':zip']));
		try {
			// These two statements run the query against your database table.
			$stmt   = $db->prepare($query);
			$result = $stmt->execute($array_ids);
		}
		catch (PDOException $ex) {
			goto ERR;
		}
		$temp = $stmt->fetch(PDO::FETCH_ASSOC);
		echo '{"FLAG":true,"id":'.json_encode($temp).'}';
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
}//driver id
}else{ //main
	goto ERR;
}
die();
ERR:
echo '{"FLAG":false}';
header("HTTP/1.1 500 Internal Server Error");