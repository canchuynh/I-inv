<?PHP
ini_set('display_errors', '1');
error_reporting(E_ALL);

	    // Connect to the Database
	    $dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=canhuynh';
    	$username = 'canhuynh';
    	$password = 'Thuegab!';
       
    	try {
        	$db = new PDO($dsn, $username, $password);
            $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            //get inputs for course information
			$userId = isset($_GET['userId']) ? $_GET['userId'] : 'DumpTable';
            $id = isset($_GET['id']) ? $_GET['id'] : '';
            $name = isset($_GET['name']) ? $_GET['name'] : '';
            $value = isset($_GET['value']) ? $_GET['value'] : '';
            $condition = isset($_GET['condition']) ? $_GET['condition'] : '';
			$description = isset($_GET['description']) ? $_GET['description'] : '';
			//echo $course_id . "<br />";
            /* Validation
			if (strlen($course_id) < 7 
                    || strlen($course_short_desc) < 6 
                    || strlen($course_long_desc) < 6 
                    || strlen($course_prereqs) < 6) {
                echo '{"result": "fail", "error": "Please enter valid data."}';
            } else {    */

			//build query
			$sql = "INSERT INTO " . $username . "." . $userId;
			$sql .= " VALUES ('$id', '$name', '$value', '$condition', '$description')";
		 
			//attempts to add record
			if ($db->query($sql)) {
				echo '{"result": "success"}';
				$db = null;
			} 
			
            //}   
        } catch(PDOException $e) {
                if ((int)($e->getCode()) == 23000) {
                    echo '{"result": "fail", "error": "That item already exists."}';
                } else {
                    echo 'Error Number: ' . $e->getCode() . '<br>';
                    echo '{"result": "fail", "error": "Unknown error (' . (((int)($e->getCode()) + 123) * 2) .')"}';
                }
        }
?>
