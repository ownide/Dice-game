<?php  
$server="localhost"; 
$username="your_db_username"; 
$password="your_password";
$database="db_name"; 
$conn = new mysqli($server, $username, $password, $database);


$uid=$_POST['uid'];
$codeuse_ornot=$_POST['codeuse_ornot'];
$invite_user_uid=$_POST['invite_user_uid'];




$sql = "UPDATE users SET codeuse_ornot='$codeuse_ornot',invite_user_uid='$invite_user_uid' WHERE uid='$uid'";



if(mysqli_query($conn, $sql)) {
 
          $result["success"] = "1";
          $result["message"] = "curren user profile update ";
 
          echo json_encode($result);
          mysqli_close($conn);
      
  }
 
else{
 
   $result["success"] = "0";
   $result["message"] = "current user profile not update";
   echo json_encode($result);
 
   mysqli_close($conn);
}

?>
