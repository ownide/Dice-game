<?php
$server="localhost"; 
$username="xbitxyz_1"; 
$password="Android2819";
$database="xbitxyz_1";   
$conn = new mysqli($server, $username, $password, $database);


    $email = $_POST['email'];
    $sql = "SELECT * FROM users WHERE email='$email'"; 
    
    
    $response = mysqli_query($conn, $sql);
    
    
    
    $count=mysqli_num_rows($response);
    
    
    
if ($count>0){


$result["success"] = "1";
        $result["message"] = "user exist ";

        echo json_encode($result);
        mysqli_close($conn);


} else{


$result["success"] = "0";
        $result["message"] = "user not exist";

        echo json_encode($result);
        mysqli_close($conn);


}
    

?>