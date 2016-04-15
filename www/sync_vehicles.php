<?php
require_once("common.php");

function extract_ID_ARRAY($obj_rows,$col_name){
    $returnMe = array();
    foreach ($obj_rows as $k => $row){
        $returnMe[] = $row->{$col_name};
    }
    return $returnMe;
}
if(empty($_POST)){
    echo '{"FLAG":false,"ERR":1}';# Not post
    goto ERR;
}
$WAWA = $_POST["json_obj"];
if(!empty($_SESSION['user'])){
    if( empty($WAWA)){
        echo '{"FLAG":false,"ERR":2}';# Check parameters
        goto ERR;
        //goto CONT;
    }
    $P_OBJ = json_decode($WAWA);
    $array_ids = extract_ID_ARRAY($P_OBJ,'vehicle_id');
    $qMarks = str_repeat('?,', count($array_ids));
    $qMarks = rtrim($qMarks,",");
	$array_ids[] = $_SESSION['user']['user_id']; // This will be users ID
    $query = "
            SELECT
            *
            FROM Vehicles
			LEFT JOIN UserVehicles ON (UserVehicles.vehicle_id = Vehicles.vehicle_id)
			WHERE Vehicles.vehicle_id NOT IN($qMarks) AND UserVehicles.user_id = ?";
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
		echo '{"FLAG":false,"ERR":3}';# Database error
        goto ERR;
    }
    $row  = $stmt->fetchAll();
    
    foreach ($P_OBJ as $k => $Trow){
    $query = "
            INSERT INTO Vehicles
            (vehicle_id,make,model,license,state,color,year)
            VALUES(:vehicle_id,:make,:model,:license,:state,:color,:year)
            ON DUPLICATE KEY UPDATE
            vehicle_id=:vehicle_id,make=:make,model=:model,license=:license,state=:state,color=:color,year=:year";

        $array_ids = array(':vehicle_id'=>$Trow->{'vehicle_id'},':make'=>$Trow->{'make'},':model'=>$Trow->{'model'},
                           ':license'=>$Trow->{'license'},':state'=>$Trow->{'state'},':color'=>$Trow->{'color'},':year'=>$Trow->{'year'});

        // IF id =0 this forces auto increment. DO NO LET!!
        try {
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($array_ids);
        }
        catch (PDOException $ex) {
            echo '{"FLAG":false,"ERR":3}';# Database error
            goto ERR;
        }
		
		$query = "
            INSERT INTO UserVehicles
            (vehicle_id,user_id)
            VALUES(:vehicle_id,:user_id)
            ON DUPLICATE KEY UPDATE
            vehicle_id=:vehicle_id,user_id=:user_id";

        $array_ids = array(':vehicle_id'=>$Trow->{'vehicle_id'},':user_id'=>$_SESSION['user']['user_id']);
        // IF id =0 this forces auto increment. DO NO LET!!
        try {
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($array_ids);
        }
        catch (PDOException $ex) {
            echo '{"FLAG":false,"ERR":3}';# Database error
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
header("HTTP/1.1 500 Internal Server Error");