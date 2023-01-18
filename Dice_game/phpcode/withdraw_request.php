<?php 
$server="localhost"; 
$username="your_db_username"; 
$password="your_db_password";
$database="db_name";    
$conn = new mysqli($server, $username, $password, $database);

                $user_email = $_POST['user_email'];
		$name=$_POST['name'];
		$uid=$_POST['uid'];
		$withdraw_point=$_POST['withdraw_point'];
                $date_time=$_POST['date_time'];
                $status=$_POST['status'];
                $coinbase_email=$_POST['coinbase_email'];
                
                

		$sql = "INSERT INTO withdraw_table (name,user_email,uid,withdraw_point,date_time,status,coinbase_email) VALUES ('$name', '$user_email','$uid','$withdraw_point','$date_time','$status','$coinbase_email')";
		$response = mysqli_query($conn,$sql);
		


        if ($response) {
        
            $result['success'] = "1";
            $result['message'] = "withdraw req success ";
            echo json_encode($result);

            mysqli_close($conn);

        } else {

            $result['success'] = "0";
            $result['message'] = "withdraw req faild!";
            echo json_encode($result);

            mysqli_close($conn);

        }



?>
