<?php  
$server="localhost"; 
$username="xbitxyz_1"; 
$password="Android2819";
$database="xbitxyz_1";   
$conn = new mysqli($server, $username, $password, $database);
if ($conn) {
  echo "success ";
}else{

echo " fail";

}

?>