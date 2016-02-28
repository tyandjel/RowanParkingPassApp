<?php
// First we execute our common code to connection to the database and start the session
require_once("common.php");

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
        // We will use this SQL query to see whether the username entered by the
        // user is already in use.  A SELECT query is used to retrieve data from the database.
        // :username is a special token, we will substitute a real value in its place when
        // we execute the query.
        $query = "
            SELECT
                1
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
        // If a row was returned, then we know a matching username was found in
        // the database already and we should not allow the user to continue.
        if (!(empty($row))) {
            $name_err = "This username is already in use";
            $NoError  = false;
        }
        
        if ($NoError) {
            
            // An INSERT query is used to add new rows to a database table.
            // Again, we are using special tokens (technically called parameters) to
            // protect against SQL injection attacks.
            $query = "
            INSERT INTO admin (
            	username,
                password,
                salt
            ) VALUES (
            	:username,
                :password,
                :salt
            )
        ";
            
            // A salt is randomly generated here to protect again brute force attacks
            // and rainbow table attacks.  The following statement generates a hex
            // representation of an 8 byte salt.  Representing this in hex provides
            // no additional security, but makes it easier for humans to read.
            // For more information:
            // http://en.wikipedia.org/wiki/Salt_%28cryptography%29
            // http://en.wikipedia.org/wiki/Brute-force_attack
            // http://en.wikipedia.org/wiki/Rainbow_table
            $salt = dechex(mt_rand(0, 2147483647)) . dechex(mt_rand(0, 2147483647));
            
            // This hashes the password with the salt so that it can be stored securely
            // in your database.  The output of this next statement is a 64 byte hex
            // string representing the 32 byte sha256 hash of the password.  The original
            // password cannot be recovered from the hash.  For more information:
            // http://en.wikipedia.org/wiki/Cryptographic_hash_function
            $password = hash('sha256', $_POST['password'] . $salt);
            
            // Next we hash the hash value 65536 more times.  The purpose of this is to
            // protect against brute force attacks.  Now an attacker must compute the hash 65537
            // times for each guess they make against a password, whereas if the password
            // were hashed only once the attacker would have been able to make 65537 different 
            // guesses in the same amount of time instead of only one.
            for ($round = 0; $round < 65536; $round++) {
                $password = hash('sha256', $password . $salt);
            }
            
            $activation = md5(uniqid(rand(), true));
            
            $query_params = array(
                ':username' => $_POST['username'],
                ':password' => $password,
                ':salt' => $salt
            );
            
            try {
                $stmt   = $db->prepare($query);
				$stmt->execute($query_params);
            }
            catch (PDOException $ex) {
                // Note: On a production website, you should not output $ex->getMessage().
                // It may provide an attacker with helpful information about your code. 
                die("Failed to run query: " . $ex->getMessage());
            }
            $_SESSION['username'] = $_POST['username'];
            $_SESSION['insert'] = "Account added.";
            // This redirects the user back to the login page after they register
            header("Location: message.php");
            
            // Calling die or exit after performing a redirect using the header function
            // is critical.  The rest of your PHP script will continue to execute and
            // will be sent to the user if you do not die or exit.
            die("Redirecting..");
        } //NoError check 2
    } //NoError check 2
}

include 'Template.php';
$view = new Template();
 
$view->title = "Register";
$view->jscript = '
$(document).ready(function () {
   $("#password1").keyup(check_pass);
   $("#password2").keyup(check_pass);
});
	function check_pass(){
	if($("#password1").val() == $("#password2").val()){
		$("#cmdregister").removeAttr("disabled");
		$("#passerr").html("<FONT COLOR=Green>passwords match.</FONT>");
		
	}else{
		$("#cmdregister").attr("disabled","disabled");
		$("#passerr").html("<FONT COLOR=Red>passwords do not match</FONT>");
	}
	}
';
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
<FONT COLOR=Red>';
if ($pw_err) {
    $registerform .= "<br />";
}
$registerform .= $pw_err;
$registerform .= '</FONT>
<label>Re-type Password: </label><input id="password2" type="password" name="password2" value="" />
<br />
<div id="passerr"></div>
<br />
<br />
    <input id="cmdregister" type="submit" value="Register Account" />
</form>';

if(!isset($_SESSION['user'])){
	$side = 'You are not logged in. <a href="https://secure144.inmotionhosting.com/~r4msof5/accounts/login.php"> click here to login</a><br /><br />';
}
$view->side = $side;
$view->links = array(array('title'=>'', 'msg'=> $registerform));

//$view->content = $view->render('content.php');
echo $view->render('main.template.php');