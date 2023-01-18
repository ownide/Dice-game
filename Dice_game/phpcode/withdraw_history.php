<?php
$server="localhost"; 
$username="your_db_username"; 
$password="your_db_password";
$database="db_name";    
$conn = new mysqli($server, $username, $password, $database);


     $uid= $_POST['uid'];
    $sql ="SELECT *
FROM withdraw_table
WHERE id = ( SELECT MAX(id) FROM withdraw_table ) and  uid='$uid'";
    
    
    $response = mysqli_query($conn, $sql);
$result = array();
 $result['data'] = array();
 
if ($response){


        $row = mysqli_fetch_assoc($response);

            $index['coinbase_email'] = $row['coinbase_email'];
            
            $index['status'] = $row['status'];
            
            $index['withdraw_point'] = $row['withdraw_point'];
            

            array_push($result['data'],$index);

            $result['success'] = "1";
            $result['message'] = "success";
            echo json_encode($result);

            mysqli_close($conn);

} else{
  
$result['success'] = "0";
            $result['message'] = "data not found";
            echo json_encode($result);

            mysqli_close($conn);
  
  
  
}
    

?>
