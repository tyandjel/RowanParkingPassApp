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

if (isset($_POST[':make']) && empty($make)) {
    echo '{"FLAG":false,"ERR":2,"PARAM":":make"}'; # param error
    goto ERR;
}
if (isset($_POST[':model']) && empty($model)) {
    echo '{"FLAG":false,"ERR":2,"PARAM":":model"}'; # param error
    goto ERR;
}
if (isset($_POST[':license']) && empty($license)) {
    echo '{"FLAG":false,"ERR":2,"PARAM":":license"}'; # param error
    goto ERR;
}
if (isset($_POST[':state']) && empty($state)) {
    echo '{"FLAG":false,"ERR":2,"PARAM":":state"}'; # param error
    goto ERR;
}
if (isset($_POST[':color']) && empty($color)) {
    echo '{"FLAG":false,"ERR":2,"PARAM":":color"}'; # param error
    goto ERR;
}
if (isset($_POST[':year']) && empty($year)) {
    echo '{"FLAG":false,"ERR":2,"PARAM":":year"}'; # param error
    goto ERR;
}
if (isset($_POST[':vehicle_id']) && empty($vehicle_id)) {
    echo '{"FLAG":false,"ERR":2,"PARAM":":vehicle_id"}'; # param error
    goto ERR;
}
$year  = intval(strval($year));
$color = intval(strval($color));
if (!empty($vehicle_id) && !empty($_POST['kill']) && trim($_POST['kill']) == '1') {
    
    $query     = "
        DELETE FROM Vehicles
		WHERE vehicle_id=:vehicle_id";
    $array_ids = array(
        ':vehicle_id' => $vehicle_id
    );
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        echo '{"FLAG":false,"ERR":3}'; # Database error
        goto ERR;
    }
    die('{"FLAG":true}'); // delete success
}
if (empty($vehicle_id)) {
    $query     = "
            SELECT
            *
            FROM Vehicles
			WHERE
            make=:make and model=:model and license=:license and state=:state and color=:color and year=:year";
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
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        echo '{"FLAG":false,"ERR":3}'; # Database error
        goto ERR;
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
            echo '{"FLAG":false,"ERR":3}'; # Database error
            goto ERR;
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
        echo '{"FLAG":false,"ERR":3}'; # Database error
        goto ERR;
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
            echo '{"FLAG":false,"ERR":3}'; # Database error
            goto ERR;
        }
        echo '{"FLAG":true}';
    } else {
        echo '{"FLAG":false,"ERR":7}'; # Database error
        goto ERR;
    }
} //vehicle id
die();
ERR:
header("HTTP/1.1 500 Internal Server Error");