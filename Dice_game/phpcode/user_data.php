<?php
$server="localhost"; 
$username="your_db_username"; 
$password="your_db_password";
$database="db_name"; 
$conn = new mysqli($server, $username, $password, $database);


     $uid= $_POST['uid'];
    $sql = "SELECT * FROM users WHERE uid='$uid'"; 
    
    
    $response = mysqli_query($conn, $sql);
    
    
    
    
    
    
$result = array();
 $result['data'] = array();
 
if ($response){


        $row = mysqli_fetch_assoc($response);

        
            
            
            $index['name'] = $row['name'];
            
            $index['uid'] = $row['uid'];
            
            $index['email'] = $row['email'];
            
            $index['codeuse_ornot'] = $row['codeuse_ornot'];
            $index['refer_code'] = $row['refer_code'];
            
            $index['refer_count'] = $row['refer_count'];
            
            $index['user_point'] = $row['user_point'];
            $index['invite_user_uid'] = $row['invite_user_uid'];
            $index['refer_point'] = $row['refer_point'];
            

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
