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

			//Drop Table
			$sql = "TRUNCATE Inventory;";
		 
			//attempts to drop table
			if ($db->query($sql)) {
				echo '{"result": "success"}';
				$db = null;
			}  
        } catch(PDOException $e) {
        	$error_message = $e->getMessage();
        	echo 'There was an error connecting to the database.';
			echo $error_message;
        	exit();
        }
?>
