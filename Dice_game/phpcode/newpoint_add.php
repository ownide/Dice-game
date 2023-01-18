<?php  
$server="localhost"; 
$username="xbitxyz_1"; 
$password="Android2819";
$database="xbitxyz_1";   
$conn = new mysqli($server, $username, $password, $database);


$uid=$_POST['uid'];
$user_point=$_POST['user_point'];

$sql = "UPDATE users SET user_point='$user_point' WHERE uid='$uid'";



if(mysqli_query($conn, $sql)) {
 
          $result["success"] = "1";
          $result["message"] = "success";
 
          echo json_encode($result);
          mysqli_close($conn);
      
  }
 
else{
 
   $result["success"] = "0";
   $result["message"] = "error!";
   echo json_encode($result);
 
   mysqli_close($conn);
}

?>