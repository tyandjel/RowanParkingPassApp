<?php
require_once("common.php");
	if($_SESSION['username']){
		print $_SESSION['insert'];
		unset($_SESSION['insert']);
	}else{
		header("Location: login.php");
	}
?>