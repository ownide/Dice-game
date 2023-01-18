<?php  
$server="localhost"; 
$username="your_db_username"; 
$password="your_db_password";
$database="db_name"; 
$conn = new mysqli($server, $username, $password, $database);
if ($conn) {
  echo "success ";
}else{

echo " fail";

}

?>
