<?php
function modifyVehicle($veh_row){
	require_once("common.php");
	// If this row was meant to be added and id synced
	if($veh_row[':server_id']=-1){
		$query = "
		SELECT
		1
		FROM Vehicles
		WHERE
		make=:make and model=:model and license=:license and state=:state and color=:color and year=:year
		";
		$query_params = array(':make' => $veh_row[':make'],
		':model' => $veh_row[':model'],
		':license' => $veh_row[':license'],
		':state' => $veh_row[':state'],
		':color' => $veh_row[':color'],
		':year' => $veh_row[':year'],
		);

		try {
			$stmt   = $db->prepare($query);
			$result = $stmt->execute($query_params);
		}
		catch (PDOException $ex) {
			die("Failed to run query: " . $ex->getMessage());
		}
		$row  = $stmt->fetch();
		if ($row) {
			return $row['vehicle_id'];
		}
		// Didn't contain
		$query = "
		INSERT INTO Vehicles(make,model,license,state,color,year)
		VALUES(:make,:model,:license,:state,:color,:year)
		";
		try {
			$stmt   = $db->prepare($query);
			$result = $stmt->execute($query_params);
		}
		catch (PDOException $ex) {
			die("Failed to run query: " . $ex->getMessage());
		}
	}else{
	}
}
function setUser($user_blah){
	require_once("common.php");
	$query = "
	SELECT
	*
	FROM User
	WHERE
	user_name = :username
	";
	$user_blah = $user_blah.split("@")[0]; // If there is an @ it is trunc
	$query_params = array(':username' => $user_blah);
	
	try {
		$stmt   = $db->prepare($query);
		$result = $stmt->execute($query_params);
	}
	catch (PDOException $ex) {
		die("Failed to run query: " . $ex->getMessage());
	}
	$row  = $stmt->fetch();
	
	if ($row) {
		$_SESSION['user'] = $row;
		return;
	}
	
	//From here this means a new user is being added
	$query = "
	INSERT INTO User
	(user_name, is_admin)
	VALUES
	(:un,:ia)";
	$query_params = array(':un'=>$user_blah,':ia'=>false);
	try {
		$stmt   = $db->prepare($query);
		$stmt->execute($query_params);
        $temp = $db->lastInsertId();
	}
	catch (PDOException $ex) {
		// Note: On a production website, you should not output $ex->getMessage().
		// It may provide an attacker with helpful information about your code. 
		die("Failed to run query: " . $ex->getMessage());
	}
    
    // GET USER OUT AND SET THE SESSION USER
    //die("WTF2");
    $query = "
	SELECT
	*
	FROM User
	WHERE
	user_id = :uid
	";
    
	$query_params = array(":uid"=>intval($temp));
	try {
		$stmt   = $db->prepare($query);
		$result = $stmt->execute($query_params);
	}
	catch (PDOException $ex) {
		die("Failed to run query: " . $ex->getMessage());
	}
	$row  = $stmt->fetch();
	
	if ($row) {
		$_SESSION['user'] = $row;
	}
	// ERR
}
function checkUser(){
	return $_SESSION['user'];
}