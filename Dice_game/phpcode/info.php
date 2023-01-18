<?php
$server="localhost"; 
$username="your_db_username"; 
$password="your_db_password";
$database="db_name"; 
$conn = new mysqli($server, $username, $password, $database);


    $sql = "SELECT  id, version, message, store_link, dialog_code, exit_message, work FROM version_work_info" ;
    
    
    $response = mysqli_query($conn, $sql);
    
    
    
    
$result = array();
 $result['data'] = array();
 
if ($response){


        $row = mysqli_fetch_assoc($response);

        
            
            $index['version'] = $row['version'];
            
            $index['message'] = $row['message'];
            
            $index['store_link'] = $row['store_link'];
            
            $index['dialog_code'] = $row['dialog_code'];
            $index['exit_message'] = $row['exit_message'];
            
$index['work'] = $row['work'];
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
