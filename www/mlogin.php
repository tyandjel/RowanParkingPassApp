<?php
// First we execute our common code to connection to the database and start the session
require_once("common.php");

//Skips page if logged in
if (isset($_SESSION['username'])) {
   header("Location: TableDisplay.php");
    die("Redirecting..");
}


$name_err;
$pw_err;

$usrname;

$NoError = true;
// This if statement checks to determine whether the registration form has been submitted
// If it has, then the registration code is run, otherwise the form is displayed
if (!empty($_POST)) {
    $usrname = $_POST['username'];
    // Ensure that the user has entered a non-empty username
    if (empty($usrname)) {
        // Note that die() is generally a terrible way of handling user errors
        // like this.  It is much better to display the error with the form
        // and allow the user to correct their mistake.  However, that is an
        // exercise for you to implement yourself.
        $name_err = "Please enter a username.";
        $NoError  = false;
    }
    
    if (strpos($usrname,' ') == true) {
        $name_err = "Usernames can only consists of letters and numbers";
        $NoError  = false;
    }
    
    // Ensure that the user has entered a non-empty password
    $pass = $_POST['password'];
    if (empty($pass) || strlen($pass) > 25) {
        $pw_err  = "Please enter a password. Max 25 characters";
        $NoError = false;
    }
    
    if ($NoError) {
		$query = "
            SELECT
            *
            FROM admin
            WHERE
                username = :username
        ";
        
        // This contains the definitions for any special tokens that we place in
        // our SQL query.  In this case, we are defining a value for the token
        // :username.  It is possible to insert $_POST['username'] directly into
        // your $query string; however doing so is very insecure and opens your
        // code up to SQL injection exploits.  Using tokens prevents this.
        // For more information on SQL injections, see Wikipedia:
        // http://en.wikipedia.org/wiki/SQL_Injection
        $query_params = array(
            ':username' => $_POST['username']
        );
        //die($_POST['username']);
        try {
            // These two statements run the query against your database table.
            $stmt   = $db->prepare($query);
            $result = $stmt->execute($query_params);
        }
        catch (PDOException $ex) {
            // Note: On a production website, you should not output $ex->getMessage().
            // It may provide an attacker with helpful information about your code. 
            die("Failed to run query: " . $ex->getMessage());
        }
        
        // The fetch() method returns an array representing the "next" row from
        // the selected results, or false if there are no more rows to fetch.
        $row  = $stmt->fetch();
		
		$salt = $row["salt"];
            
		// This hashes the password with the salt so that it can be stored securely
		// in your database.  The output of this next statement is a 64 byte hex
		// string representing the 32 byte sha256 hash of the password.  The original
		// password cannot be recovered from the hash.  For more information:
		// http://en.wikipedia.org/wiki/Cryptographic_hash_function
		$password = hash('sha256', $_POST['password'] . $salt);
		for ($round = 0; $round < 65536; $round++) {
			$password = hash('sha256', $password . $salt);
		}
		
		$activation = md5(uniqid(rand(), true));
		if($row['password']==$password){
			$_SESSION['username'] = $row['username'];
            $_SESSION['insert'] = "Login success.";
            // This redirects the user back to the login page after they register
            header("Location: LicensePlateSearch.php");
            
            // Calling die or exit after performing a redirect using the header function
            // is critical.  The rest of your PHP script will continue to execute and
            // will be sent to the user if you do not die or exit.
            die("Redirecting..");
		}
		else{
			$name_err = "Invalid Login.";
		}
    } //NoError check
}

include 'Template.php';
$view = new Template();
 
$view->title = "Login Page - Rowan Parking Pass";

$registerform = '<body bgcolor = "#FFFFFF">
      <div align = "center">
         <div class = "outerBox" align = "left">
            <div class = "innerBox"><b>Login</b></div>
            <div style = "margin:30px">
               <form action = "" method = "post">
			   <label>UserName  :</label><input type = "text" name = "username" class = "box"/><br />
			   <FONT COLOR=Red>';
if ($name_err) {
     $registerform .= "<br />";
}
$registerform .= $name_err . '</FONT>
<br /><br />
<label>Password  :</label><input type = "password" id="password1" name = "password" class = "box" /><br/><br />
<br />
    <input id="cmdregister" type="submit" value="Login" />
</form>';

$view->side = $side;
$ref_ar = array(array('title'=>'', 'msg'=> $registerform));
$view->links = $ref_ar;
if($_SESSION['insert']){
	$message = '<div align = "center"><FONT COLOR=green>' . $_SESSION['insert'] . '</FONT></div>';
	array_unshift($ref_ar,array('title'=>"",'msg'=>$message));
	unset($_SESSION['insert']);
}
$view->links = $ref_ar;

//$view->content = $view->render('content.php');
echo $view->render('main.template.php');