<?PHP
ini_set('display_errors', '1');
error_reporting(E_ALL);
$command = $_GET['cmd'];

	// Connect to the Database
	$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=canhuynh';
    	$username = 'canhuynh';
    	$password = 'Thuegab!';

    	try {
        	$db = new PDO($dsn, $username, $password);
			
			$userId = isset($_GET['userId']) ? $_GET['userId'] : '';
	        	
			if ($command == "makeTable") {
				$select_sql = 
					'SELECT table_name 
					FROM information_schema.TABLES 
					WHERE TABLE_SCHEMA =\'' . $username . '\' 
						AND table_name = \'' . $userId . '\'';
				$item_query = $db->query($select_sql);
				// $items = $item_query->fetchAll(PDO::FETCH_ASSOC);
				if ($item_query->rowCount() == 0) {	//Current user does not have table
					try {
						// Make table for user
						$sql = "CREATE TABLE " . $username . "." . $userId . " (
								ID INT(11) PRIMARY KEY,
								Name VARCHAR(45) NOT NULL,
								Value DOUBLE,
								State VARCHAR(45),
								Description VARCHAR(100)
								)";
						$db->exec($sql);
						echo '{"result": "success"}';
					} catch(PDOException $e) {
						echo $e->getMessage();//Remove or change message in production code
					}
					
				}
			}
    	} catch (PDOException $e) {
        	$error_message = $e->getMessage();
        	echo 'There was an error connecting to the database.';
			echo $error_message;
        	exit();
    	}
?>
