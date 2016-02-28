<?php
	require_once("common.php");
	unset($_SESSION['username']);
	$_SESSION['insert'] = "Logout success.";
	header("Location: message.php");
	die("Redirecting..");
?>
