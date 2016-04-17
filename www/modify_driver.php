<?php
require_once("common.php");

if (empty($_SESSION['user'])) {
    echo '{"FLAG":false,"ERR":2}'; # param error
    goto ERR;
}

$street    = trim($_POST[':street']);
$city      = trim($_POST[':city']);
$full_name = trim($_POST[':full_name']);
$zip       = trim(strval($_POST[':zip']));
$state       = trim(strval($_POST[':state']));
$d_id      = trim($_POST[':driver_id']);
$kill      = trim($_POST['kill']);

if (isset($_POST[':street']) && empty($street)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":street"}'); # param error
}
if (isset($_POST[':city']) && empty($city)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":city"}'); # param error
}
if (isset($_POST[':full_name']) && empty($full_name)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":full_name"}'); # param error
}
if (isset($_POST[':zip']) && empty($zip)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":zip"}'); # param error
}
if (isset($_POST[':driver_id']) && empty($d_id)) {
    die('{"FLAG":false,"ERR":2,"PARAM":":driver_id"}'); # param error
}

$zip = intval(strval($zip));

if (!empty($d_id) && !empty($_POST['kill']) && $kill == '1') {
    
    $query     = "
            DELETE FROM UserDrivers
			WHERE user_id=:user_id and driver_id=:driver_id";
    $array_ids = array(
        ':driver_id' => $d_id,
        ':user_id' => $_SESSION['user']['user_id']
    );
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        die('{"FLAG":false,"ERR":3,"PDO_ERR":"'.$ex->getMessage().'"}');# Database error
    }
    die('{"FLAG":true}'); // delete success
}

if (empty($d_id)) {
    $query = "
            SELECT
            Driver.*
            FROM Driver
            LEFT JOIN UserDrivers ON (UserDrivers.driver_id = Driver.driver_id)
			WHERE
            UserDrivers.user_id=:us_id and street=:street and city=:city and full_name=:full_name and state=:state and zip=:zip";
    
    $array_ids = array(
        ':street' => $_POST[':street'],
        ':city' => $city,
        ':state'=> $state,
        ':full_name' => $full_name,
        ':zip' => $zip,
        ':us_id' => $_SESSION['user']['user_id']
    );
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        die('{"FLAG":false,"ERR":3,"PDO_ERR":"'.$ex->getMessage().'"}');# Database error
    }
    $row = $stmt->fetch();
    if ($row) {
		$return_id = $row['driver_id'];
    } else {
        $query     = "
            INSERT INTO Driver
            (street,city,state,full_name,zip)
            VALUES
			(:street,:city,:state,:full_name,:zip)";
        $array_ids = array(
            ':street' => $street,
            ':city' => $city,
            ':state' => $state,
            ':full_name' => $full_name,
            ':zip' => $zip
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
    }
    // Updates junction table
    $query     = "
        INSERT INTO UserDrivers
        (driver_id,user_id)
        VALUES
		(:driver_id,:user_id)";
    $array_ids = array(
        ':driver_id' => $return_id,
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
            FROM Driver
			WHERE
            driver_id=:driver_id";
    $array_ids = array(
        ':driver_id' => $d_id
    );
    try {
        // These two statements run the query against your database table.
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($array_ids);
    }
    catch (PDOException $ex) {
        die('{"FLAG":false,"ERR":3,"PDO_ERR":"'.$ex->getMessage().'"}');# Database error
    }
    
    $row = $stmt->fetch();
    if ($row) {
        $query     = "
            UPDATE Driver
            SET street=:street, city=:city, full_name=:full_name, zip=:zip, state=:state
            WHERE driver_id=:driver_id";
        $array_ids = array(
            ':street' => $street,
            ':city' => $city,
            ':full_name' => $full_name,
            ':state' => $state,
            ':zip' => intval($zip),
            ':driver_id' => $d_id
        );
        try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($array_ids);
        }
        catch (PDOException $ex) {
            die('{"FLAG":false,"ERR":3,"PDO_ERR":"'.$ex->getMessage().'"}');# Database error
        }
        echo '{"FLAG":true}';
    } else {
        echo '{"FLAG":false,"ERR":8}'; # Database error
        goto ERR;
    }
} //driver id
die();
ERR:
die();