<?php
require_once("common.php");
	if($_SESSION['username']){
		print "Account added.";
	}else{
		header("Location: register.php");
		die("Goodbye cruel world..");
	}
?>