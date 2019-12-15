<?php
$servername = "localhost";
$username = "***REMOVED***";
$password = "***REMOVED***";
$database = "***REMOVED***";
$response = array();

try {
    $conn = new mysqli($servername,$username,$password,$database);
    if ($conn->connect_error) {
        $response["success"] = 2;
        $response["message"] = "Error connecting to database";
        die("Connection went wrong: " . $conn->connect_error); 
    }
    
    $cust_name = $_GET['name'];
    $cust_email = $_GET['email'];
    $cust_phone = $_GET['phone'];
    $overall_rating = intval($_GET['rating']);
    $comments = $_GET['comments'];

    $sql = "insert into USER_FEEDBACK (CUST_NAME, CUST_EMAIL, CUST_PHONE, OVERALL_RATING, COMMENTS) values ('" . $cust_name . "', '" . $cust_email . "', '". $cust_phone . "', " . $overall_rating . ", '" . $comments . "')";

    if ($conn->query($sql) === TRUE) {
        $response["success"] = 1;
        $response["message"] = "Review Submitted Successfully";
    } else {
        $response["success"] = 0;
        $response["message"] = "Error while submitting review";
    }
    
    $conn->close();
    echo json_encode($response);
} catch(PDOException $e) {    
    echo "Connection failed: " . $e->getMessage();
}
?>

