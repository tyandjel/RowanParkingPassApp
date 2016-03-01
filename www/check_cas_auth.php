<?php
$SUCCESS_STR = "You have successfully logged into the Rowan University Central Authentication Service.<";

function get_string_between($string, $start, $end){
    $string = ' ' . $string;
    $ini = strpos($string, $start);
    if ($ini == 0) return '';
    $ini += strlen($start);
    $len = strpos($string, $end, $ini) - $ini;
    return substr($string, $ini, $len);
}


require_once("http_util.php");
$ch = curl_init();


curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, true);
//curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 2);
curl_setopt($ch, CURLOPT_CAINFO, "cas.rowan.edu.crt");

curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_VERBOSE, 1);
curl_setopt($ch, CURLOPT_HEADER, 1);

$url = "https://cas3.rowan.edu/cas/login";
curl_setopt($ch, CURLOPT_URL, $url);

// Set so curl_exec returns the result instead of outputting it.
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    
// Get the response and close the channel.
$response = curl_exec($ch);

$header_obj = parse_Response($ch,$response);

$JSESSIONID = $header_obj['Set-Cookie']['JSESSIONID'];

$header_size = curl_getinfo($ch, CURLINFO_HEADER_SIZE);
curl_close($ch);
$body = substr($response, $header_size);
$lp = get_string_between($body,"name=\"lt\" value=\"", "\" />");
// Try to log in:
$ch = curl_init();
$_POST['password'] = trim($_POST['password']);
$_POST['username'] = trim($_POST['username']);
$fields = array(
   'username' => urlencode($_POST['username']),
   'password' => urlencode($_POST['password']),
   'lt' => urlencode($lp),
   'execution' => urlencode('e1s1'),
   '_eventId' => urlencode('submit'),
   'submit' => urlencode('LOGIN')
);

foreach($fields as $key=>$value) { $fields_string .= $key.'='.$value.'&'; }
rtrim($fields_string, '&');

$url = '/cas/login;jsessionid='.$JSESSIONID;
curl_setopt($ch, CURLINFO_HEADER_OUT, true);

curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 2);
curl_setopt($ch, CURLOPT_CAINFO, "cas.rowan.edu.crt");

curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
curl_setopt($ch, CURLOPT_POSTFIELDS, $fields_string);

curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_VERBOSE, 1);
curl_setopt($ch, CURLOPT_HEADER, 1);

curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-type: application/x-www-form-urlencoded', "Cookie: JSESSIONID={$JSESSIONID}") );



$url = "https://cas3.rowan.edu/cas/login";
curl_setopt($ch, CURLOPT_URL, $url);

// Set so curl_exec returns the result instead of outputting it.
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

$response = curl_exec($ch);
//echo json_encode(curl_getinfo($ch));
$header_obj = parse_Response($ch,$response);
curl_close($ch);
//echo json_encode($header_obj);
$body = substr($response, $header_size);

if (strpos($body, $SUCCESS_STR) == TRUE){
    echo "Success authenticating with CAS.";
}else{
    echo "Failure Authenticating.";
}