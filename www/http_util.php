<?php
function parse_Response($ch,$response){
	$header_size = curl_getinfo($ch, CURLINFO_HEADER_SIZE);
	$header = substr($response, 0, $header_size);
	$body = substr($response, $header_size);
	$header_lines = explode("\r\n",$header);
	$header_obj = array();

	foreach ($header_lines as $value){
		//echo $value;
		$sep = mb_strpos($value,':');
		if($sep){
			$key = substr($value,0,$sep);
			$msg = substr($value,$sep+1);
			//echo "{$key} : {$msg} \r\n";
			$header_obj = array_merge(array($key=>$msg),$header_obj);
		}
	}

	$cookies = explode(";",$header_obj['Set-Cookie']);
	$header_obj['Set-Cookie'] = array();
	foreach ($cookies as $value){
		$peices = explode("=",$value);
		$header_obj['Set-Cookie'] = array_merge($header_obj['Set-Cookie'],array(trim($peices[0])=>trim($peices[1])));
	}
	return $header_obj;
}
?>