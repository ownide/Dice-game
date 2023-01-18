<?php 
$server="localhost"; 
$username="your_db_username"; 
$password="your_db_password";
$database="db_name"; 
$conn = new mysqli($server, $username, $password, $database);
	
		$email =$_POST['email'];
		$name=$_POST['name'];
		$codeuse_ornot=$_POST['codeuse_ornot'];
		$refer_code=$_POST['refer_code'];
		$uid=$_POST['uid'];
		$refer_count=$_POST['refer_count'];
		$user_point=$_POST['user_point'];
		
		$sql = "INSERT INTO users (name, email,uid,refer_count,codeuse_ornot,user_point,refer_code) VALUES ('$name', '$email', '$uid','$refer_count','$codeuse_ornot','$user_point','$refer_code')";
		
    $response = mysqli_query($conn, $sql);
    if ($response)  {
    
$result["success"] = "1";
        $result["message"] = "success";

        echo json_encode($result);
        mysqli_close($conn);


        
}
else {

        $result["success"] = "0";
        $result["message"] = "error";

        echo json_encode($result);
        mysqli_close($conn);
    }
?>
