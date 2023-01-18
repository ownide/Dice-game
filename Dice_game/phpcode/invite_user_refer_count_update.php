<?php  
$server="localhost"; 
$username="xbitxyz_1"; 
$password="Android2819";
$database="xbitxyz_1";   
$conn = new mysqli($server, $username, $password, $database);


$uid=$_POST['uid'];
$refer_count=$_POST['refer_count'];

$sql = "UPDATE users SET refer_count ='$refer_count' WHERE uid='$uid'";



if(mysqli_query($conn, $sql)) {
 
          $result["success"] = "1";
          $result["message"] = "invite user profile update done";
 
          echo json_encode($result);
          mysqli_close($conn);
      
  }
 
else{
 
   $result["success"] = "0";
   $result["message"] = "invite user profile not update";
   echo json_encode($result);
 
   mysqli_close($conn);
}

?>