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
	        	
			if ($command == "inventory") {
				$select_sql = 'SELECT * FROM Inventory';
				$item_query = $db->query($select_sql);
				$items = $item_query->fetchAll(PDO::FETCH_ASSOC);
				if ($items) {	
	   				echo json_encode($items);
				}
			}
    	} catch (PDOException $e) {
        	$error_message = $e->getMessage();
        	echo 'There was an error connecting to the database.';
			echo $error_message;
        	exit();
    	}

?>
