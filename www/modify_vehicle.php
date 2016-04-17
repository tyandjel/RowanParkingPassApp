<?php
require_once("common.php");
if (empty($_SESSION['user'])) {
    echo '{"FLAG":false,"ERR":2}'; # param error
    goto ERR;
}

$make       = trim($_POST[':make']);
$model      = trim($_POST[':model']);
$license    = trim($_POST[':license']);
$state      = trim($_POST[':state']);
$color      = trim($_POST[':color']);
$year       = trim($_POST[':year']);
$vehicle_id = trim($_POST[':vehicle_id']);
$kill       = trim($_POST['kill']);

if (isset($_POST[':make']) && empty($make)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":make"}'); # param error
}
if (isset($_POST[':model']) && empty($model)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":model"}'); # param error
}
if (isset($_POST[':license']) && empty($license)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":license"}'); # param error
}
if (isset($_POST[':state']) && empty($state)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":state"}'); # param error
}
if (isset($_POST[':color']) && empty($color)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":color"}'); # param error
}
if (isset($_POST[':year']) && empty($year)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":year"}'); # param error
}
if (isset($_POST[':vehicle_id']) && empty($vehicle_id)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":vehicle_id"}'); # param error
}
$year  = intval(strval($year));
$color = intval(strval($color));
if (!empty($vehicle_id) && !empty($_POST['kill']) && $kill == '1') {
    
    $query     = "
        DELETE FROM  UserVehicles
        WHERE user_id=:user_id and vehicle_id=:vehicle_id";
    $array_ids = array(
        ':vehicle_id' => $vehicle_id,
        ':user_id' => $_SESSION['user']['user_id']
    );
    
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        die('{"FLAG":false,"ERR":3,\"PDO_MSG\":' . $ex->getMessage() . '}'); # Database error
    }
    die('{"FLAG":true}'); // delete success
}
if (empty($vehicle_id)) {
    $query     = "
            SELECT
            Vehicles.*
            FROM Vehicles
            LEFT JOIN UserVehicles ON (UserVehicles.vehicle_id = Vehicles.vehicle_id)
			WHERE UserVehicles.user_id = :us_id and Vehicles.make=:make and Vehicles.model=:model and Vehicles.license=:license and Vehicles.state=:state and Vehicles.color=:color and Vehicles.year=:year";
    $array_ids = array(
        ':make' => $make,
        ':model' => $model,
        ':license' => $license,
        ':state' => intval($state),
        ':color' => intval($color),
        ':year' => intval($year),
        ':us_id' => $_SESSION['user']['user_id']
    );
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        die('{"FLAG":false,"ERR":3,\"PDO_MSG\":' . $ex->getMessage() . '}'); # Database error
    }
    $row = $stmt->fetch();
    
    if ($row) {
        $return_id = $row['vehicle_id'];
    } else {
        $query     = "
            INSERT INTO Vehicles
            (make,model,license,state,color,year)
            VALUES
			(:make,:model,:license,:state,:color,:year)";
        $array_ids = array(
            ':make' => $make,
            ':model' => $model,
            ':license' => $license,
            ':state' => intval($state),
            ':color' => intval($color),
            ':year' => intval($year)
        );
        try {
            // These two statements run the query against your database table.
            $stmt      = $db->prepare($query);
            $result    = $stmt->execute($array_ids);
            $return_id = $db->lastInsertId();
        }
        catch (PDOException $ex) {
            die('{"FLAG":false,"ERR":3,\"pdocode\":' . $ex->getCode() . '}'); # Database error
        }
    }
    
    // Updates junction table
    $query     = "
        INSERT INTO UserVehicles
        (vehicle_id,user_id)
        VALUES
		(:vehicle_id,:user_id)";
    $array_ids = array(
        ':vehicle_id' => $return_id,
        ':user_id' => $_SESSION['user']['user_id']
    );
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        if ($ex->getCode() != 23000) {
            die('{"FLAG":false,"ERR":3,\"pdocode\":' . $ex->getCode() . '}'); # Database error
        }
    }
    echo '{"FLAG":true,"id":' . strval($return_id) . '}';
} else {
    $query     = "
            SELECT
            *
            FROM Vehicles
			WHERE
            vehicle_id=:vehicle_id";
    $array_ids = array(
        ':vehicle_id' => $vehicle_id
    );
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        die('{"FLAG":false,"ERR":3,\"pdocode\":' . $ex->getCode() . '}'); # Database error
    }
    $row = $stmt->fetch();
    if ($row) {
        $query     = "
            UPDATE Vehicles
            SET make=:make, model=:model, license=:license, state=:state, color=:color, year=:year
            WHERE vehicle_id=:vehicle_id";
        $array_ids = array(
            ':make' => $make,
            ':model' => $model,
            ':license' => $license,
            ':state' => intval($state),
            ':color' => intval($color),
            ':year' => intval($year),
            ':vehicle_id' => $vehicle_id
        );
        try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($array_ids);
        }
        catch (PDOException $ex) {
            die('{"FLAG":false,"ERR":3,\"pdocode\":' . $ex->getCode() . '}'); # Database error
        }
        echo '{"FLAG":true}';
    } else {
        echo '{"FLAG":false,"ERR":7}'; # Database error
        goto ERR;
    }
} //vehicle id
die();
ERR:
die();