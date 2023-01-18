<?php
$server="localhost"; 
$username="xbitxyz_1"; 
$password="Android2819";
$database="xbitxyz_1";   
$conn = new mysqli($server, $username, $password, $database);


    $refer_code = $_POST['refer_code'];
    $sql = "SELECT * FROM users WHERE refer_code='$refer_code'"; 
    
    
    $response = mysqli_query($conn, $sql);
    

    
$result = array();
 $result['data'] = array();
    
    
    
    
    
$count=mysqli_num_rows($response);
    
    
    
if ($count===1){



        $row = mysqli_fetch_assoc($response);

        
            
            
            
            
            $index['uid'] = $row['uid'];
            
            
            
            
            
            $index['refer_count'] = $row['refer_count'];
            
            

            array_push($result['data'],$index);

            $result['success'] = "1";
            $result['message'] = "refer code is valid";
            echo json_encode($result);

            mysqli_close($conn);

        

            


} else{
  
$result['success'] = "0";
            $result['message'] = "it’s not valid";
            echo json_encode($result);

            mysqli_close($conn);
  
  
  
}
    

?>